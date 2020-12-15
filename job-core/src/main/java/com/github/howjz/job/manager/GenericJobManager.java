package com.github.howjz.job.manager;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.bean.NullJob;
import com.github.howjz.job.manager.job.AbstractJobManager;
import com.github.howjz.job.operator.job.JobUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangjh
 * @date 2020/12/7 15:16
 */
@Slf4j
public class GenericJobManager extends AbstractJobManager {

    public GenericJobManager(JobDataContext dataContext) {
        super(dataContext);
    }

    @Override
    public Job findJob(String jobId, boolean detail) throws Exception {
        Job job = this.getJobMap().get(jobId);
        if (job != null) {
            job.setTasks(new ArrayList<>(JobUtil.getHistoryTaskMap(this.getDataContext(), jobId).values()));
            JobUtil.calcSnapshot(this.getDataContext(), job, job.getTasks().get(0));
            if(!detail) {
                // 需要返回一个复制的 job
                return JobUtil.cloneJob(job);
            }
        } else {
            job = NullJob.getInstance();
        }
        return job;
    }

    // 同步作业状态
    @Override
    public void syncJob(Job job) throws Exception {
        this.getJobMap().put(job.getId(), job);
    }

    // 同步任务状态
    @Override
    public synchronized void syncTask(Job job, Job task) throws Exception {
        this.addRelation(job.getId(), task.getId());
        // 2、同步当前任务详情
        this.getTaskMap().put(task.getId(), task);
    }

    // 同步大量任务详情
    @Override
    public synchronized void syncTasks(Job job, List<Job> tasks) throws Exception {
        // 1、查询旧的任务详情
        this.addRelations(job.getId(), tasks.stream().map(Job::getId).collect(Collectors.toSet()));
        // 2、同步当前任务详情
        this.getTaskMap().putAll(tasks.stream()
                .collect(Collectors.toMap(Job::getId, t -> t)));
    }

    private synchronized void addRelations(String jobId, Set<String> taskIds) {
        // 1、同步关联
        Set<String> existTaskIds = this.getJobTaskRelationMap().get(jobId);
        if (existTaskIds != null) {
            taskIds.addAll(existTaskIds);
        }
        this.getJobTaskRelationMap().put(jobId, taskIds);
    }

    private void addRelation(String jobId, String taskId) {
        this.addRelations(jobId, new HashSet<>(Collections.singleton(taskId)));
    }

}
