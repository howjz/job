package com.github.howjz.job.manager.job;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.bean.NullJob;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.operator.execute.Executable;
import com.github.howjz.job.util.JobUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangjh
 * @date 2020/12/13 1:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractJobManager implements JobManager {

    // ================================= 数据交换 =================================
    // 执行器ID
    private final String executorId;

    // 作业详情
    private final Map<String, Job> jobMap;

    // 作业 - 任务详情
    private final Map<String, Map<String, Job>> jobTaskMap;

    public AbstractJobManager(JobDataContext dataContext) {
        this.executorId = dataContext.getExecutor().getExecutorId();
        this.jobMap = dataContext.getJobMap();
        this.jobTaskMap = dataContext.getJobTaskMap();
    }

    public abstract void calcSnapshot(Job job, Job task);

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
//        job.setId(StringUtils.isEmpty(jobId) ? String.valueOf(JobHelper.IdWorker.nextId()) : jobId);
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
    public List<Job> getTasks(String jobId) throws Exception {
        return new ArrayList<>(this.jobTaskMap.get(jobId).values());
    }

    @Override
    public void handleAddTask(Job job, Job task) throws Exception {
        this.calcSnapshot(job, task);
    }

    @Override
    public void handleWaitTask(Job job, Job task) throws Exception {
        this.calcSnapshot(job, task);
    }

    @Override
    public void handleReadyTask(Job job, Job task) throws Exception {
        this.calcSnapshot(job, task);
    }

    @Override
    public void handleRunningTask(Job job, Job task) throws Exception {
        this.calcSnapshot(job, task);
    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {
        this.calcSnapshot(job, task);
    }

    @Override
    public void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {
        this.calcSnapshot(job, task);
    }

    @Override
    public void handleSkipTask(Job job, Job task) throws Exception {
        this.calcSnapshot(job, task);
    }

    @Override
    public void handlePauseTask(Job job, Job task) throws Exception {
        this.calcSnapshot(job, task);
    }

    @Override
    public void handleStopTask(Job job, Job task) throws Exception {
        this.calcSnapshot(job, task);
    }

    @Override
    public void handleTask(Job job, Job task, JobStatus status) throws Exception {
        this.calcSnapshot(job, task);
    }

    // 获取作业-任务详情
    protected Map<String, Job> getHistoryTaskMap(Job job) {
        Map<String, Job> oldTaskMap = new HashMap<>(0);
        if (!this.jobTaskMap.containsKey(job.getId())) {
            this.jobTaskMap.put(job.getId(), new HashMap<>(0));
        }
        oldTaskMap = this.jobTaskMap.get(job.getId());
        return oldTaskMap;
    }
}
