package com.github.howjz.job.listener;

import com.github.howjz.job.Job;
import com.github.howjz.job.manager.GenericExecutorManager;

/**
 * @author zhangjh
 * @date 2020/12/11 17:25
 */
public interface ExecutorListener {

    /**
     * 启动
     * @param executorManager   执行器管理器
     */
    void handleInit(GenericExecutorManager executorManager);

    /**
     * 执行中
     * @param executorManager   执行器管理器
     * @throws Exception        异常
     */
    void handleRun(GenericExecutorManager executorManager) throws Exception;

    /**
     * 作业创建
     * @param job               作业
     * @throws Exception        异常
     */
    void handleCreateJob(Job job) throws Exception;

    /**
     * 作业就绪
     * @param job           作业
     * @throws Exception    异常
     */
    void handleReadyJob(Job job) throws Exception;

    /**
     * 作业开始
     * @param job           作业
     * @throws Exception    异常
     */
    void handleStartJob(Job job) throws Exception;

    /**
     * 作业等待
     * @param job           作业
     * @throws Exception    异常
     */
    void handleWaitingJob(Job job) throws Exception;

    /**
     * 作业暂停
     * @param job           作业
     * @throws Exception    异常
     */
    void handlePauseJob(Job job) throws Exception;

    /**
     * 作业停止
     * @param job           作业
     * @throws Exception    异常
     */
    void handleStopJob(Job job) throws Exception;

    /**
     * 作业移除
     * @param job           作业
     * @throws Exception    异常
     */
    void handleRemoveJob(Job job) throws Exception;

    /**
     * 作业完成
     * @param job           作业
     * @throws Exception    异常
     */
    void handleCompleteJob(Job job) throws Exception;

    /**
     * 作业完成（包含错误的情况）
     * @param job           作业
     * @throws Exception    异常
     */
    void handleEndJob(Job job) throws Exception;
}
