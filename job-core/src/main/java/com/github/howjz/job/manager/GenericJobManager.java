package com.github.howjz.job.manager;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.bean.NullJob;
import com.github.howjz.job.bean.Snapshot;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.manager.job.AbstractJobManager;
import com.github.howjz.job.util.JobUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
            if (job.getTasks().size() > 0) {
                this.calcSnapshot(job, job.getTasks().get(0));
            }
            if(!detail) {
                // 需要返回一个复制的 job
                return JobUtil.cloneJob(job);
            }
        } else {
            job = NullJob.getInstance();
        }
        return job;
    }

    @Override
    public void calcSnapshot(Job job, Job task) {
        if (job == null) {
            log.error("当前作业为空");
            return;
        }
        Map<String, Job> jobTaskMap = this.getHistoryTaskMap(job);
        Snapshot snapshot = Snapshot.fromJobTaskMap(job, jobTaskMap);
        job.setSnapshot(snapshot);
        long allExceptionCount = snapshot.getException();
        long allStopCount = snapshot.getStop();
        long allTotalCount = snapshot.getTotal();
        long allCompleteCount = snapshot.getComplete();
        long allEndCount = snapshot.getEnd();
        if (allTotalCount == 0) {
            return;
        }
        job.setComplete(allCompleteCount);
        job.setEnd(allEndCount);
        job.setTotal(allTotalCount);
        // 1、计算具体进度
        job.setProgress(Math.round(job.getComplete().floatValue() / job.getTotal().intValue() * 100));
        // 2、进行总计数
        if (allTotalCount == allEndCount) {
            job.set_waited(false);
            job.setProgress(100);
            if (job.getEndTime() == null) {
                job.setEndTime(new Date());
            }
            if (allExceptionCount > 0) {
                job.setStatus(JobStatus.EXCEPTION);
            } else if (allStopCount > 0) {
                job.setStatus(JobStatus.STOP);
            } else {
                job.setStatus(JobStatus.COMPLETE);
            }
        }
    }

    // 同步作业状态
    @Override
    public void syncJob(Job job) throws Exception {
        this.getJobMap().put(job.getId(), job);
    }

    // 同步任务状态
    @Override
    public void syncTask(Job job, Job task) throws Exception {
        // 1、查询旧的任务详情
        Map<String, Job> historyTaskMap = this.getHistoryTaskMap(job);
        // 2、同步当前任务详情
        historyTaskMap.put(task.getId(), task);
        this.getJobTaskMap().put(job.getId(), historyTaskMap);
    }

    // 同步大量任务详情
    @Override
    public void syncTasks(Job job, List<Job> tasks) throws Exception {
        // 1、查询旧的任务详情
        Map<String, Job> historyTaskMap = this.getHistoryTaskMap(job);
        // 2、同步当前任务详情
        for(Job task: tasks) {
            historyTaskMap.put(task.getId(), task);
        }
        this.getJobTaskMap().put(job.getId(), historyTaskMap);
    }

}
