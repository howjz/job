package com.github.howjz.job.operator.genericjob;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.operator.then.Thenable;
import org.apache.commons.lang.ObjectUtils;

/**
 *  自定义作业操作器：
 *  1、进行 任务拼接
 *  2、进行 then拼接
 *  3、进行 回调触发
 * @author zhangjh
 * @date 2020/12/12 14:30
 */
public class GenericJobOperator extends GenericOperator<ObjectUtils.Null> {
    public GenericJobOperator(JobDataContext dataContext) {
        super(dataContext);
    }

    @Override
    public Operate operate() {
        return Operate.GENERIC_JOB;
    }

    @Override
    public void handleCreateJob(Job job) throws Exception {
        if (job instanceof GenericJob) {
            // 当前为 GenericJob，生成任务并设置then
            GenericJob genericJob = (GenericJob) job;
            genericJob.generateTasks();
            genericJob.then((Thenable) genericJob);
            genericJob.handleCreateJob(job);
        }
    }

    @Override
    public void handleAddTask(Job job, Job task) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleAddTask(job, task);
        }
    }

    @Override
    public void handleWaitTask(Job job, Job task) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleWaitTask(job, task);
        }
    }

    @Override
    public void handleReadyTask(Job job, Job task) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleReadyTask(job, task);
        }
    }

    @Override
    public void handleRunningTask(Job job, Job task) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleRunningTask(job, task);
        }
    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleCompleteTask(job, task);
        }
    }

    @Override
    public void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleExceptionTask(job, task, exception);
        }
    }

    @Override
    public void handleSkipTask(Job job, Job task) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleSkipTask(job, task);
        }
    }

    @Override
    public void handlePauseTask(Job job, Job task) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handlePauseTask(job, task);
        }
    }

    @Override
    public void handleStopTask(Job job, Job task) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleStopTask(job, task);
        }
    }

    @Override
    public void handleStartJob(Job job) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleStartJob(job);
        }
    }

    @Override
    public void handleWaitingJob(Job job) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleWaitingJob(job);
        }
    }

    @Override
    public void handlePauseJob(Job job) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handlePauseJob(job);
        }
    }

    @Override
    public void handleStopJob(Job job) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleStopJob(job);
        }
    }

    @Override
    public void handleRemoveJob(Job job) throws Exception {
        if (job instanceof GenericJob) {
            ((GenericJob) job).handleRemoveJob(job);
        }
    }
}
