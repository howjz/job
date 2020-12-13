package com.github.howjz.job.manager.job;

import com.github.howjz.job.handler.JobHandler;
import com.github.howjz.job.listener.StatusListener;

/**
 * @author zhangjh
 * @date 2020/12/13 1:23
 */
public interface JobManager extends JobHandler, StatusListener {
}
