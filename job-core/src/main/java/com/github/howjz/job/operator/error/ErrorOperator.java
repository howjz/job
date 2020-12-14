package com.github.howjz.job.operator.error;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.operator.OperatorEnableFlag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/11 11:30
 */
@Slf4j
public class ErrorOperator extends GenericOperator<Errorable> {

    private final Map<String, Errorable> errorableMap;

    private final Map<String, Integer> notifyMap;

    public ErrorOperator(JobDataContext dataContext) {
        super(dataContext);
        this.errorableMap = dataContext.getErrorData().getErrorableMap();
        this.notifyMap = dataContext.getNotifyData().getNotifyMap();
    }

    @Override
    public Operate operate() {
        return Operate.ERROR;
    }

    @Override
    public void handleCreateJob(Job job) throws Exception {
        // 默认发生错误其他子任务都是跳过
        job.error(((task, exception) -> ErrorPolicy.SKIP));
    }

    @Override
    public void handleOperate(Job jobOrTask, Errorable operator) {
        this.errorableMap.put(jobOrTask.getId(), operator);
    }

    @Override
    public synchronized void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {
        job.getExceptions().put(task.getId(), exception);
        Errorable errorable = this.errorableMap.get(job.getId());
        ErrorPolicy errorPolicy = errorable.error(task, exception);
        if (errorPolicy == null) {
            throw new RuntimeException("定义错误函数错误，返回的错误策略不能为空");
        }
        // 1、获取错误策略
        switch (errorPolicy) {
            case KEEP:
                break;
            case SKIP:
                // 2、将 非结束的其他任务 存放跳过标志
                job.getTasks().forEach(t -> {
                    if (!t.getStatus().isEnd() && !StringUtils.equals(t.getId(), task.getId())) {
                        if (t.getRetry() != null && t.getRetry() == 0) {
                            this.notifyMap.put(ErrorUtil.targetSkipTaskId(t), JobStatus.SKIP.getValue());
                        }
                    }
                });
                break;
        }
    }

    @Override
    public synchronized OperatorEnableFlag enable(Job job, Job task) throws Exception {
        OperatorEnableFlag flag = OperatorEnableFlag.KEEP;
        String currentSkipTaskId = ErrorUtil.currentSkipTaskId(task);
        // 1、检查当前触发状态中是否包含
        Integer i = this.notifyMap.get(currentSkipTaskId);
        if (i == null) {
            return flag;
        }
        JobStatus status = JobStatus.findByValue(i);
        if (status == JobStatus.SKIP) {
            flag = OperatorEnableFlag.DISABLE;
            // 2、调用中间变量触发
            JobHelper.manager.handleSkipTask(job, task);
        }
        return flag;
    }

    @Override
    public void handleCompleteJob(Job job) throws Exception {
        // 1、作业完成后，移除错误回调函数记录
//        this.errorableMap.remove(job.getId());
        // 2、移除跳过标记
        job.getTasks().forEach(t -> {
            this.notifyMap.remove(ErrorUtil.targetSkipTaskId(t));
        });
    }

}
