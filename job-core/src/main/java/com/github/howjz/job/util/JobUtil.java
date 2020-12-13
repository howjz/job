package com.github.howjz.job.util;

import com.github.howjz.job.Job;
import com.github.howjz.job.constant.JobStatus;
import org.apache.commons.lang.BooleanUtils;

import java.util.ArrayList;

/**
 * @author zhangjh
 * @date 2020/12/4 16:27
 */
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
}
