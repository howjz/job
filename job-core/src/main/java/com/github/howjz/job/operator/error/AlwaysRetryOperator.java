package com.github.howjz.job.operator.error;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.operator.GenericOperator;

import java.util.Set;

/**
 * @author zhangjh
 * @date 2020/12/13 12:50
 */
public class AlwaysRetryOperator extends GenericOperator<String> {

    private final Set<String> alwaysRetrySet;

    public AlwaysRetryOperator(JobDataContext dataContext) {
        super(dataContext);
        this.alwaysRetrySet = dataContext.getErrorData().getAlwaysRetrySet();
    }

    @Override
    public Operate operate() {
        return Operate.ALWAYS_RETRY;
    }

    @Override
    public void handleOperate(Job jobOrTask, String operator) {
        this.alwaysRetrySet.add(jobOrTask.getId());
    }

    @Override
    public synchronized void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {
        // 如果当前作业标记了总是重试，当任务重试次数不足时，设置为 1
        if (this.alwaysRetrySet.contains(job.getId())) {
            if (task.get_retry() == null || task.get_retry() == 1 || task.get_retry() == 2) {
                task.set_retry(2);
            }
        }
    }

}
