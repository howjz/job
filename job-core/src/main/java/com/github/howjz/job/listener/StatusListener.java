package com.github.howjz.job.listener;

import com.github.howjz.job.Job;
import com.github.howjz.job.constant.JobStatus;

/**
 * @author zhangjh
 * @date 2020/12/7 15:14
 */
public interface StatusListener {

    /**
     * 子任务添加
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常
     */
    void handleAddTask(Job job, Job task) throws Exception;

    /**
     * 任务等待
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常
     */
    void handleWaitTask(Job job, Job task) throws Exception;

    /**
     * 任务就绪
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常
     */
    void handleReadyTask(Job job, Job task) throws Exception;

    /**
     * 任务执行
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常
     */
    void handleRunningTask(Job job, Job task) throws Exception;

    /**
     * 任务完成
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常
     */
    void handleCompleteTask(Job job, Job task) throws Exception;

    /**
     * 任务异常
     * @param job           作业
     * @param task          任务
     * @param exception     异常信息
     * @throws Exception    异常
     */
    void handleExceptionTask(Job job, Job task, Exception exception) throws Exception;

    /**
     * 任务跳过
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常
     */
    void handleSkipTask(Job job, Job task) throws Exception;

    /**
     * 任务暂停
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常
     */
    void handlePauseTask(Job job, Job task) throws Exception;

    /**
     * 任务停止
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常
     */
    void handleStopTask(Job job, Job task) throws Exception;

    /**
     * 状态触发
     * @param job           作业
     * @param task          任务
     * @param status        状态
     * @throws Exception         异常
     */
    void handleTask(Job job, Job task, JobStatus status) throws Exception;

}
