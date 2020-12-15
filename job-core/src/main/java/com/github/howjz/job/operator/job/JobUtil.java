package com.github.howjz.job.operator.job;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.bean.Snapshot;
import com.github.howjz.job.constant.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangjh
 * @date 2020/12/15 22:27
 */
@Slf4j
public class JobUtil {

    public static boolean executable(Job task) {
        JobStatus taskStatus = task.getStatus();
        return !taskStatus.isEnd() &&
                taskStatus != JobStatus.RUNNING &&
                (task.get_added() == null || BooleanUtils.isFalse(task.get_added()));
    }

    public static Job cloneJob(Job jobExecutor) {
        Job cloneJob = new Job();
        cloneJob.setId(jobExecutor.getId());
        cloneJob.setParentId(jobExecutor.getParentId());
        cloneJob.setPurpose(jobExecutor.getPurpose());
        cloneJob.setParam(jobExecutor.getParam());
        cloneJob.setResult(jobExecutor.getResult());
        cloneJob.setStatus(jobExecutor.getStatus());
        cloneJob.setStartTime(jobExecutor.getStartTime());
        cloneJob.setEndTime(jobExecutor.getEndTime());
        cloneJob.set_retry(jobExecutor.get_retry());
        cloneJob.setIssuer(jobExecutor.getIssuer());
        cloneJob.setComplete(jobExecutor.getComplete());
        cloneJob.setEnd(jobExecutor.getEnd());
        cloneJob.setTotal(jobExecutor.getTotal());
        cloneJob.setProgress(jobExecutor.getProgress());
        cloneJob.setException(jobExecutor.getException());
        cloneJob.setCreateTime(jobExecutor.getCreateTime());
        cloneJob.setExceptions(jobExecutor.getExceptions());
        cloneJob.setSnapshot(jobExecutor.getSnapshot());
        cloneJob.setMessage(jobExecutor.getMessage());
        cloneJob.setTasks(new ArrayList<>(0));
        cloneJob.set_added(jobExecutor.get_added());
        cloneJob.set_waited(jobExecutor.get_waited());
        cloneJob.set_cross(jobExecutor.get_cross());
        cloneJob.set_restart(jobExecutor.get_restart());
        return cloneJob;
    }

    /**
     * 获取任务历史信息
     * @param jobId
     * @return
     */
    public synchronized static Map<String, Job> getHistoryTaskMap(JobDataContext dataContext, String jobId) {
        Map<String, Set<String>> jobTaskRelationMap = dataContext.getJobData().getJobTaskRelationMap();
        Map<String, Job> taskMap = dataContext.getJobData().getTaskMap();
        Map<String, Job> oldTaskMap = new HashMap<>(0);
        Set<String> taskIds = jobTaskRelationMap.get(jobId);
        if (taskIds == null) {
            return Collections.emptyMap();
        }
        return taskMap.entrySet()
                .stream()
                .filter(entry -> taskIds.contains(entry.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }


    /**
     * 计算快照进度
     * @param job
     * @param task
     */
    public synchronized static void calcSnapshot(JobDataContext dataContext, Job job, Job task) {
        if (job == null) {
            log.error("当前作业为空");
            return;
        }
        Map<String, Job> jobTaskMap = JobUtil.getHistoryTaskMap(dataContext, job.getId());
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

}
