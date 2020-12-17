package com.github.howjz.job.trigger;

import com.github.howjz.job.handler.TaskTrigger;
import com.github.howjz.job.runnable.ExecutorRunnable;

/**
 *  执行器触发器
 *      主动触发执行器各种操作
 * @author zhangjh
 * @date 2020/12/16 22:38
 */
public interface ExecutorTrigger extends JobTrigger, TaskTrigger {

    /**
     * 执行线程就绪
     * @param executorRunnable  执行器执行线程
     * @throws Exception        异常抛出
     */
    void handleReady(ExecutorRunnable executorRunnable) throws Exception;

    /**
     * 执行线程执行完成
     * @param executorRunnable  执行器执行线程
     * @throws Exception        异常抛出
     */
    void handleExecuted(ExecutorRunnable executorRunnable) throws Exception;

}
