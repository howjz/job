package com.github.howjz.job.handler;

import com.github.howjz.job.Executor;
import com.github.howjz.job.Job;
import com.github.howjz.job.operator.Operator;

import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/7 16:17
 */
public interface ExecutorHandler {

    /**
     * 获取执行器信息
     * @return  执行器信息
     */
    Executor getExecutor();

    /**
     * 获取执行器信息列表
     * @return  执行器信息列表
     */
    Map<String, Executor> getExecutors();

    /**
     * 执行任务
     * @throws Exception    异常
     */
    void executeTask() throws Exception;

    /**
     * 开始作业
     * @param job           作业
     * @throws Exception    异常
     */
    void startJob(Job job) throws Exception;

    /**
     * 等待作业执行完成
     * @param job           作业
     * @throws Exception    异常
     */
    void waitingJob(Job job) throws Exception;

    /**
     * 暂停作业
     * @param job           作业
     * @throws Exception    异常
     */
    void pauseJob(Job job) throws Exception;

    /**
     * 停止作业
     * @param job           作业
     * @throws Exception    异常
     */
    void stopJob(Job job) throws Exception;

    /**
     * 移除作业
     * @param job           作业
     * @throws Exception    异常
     */
    void removeJob(Job job) throws Exception;

    /**
     *
     * @param operate       目标操作
     * @param jobOrTask     作业或任务
     * @param operatorParam 操作参数
     * @param <T>           操作参数类型
     * @throws Exception    异常
     */
    <T> void handleOperate(Operator.Operate operate, Job jobOrTask, T operatorParam) throws Exception;

}
