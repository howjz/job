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
     * 获取执行器ID
     * @return
     */
    Executor getExecutor();

    /**
     * 获取所有执行器信息
     * @return
     */
    Map<String, Executor> getExecutors();

    /**
     * 执行任务
     */
    void executeTask() throws Exception;

    /**
     * 开始作业
     * @param job       作业
     * @throws Exception
     */
    void startJob(Job job) throws Exception;

    /**
     * 等待作业执行完成
     * @param job       作业
     * @throws Exception
     */
    void waitingJob(Job job) throws Exception;

    /**
     * 暂停作业
     * @param job       作业
     * @throws Exception
     */
    void pauseJob(Job job) throws Exception;

    /**
     * 停止作业
     * @param job       作业
     * @throws Exception
     */
    void stopJob(Job job) throws Exception;

    /**
     * 移除作业
     * @param job       作业
     * @throws Exception
     */
    void removeJob(Job job) throws Exception;

    /**
     * 执行操作
     * @param operate
     * @param jobOrTask
     * @param operatorParam
     */
    <T> void handleOperate(Operator.Operate operate, Job jobOrTask, T operatorParam) throws Exception;

}
