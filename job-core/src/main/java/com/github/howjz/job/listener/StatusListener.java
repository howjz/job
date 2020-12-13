package com.github.howjz.job.listener;

import com.github.howjz.job.Job;
import com.github.howjz.job.constant.JobStatus;

/**
 * @author zhangjh
 * @date 2020/12/7 15:14
 */
public interface StatusListener {

    /**
     * 添加子任务
     * @param job
     * @param task
     * @throws Exception
     */
    void handleAddTask(Job job, Job task) throws Exception;

    /**
     * 任务等待
     * @param task
     * @throws Exception
     */
    void handleWaitTask(Job job, Job task) throws Exception;

    /**
     * 任务就绪
     * @param task
     * @throws Exception
     */
    void handleReadyTask(Job job, Job task) throws Exception;

    /**
     * 任务执行
     * @param task
     * @throws Exception
     */
    void handleRunningTask(Job job, Job task) throws Exception;

    /**
     * 任务执行
     * @param task
     * @throws Exception
     */
    void handleCompleteTask(Job job, Job task) throws Exception;

    /**
     * 任务异常
     * @param task
     * @param exception
     */
    void handleExceptionTask(Job job, Job task, Exception exception) throws Exception;

    /**
     * 任务跳过
     * @param task
     * @throws Exception
     */
    void handleSkipTask(Job job, Job task) throws Exception;

    /**
     * 任务暂停
     * @param task
     * @throws Exception
     */
    void handlePauseTask(Job job, Job task) throws Exception;

    /**
     * 任务停止
     * @param task
     * @throws Exception
     */
    void handleStopTask(Job job, Job task) throws Exception;

    /**
     * 状态触发
     * @param job
     * @param task
     * @param status
     * @throws Exception
     */
    void handleTask(Job job, Job task, JobStatus status) throws Exception;

}
