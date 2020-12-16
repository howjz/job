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
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常
     */
    void execute(Job job, Job task) throws Exception;

}
