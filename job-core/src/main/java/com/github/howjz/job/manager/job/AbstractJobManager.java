package com.github.howjz.job.manager.job;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.bean.NullJob;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.operator.execute.Executable;
import com.github.howjz.job.operator.job.JobData;
import com.github.howjz.job.operator.job.JobUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangjh
 * @date 2020/12/13 1:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractJobManager implements JobManager {

    // ================================= 数据交换 =================================
    private final JobDataContext dataContext;

    // 执行器ID
    private final String executorId;

    // 作业详情
    private final Map<String, Job> jobMap;

    // 任务详情
    private final Map<String, Job> taskMap;

    // 作业-任务关联详情
    private final Map<String, Set<String>> jobTaskRelationMap;

    public AbstractJobManager(JobDataContext dataContext) {
        this.dataContext = dataContext;
        this.executorId = dataContext.getExecutor().getExecutorId();
        this.jobMap = dataContext.getJobData().getJobMap();
        this.taskMap = dataContext.getJobData().getTaskMap();
        this.jobTaskRelationMap = dataContext.getJobData().getJobTaskRelationMap();
    }

    @Override
    public abstract Job findJob(String jobId, boolean detail) throws Exception;


    @Override
    public abstract void syncJob(Job job) throws Exception;

    @Override
    public abstract void syncTask(Job job, Job task) throws Exception;

    @Override
    public abstract void syncTasks(Job job, List<Job> tasks) throws Exception;

    @Override
    public Job createJob(String jobId, Executable executable) throws Exception {
        Job job = new Job();
        job.setIssuer(this.executorId);
        job.setStatus(JobStatus.READY);
        this.syncJob(job);
        return job;
    }

    @Override
    public Job findOrCreateJob(String jobId, Executable executable) throws Exception {
        Job job = this.findJob(jobId, true);
        if (job == null || job instanceof NullJob) {
            job = this.createJob(jobId, executable);
        }
        return job;
    }

    @Override
    public List<Job> getJobs() {
        return this.jobMap.values().stream()
                .map(JobUtil::cloneJob)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Job> getTasks(String jobId) throws Exception {
        // 1、先查找关联
        Set<String> taskIds = this.jobTaskRelationMap.get(jobId);
        if (taskIds == null) {
            return Collections.emptyList();
        }
        return this.taskMap.entrySet()
                .stream()
                .filter(entry -> taskIds.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }


    @Override
    public void handleAddTask(Job job, Job task) throws Exception {
        JobUtil.calcSnapshot(dataContext, job, task);
    }

    @Override
    public void handleWaitTask(Job job, Job task) throws Exception {
        JobUtil.calcSnapshot(dataContext, job, task);
    }

    @Override
    public void handleReadyTask(Job job, Job task) throws Exception {
        JobUtil.calcSnapshot(dataContext, job, task);
    }

    @Override
    public void handleRunningTask(Job job, Job task) throws Exception {
        JobUtil.calcSnapshot(dataContext, job, task);
    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {
        JobUtil.calcSnapshot(dataContext, job, task);
    }

    @Override
    public void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {
        JobUtil.calcSnapshot(dataContext, job, task);
    }

    @Override
    public void handleSkipTask(Job job, Job task) throws Exception {
        JobUtil.calcSnapshot(dataContext, job, task);
    }

    @Override
    public void handlePauseTask(Job job, Job task) throws Exception {
        JobUtil.calcSnapshot(dataContext, job, task);
    }

    @Override
    public void handleStopTask(Job job, Job task) throws Exception {
        JobUtil.calcSnapshot(dataContext, job, task);
    }

    @Override
    public void handleTask(Job job, Job task, JobStatus status) throws Exception {
        JobUtil.calcSnapshot(dataContext, job, task);
    }
}
