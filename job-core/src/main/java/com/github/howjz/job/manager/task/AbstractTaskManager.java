package com.github.howjz.job.manager.task;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.constant.JobKey;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.operator.execute.Executable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * @author zhangjh
 * @date 2020/12/13 1:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractTaskManager implements TaskManager {


    // ================================= 数据交换 =================================
    // 执行器ID
    private final String executorId;

    // 作业 - 任务详情
    private final Map<String, Map<String, Job>> jobTaskMap;

    // 任务ID队列
    private final BlockingQueue<String> taskQueue;

    public AbstractTaskManager(JobDataContext dataContext) {
        this.executorId = dataContext.getExecutor().getExecutorId();
        this.taskQueue = dataContext.getTaskQueue();
        this.jobTaskMap = dataContext.getJobTaskMap();
    }

    @Override
    public abstract Job addTask(Job job, Job task) throws Exception;

    @Override
    public abstract Job addTask(Job job, Executable executable) throws Exception;

    @Override
    public synchronized Job findTask(String jobId, String taskId) throws Exception {
        Map<String, Job> stringZJobMap = this.jobTaskMap.get(jobId);
        if (stringZJobMap != null) {
            return stringZJobMap.get(taskId);
        }
        return null;
    }

    @Override
    public synchronized void startTasks(Job job, List<Job> tasks) throws Exception {
        if (tasks != null) {
            for(Job task: tasks) {
                this.startTask(job, task);
            }
        }
    }

    @Override
    public synchronized void startTask(Job job, Job task) throws Exception {
        // 1、提交ID到任务队列
        this.offerTaskId(task.getParentId() + JobKey.SEPARATOR + task.getId());
        // 2、直接进入等待
        this.handleWaitTask(job, task);
    }

    @Override
    public void pauseTask(Job job, Job task) throws Exception {

    }

    @Override
    public void stopTask(Job job, Job task) throws Exception {

    }

    @Override
    public void removeTask(Job job, Job task) throws Exception {

    }

    @Override
    public int getQueueTaskCount() throws Exception {
        return this.taskQueue.size();
    }

    @Override
    public void offerTaskId(String jobAndTaskId) throws Exception {
        // 1、塞入队列
        this.taskQueue.offer(jobAndTaskId);
    }

    @Override
    public String consumeTaskId() throws Exception {
        return this.taskQueue.poll();
    }

    @Override
    public void handleAddTask(Job job, Job task) throws Exception {
        job.setStatus(JobStatus.WAIT);
        task.setStatus(JobStatus.WAIT);
    }

    @Override
    public void handleWaitTask(Job job, Job task) throws Exception {
        task.setStatus(JobStatus.WAIT);
    }

    @Override
    public void handleReadyTask(Job job, Job task) throws Exception {
        task.setStatus(JobStatus.READY);
        if (task.getStartTime() == null) {
            task.setStartTime(new Date());
        }
    }

    @Override
    public void handleRunningTask(Job job, Job task) throws Exception {
        task.setStatus(JobStatus.RUNNING);
    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {
        task.setStatus(JobStatus.COMPLETE);
        task.setEndTime(new Date());
    }

    @Override
    public void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {
        task.setStatus(JobStatus.EXCEPTION);
        task.setException(exception);
    }

    @Override
    public void handleSkipTask(Job job, Job task) throws Exception {
        task.setStatus(JobStatus.SKIP);
        task.setEndTime(new Date());
    }

    @Override
    public void handlePauseTask(Job job, Job task) throws Exception {
        task.setStatus(JobStatus.PAUSE);
    }

    @Override
    public void handleStopTask(Job job, Job task) throws Exception {
        task.setStatus(JobStatus.STOP);
        task.setEndTime(new Date());
    }

    @Override
    public void handleTask(Job job, Job task, JobStatus status) throws Exception {
        task.setStatus(status);
    }
}
