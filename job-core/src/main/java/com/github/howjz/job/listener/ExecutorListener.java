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
     * @param executorManager
     */
    void handleInit(GenericExecutorManager executorManager);

    /**
     * 执行中
     * @param executorManager
     */
    void handleRun(GenericExecutorManager executorManager) throws Exception;

    /**
     * 作业创建
     * @param job
     */
    void handleCreateJob(Job job) throws Exception;

    /**
     * 作业就绪
     * @param job
     * @throws Exception
     */
    void handleReadyJob(Job job) throws Exception;

    /**
     * 作业开始
     * @param job
     * @throws Exception
     */
    void handleStartJob(Job job) throws Exception;

    /**
     * 作业等待
     * @param job
     * @throws Exception
     */
    void handleWaitingJob(Job job) throws Exception;

    /**
     * 作业暂停
     * @param job
     * @throws Exception
     */
    void handlePauseJob(Job job) throws Exception;

    /**
     * 作业停止
     * @param job
     * @throws Exception
     */
    void handleStopJob(Job job) throws Exception;

    /**
     * 作业移除
     * @param job
     * @throws Exception
     */
    void handleRemoveJob(Job job) throws Exception;

    /**
     * 作业完成
     * @param job
     * @throws Exception
     */
    void handleCompleteJob(Job job) throws Exception;

    /**
     * 作业完成（包含错误的情况）
     * @param job
     * @throws Exception
     */
    void handleEndJob(Job job) throws Exception;
}
