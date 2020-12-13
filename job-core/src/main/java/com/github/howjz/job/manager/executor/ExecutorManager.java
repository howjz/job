package com.github.howjz.job.manager.executor;

import com.github.howjz.job.handler.ExecutorHandler;
import com.github.howjz.job.handler.JobHandler;
import com.github.howjz.job.handler.TaskHandler;
import com.github.howjz.job.listener.ExecutorListener;
import com.github.howjz.job.listener.StatusListener;

/**
 * @author zhangjh
 * @date 2020/12/13 1:21
 */
public interface ExecutorManager extends ExecutorHandler, JobHandler, TaskHandler, ExecutorListener, StatusListener {
}
