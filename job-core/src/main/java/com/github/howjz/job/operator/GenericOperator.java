package com.github.howjz.job.operator;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.manager.GenericExecutorManager;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangjh
 * @date 2020/12/11 10:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class GenericOperator<T> implements Operator<T> {

    private JobDataContext dataContext;

    private GenericExecutorManager executorManager;

    public GenericOperator(JobDataContext dataContext) {
        this.dataContext = dataContext;
    }

    @Override
    public void handleOperate(Job jobOrTask, T operator) throws Exception {

    }

    @Override
    public void handleAddTask(Job job, Job task) throws Exception {

    }

    @Override
    public OperatorEnableFlag enable(Job job, Job task) throws Exception {
        return OperatorEnableFlag.KEEP;
    }

    @Override
    public void handleWaitTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleReadyTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleRunningTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {

    }

    @Override
    public void handleSkipTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handlePauseTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleStopTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleTask(Job job, Job task, JobStatus status) throws Exception {

    }

    @Override
    public void handleInit(GenericExecutorManager executorManager) {
        this.executorManager = executorManager;
    }

    @Override
    public void handleCreateJob(Job job) throws Exception {

    }

    @Override
    public void handleRun(GenericExecutorManager executorManager) throws Exception {

    }

    @Override
    public void handleStartJob(Job job) throws Exception {

    }

    @Override
    public void handleWaitingJob(Job job) throws Exception {

    }

    @Override
    public void handlePauseJob(Job job) throws Exception {

    }

    @Override
    public void handleStopJob(Job job) throws Exception {

    }

    @Override
    public void handleRemoveJob(Job job) throws Exception {

    }

    @Override
    public void handleFinishJob(Job job) throws Exception {

    }
}
