package com.github.howjz.job.operator.job;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.bean.Snapshot;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.manager.GenericExecutorManager;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.operator.OperatorEnableFlag;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangjh
 * @date 2020/12/15 22:16
 */
@Slf4j
public class JobOperator extends GenericOperator<Job> {

    private final Map<String, Job> jobMap;
    private final Map<String, Set<String>> jobTaskRelationMap;
    private final Map<String, Job> taskMap;

    public JobOperator(JobDataContext dataContext) {
        super(dataContext);
        this.jobMap = dataContext.getJobData().getJobMap();
        this.jobTaskRelationMap = dataContext.getJobData().getJobTaskRelationMap();
        this.taskMap = dataContext.getJobData().getTaskMap();
    }

    @Override
    public Operate operate() {
        return Operate.JOB;
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
    public void handleCreateJob(Job job) throws Exception {

    }

    @Override
    public void handleReadyJob(Job job) throws Exception {

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
    public synchronized void handleRemoveJob(Job job) throws Exception {
        if (job.isEnd()) {
            // 1、查找关联ID
            Set<String> taskIds = this.jobTaskRelationMap.remove(job.getId());
            // 2、移除任务信息
            for(String taskId: taskIds) {
                this.taskMap.remove(taskId);
            }
            // 3、移除作业信息
            this.jobMap.remove(job.getId());
        }
    }

    @Override
    public void handleCompleteJob(Job job) throws Exception {

    }

    @Override
    public void handleEndJob(Job job) throws Exception {

    }

}
