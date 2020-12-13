package com.github.howjz.job.operator.error;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.operator.OperatorEnableFlag;
import org.apache.commons.lang.ObjectUtils;

import java.util.Map;

/**
 *  重试操作器
 * @author zhangjh
 * @date 2020/12/13 0:04
 */
public class RetryOperator extends GenericOperator<ObjectUtils.Null> {

    private final Map<String, Integer> notifyMap;

    public RetryOperator(JobDataContext dataContext) {
        super(dataContext);
        this.notifyMap = dataContext.getNotifyData().getNotifyMap();
    }

    @Override
    public Operate operate() {
        return Operate.RETRY;
    }

    @Override
    public synchronized OperatorEnableFlag enable(Job job, Job task) throws Exception {
        String targetSkipTaskId = ErrorUtil.targetSkipTaskId(task);
        this.notifyMap.remove(targetSkipTaskId);
        return OperatorEnableFlag.KEEP;
    }

    @Override
    public synchronized void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {
        if (task.get_retry() != null && task.get_retry() > 0) {
            // 1、将任务重新设置为等待中
            JobHelper.manager.handleWaitTask(job, task);
            // 2、快照计算
            JobHelper.manager.getJobManager().calcSnapshot(job, task);
            // 3、移除错误信息
            job.getExceptions().remove(task.getId());
            // 4、将任务ID重新设置入队列
            JobHelper.manager.offerTaskId(task.getParentId() + "-" + task.getId());
            // 5、重试次数 - 1
            task.set_retry(task.get_retry() - 1);
        }
    }
}
