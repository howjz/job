package com.github.howjz.job.manager;

import com.github.howjz.job.handler.TaskTrigger;
import com.github.howjz.job.listener.ExecutorListener;
import com.github.howjz.job.listener.JobListener;
import com.github.howjz.job.listener.TaskListener;
import com.github.howjz.job.operator.ExecutorOperator;
import com.github.howjz.job.runnable.ExecutorRunnable;
import com.github.howjz.job.trigger.ExecutorTrigger;
import com.github.howjz.job.trigger.JobTrigger;

import java.util.Map;

/**
 *  执行器管理器
 *      负责各种操作、各种监听的转发
 * @author zhangjh
 * @date 2020/12/16 22:38
 */
public interface ExecutorManager extends ExecutorTrigger, ExecutorListener, JobTrigger, JobListener, TaskTrigger, TaskListener {

    /**
     * 执行器管理器启动
     * @param executorManager   执行器管理器
     */
    void init(ExecutorManager executorManager);

    /**
     * 注册执行器执行线程
     * @param executorRunnable  执行器执行线程
     * @throws Exception        异常抛出
     */
    void registerRunnable(ExecutorRunnable executorRunnable) throws Exception;

    /**
     * 获取主要的执行器线程
     * @return                  主执行器线程
     * @throws Exception        异常抛出
     */
    ExecutorRunnable getPrimaryRunnable() throws Exception;

    /**
     * 获取所有的执行器线程
     * @return                  所有的执行器线程
     * @throws Exception        异常抛出
     */
    Map<String, ExecutorRunnable> getRunnable() throws Exception;

    /**
     * 获取所有的执行器操作器
     * @return                  所有的执行器操作器
     * @throws Exception        异常抛出
     */
    Map<String, ExecutorOperator<?>> getOperator() throws Exception;
}
