package com.github.howjz.job.manager;

import com.github.howjz.job.ExecutorDataContext;
import com.github.howjz.job.listener.ExecutorListener;
import com.github.howjz.job.operator.ExecutorOperator;
import com.github.howjz.job.thread.ExecutorThread;
import com.github.howjz.job.trigger.ExecutorTrigger;
import java.util.Map;

/**
 *  执行器管理器
 *      负责各种操作、各种监听的转发
 * @author zhangjh
 * @date 2020/12/16 22:38
 */
public interface ExecutorManager extends ExecutorTrigger, ExecutorListener, JobManager, TaskManager {

    /**
     * 执行器管理器启动
     * @param executorManager   执行器管理器
     * @throws Exception        异常抛出
     */
    void init(ExecutorManager executorManager) throws Exception;


    /**
     * 获取执行器数据上下文对象
     * @return                  执行器数据上下文对象
     * @throws Exception        异常抛出
     */
    ExecutorDataContext getDataContext() throws Exception;

    /**
     * 获取所有的执行器操作器
     * @return                  所有的执行器操作器
     * @throws Exception        异常抛出
     */
    Map<String, ExecutorOperator<?>> getOperator() throws Exception;

    /**
     * 获取主要的执行器线程
     * @return                  主执行器线程
     * @throws Exception        异常抛出
     */
    ExecutorThread getPrimaryThread() throws Exception;

    /**
     * 获取所有的执行器线程
     * @return                  所有的执行器线程
     * @throws Exception        异常抛出
     */
    Map<String, ExecutorThread> getThread() throws Exception;

    /**
     * 停止
     */
    void destroy();

}
