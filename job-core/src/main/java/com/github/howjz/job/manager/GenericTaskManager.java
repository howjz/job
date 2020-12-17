package com.github.howjz.job.manager;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.bean.NullJob;
import com.github.howjz.job.constant.JobType;
import com.github.howjz.job.manager.task.AbstractTaskManager;
import com.github.howjz.job.operator.execute.Executable;

import java.util.Set;

/**
 * @author zhangjh
 * @date 2020/12/7 15:28
 */
public class GenericTaskManager extends AbstractTaskManager {

    public GenericTaskManager(JobDataContext dataContext) {
        super(dataContext);
    }

    @Override
    public Job addTask(Job job, Job task) throws Exception {
        task.setParentId(job.getId());
        task.setRetry(job.getRetry());
        task.set_retry(job.get_retry());
        task.setType(JobType.getJobType(task));
        task.setIssuer(this.getExecutorId());
        job.getTasks().add(task);
        job.setType(JobType.getJobType(job));
        return task;
    }

    @Override
    public Job addTask(Job job, Executable executable) throws Exception {
        Job task = new Job();
        task.execute(executable);
        return this.addTask(job, task);
    }

    @Override
    public synchronized void pauseTask(Job job, Job task) throws Exception {
//        this.getNotifyStatusMap().put(job.getId() + JobKey.SEPARATOR + task.getId(), JobStatus.PAUSE.getValue());
    }

    @Override
    public synchronized void stopTask(Job job, Job task) throws Exception {
//        this.getNotifyStatusMap().put(job.getId() + JobKey.SEPARATOR + task.getId(), JobStatus.STOP.getValue());
    }

    @Override
    public synchronized void removeTask(Job job, Job task) throws Exception {
        if (job == null || job instanceof NullJob) {
            throw new RuntimeException("当前作业为空");
        }
        if (task == null || task instanceof NullJob) {
            throw new RuntimeException("当前任务为空");
        }
        if (!task.getStatus().isEnd()) {
            throw new RuntimeException("当前任务未结束，无法移除");
        }
        // 1、移除依赖
        Set<String> taskIds = this.getJobTaskRelationMap().get(job.getId());
        if (taskIds != null) {
            taskIds.remove(task.getId());
            this.getJobTaskRelationMap().put(job.getId(), taskIds);
        }
        this.getTaskMap().remove(task.getId());
        JobHelper.manager.syncJob(job);
    }

}
