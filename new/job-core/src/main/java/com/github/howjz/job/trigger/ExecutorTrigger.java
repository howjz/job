package com.github.howjz.job.trigger;

import com.github.howjz.job.thread.ExecutorThread;

/**
 *  执行器触发器
 *      主动触发执行器各种操作
 * @author zhangjh
 * @date 2020/12/16 22:38
 */
public interface ExecutorTrigger extends JobTrigger, TaskTrigger {

    /**
     * 执行线程就绪
     * @param executorThread    执行器执行线程
     * @throws Exception        异常抛出
     */
    void handleReady(ExecutorThread executorThread) throws Exception;

    /**
     * 执行线程执行完成
     * @param executorThread    执行器执行线程
     * @throws Exception        异常抛出
     */
    void handleExecuted(ExecutorThread executorThread) throws Exception;

}
