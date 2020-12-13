package com.github.howjz.job.operator.notify;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.operator.Operator;
import com.github.howjz.job.operator.OperatorEnableFlag;
import com.github.howjz.job.operator.error.ErrorUtil;
import com.github.howjz.job.util.JobUtil;

import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/13 21:06
 */
public class NotifyOperator extends GenericOperator<NotifyType> {

    private final Map<String, Integer> notifyMap;

    public NotifyOperator(JobDataContext dataContext) {
        super(dataContext);
        this.notifyMap = dataContext.getNotifyData().getNotifyMap();
    }

    @Override
    public Operate operate() {
        return Operate.NOTIFY;
    }

    @Override
    public synchronized OperatorEnableFlag enable(Job job, Job task) throws Exception {
        OperatorEnableFlag flag = OperatorEnableFlag.KEEP;
        // 1、获取当前任务的目标触发状态
        String targetNotifyTaskId = NotifyUtil.targetNotifyTaskId(task);
        Integer i = this.notifyMap.get(targetNotifyTaskId);
        if (i != null) {
            JobStatus status = JobStatus.findByValue(i);
            switch (status) {
                case PAUSE:
                    // 2.1、重新放回任务队列
                    flag = OperatorEnableFlag.RESET;
                    // 2.2、手动触发任务暂停事件
                    JobHelper.manager.handlePauseTask(job, task);
                    break;
                case STOP:
                    // 3.1、直接停了
                    flag = OperatorEnableFlag.DISABLE;
                    // 3.2、手动触发任务停止时间
                    JobHelper.manager.handleStopTask(job, task);
                    break;
            }
        }
        return flag;
    }

    @Override
    public synchronized void handleOperate(Job jobOrTask, NotifyType operator) throws Exception {
        switch (operator) {
            case PAUSE:
                this.notifyJob(jobOrTask, JobStatus.PAUSE);
                break;
            case STOP:
                this.notifyJob(jobOrTask, JobStatus.STOP);
                break;
            case REMOVE:
                if (!jobOrTask.isFinish()) {
                    throw new RuntimeException("当前作业未结束，无法移除");
                }
                // 主动触发 remove
                JobHelper.manager.handleRemoveJob(jobOrTask);
                break;
        }
    }

    private synchronized void notifyJob(Job job, JobStatus status) throws Exception {
        job.getTasks()
                .stream()
                .filter(task -> {
                    // 1、筛选出 未结束 / 异常但是重试次数 > 0 的任务
                    JobStatus currentStatus = task.getStatus();
                    if (JobStatus.EXCEPTION == currentStatus && task.get_retry() != null && task.get_retry() > 0) {
                        return true;
                    }
                    return JobStatus.EXCEPTION != currentStatus && !currentStatus.isEnd();
                })
                .forEach(task -> {
                    // 2、存放状态进入触发map
                    this.notifyMap.put(NotifyUtil.targetNotifyTaskId(task), status.getValue());
                });
        // 3、手动触发时间
        switch (status) {
            case PAUSE:
                JobHelper.manager.handlePauseJob(job);
                break;
            case STOP:
                JobHelper.manager.handleStopJob(job);
                break;
        }
    }
}
