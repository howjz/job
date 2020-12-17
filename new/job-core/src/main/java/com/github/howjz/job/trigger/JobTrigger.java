package com.github.howjz.job.trigger;

import com.github.howjz.job.Job;

/**
 *  作业触发器
 *      作业主动触发
 * @author zhangjh
 * @date 2020/12/16 23:12
 */
public interface JobTrigger {

    /**
     * 作业开始
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void handleStart(Job job) throws Exception;

    /**
     * 作业暂停
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void handlePause(Job job) throws Exception;

    /**
     * 作业停止
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void handleStop(Job job) throws Exception;

    /**
     * 作业移除
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void handleRemove(Job job) throws Exception;
}
