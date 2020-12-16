package com.github.howjz.job.handler;

import com.github.howjz.job.Job;
import com.github.howjz.job.operator.execute.Executable;

import java.util.List;

/**
 * @author zhangjh
 * @date 2020/12/4 16:26
 */
public interface JobHandler {

    /**
     * 创建作业
     * @param jobId         固定的作业ID
     * @param executable    环绕回调，每个子任务完成后都会执行
     * @return  Job         作业
     * @throws Exception    异常
     */
    Job createJob(String jobId, Executable executable) throws Exception;

    /**
     * 查找作业
     * @param jobId         作业ID
     * @param detail        是否返回子任务信息
     * @return              作业
     * @throws Exception    异常
     */
    Job findJob(String jobId, boolean detail) throws Exception;

    /**
     * 根据作业ID查找或者创建作业
     * @param jobId         固定的作业ID
     * @param executable    环绕回调，每个子任务完成后都会执行
     * @return              作业
     * @throws Exception    异常
     */
    Job findOrCreateJob(String jobId, Executable executable) throws Exception;

    /**
     * 获取所有作业
     * @return              作业列表
     */
    List<Job> getJobs();

    /**
     * 根据作业ID获取所有任务
     * @param jobId         作业ID
     * @return              任务列表
     * @throws Exception    异常
     */
    List<Job> getTasks(String jobId) throws Exception;

    /**
     * 同步作业状态
     * @param job           作业
     * @throws Exception    异常
     */
    void syncJob(Job job) throws Exception;

    /**
     * 同步任务状态
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常
     */
    void syncTask(Job job, Job task) throws Exception;

    /**
     * 同步大量任务详情
     * @param job           作业
     * @param tasks         任务列表
     * @throws Exception    异常
     */
    void syncTasks(Job job, List<Job> tasks) throws Exception;
}
