package com.github.howjz.job.operator.execute;

import com.github.howjz.job.Job;

import java.io.Serializable;

/**
 * @author zhangjh
 * @date 2020/12/11 10:15
 */
public interface Executable extends Serializable {

    /**
     * 子任务执行
     * @param job
     * @param task
     * @throws Exception
     */
    void execute(Job job, Job task) throws Exception;

}
