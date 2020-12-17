package com.github.howjz.job.handler;

import com.github.howjz.job.Job;

import java.util.List;

/**
 *  任务触发器
 *      任务主动触发
 * @author zhangjh
 * @date 2020/12/16 23:12
 */
public interface TaskTrigger {

    /**
     * 任务开始
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void handleStart(Job job, Job task) throws Exception;

    /**
     * 任务开始
     * @param job           作业
     * @param tasks         任务列表
     * @throws Exception    异常抛出
     */
    void handleStart(Job job, List<Job> tasks) throws Exception;

    /**
     * 任务暂停
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void handlePause(Job job, Job task) throws Exception;

    /**
     * 任务暂停
     * @param job           作业
     * @param tasks         任务列表
     * @throws Exception    异常抛出
     */
    void handlePause(Job job, List<Job> tasks) throws Exception;

    /**
     * 任务停止
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void handleStop(Job job, Job task) throws Exception;

    /**
     * 任务停止
     * @param job           作业
     * @param tasks         任务列表
     * @throws Exception    异常抛出
     */
    void handleStop(Job job, List<Job> tasks) throws Exception;

    /**
     * 任务移除
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void handleRemove(Job job, Job task) throws Exception;

    /**
     * 任务移除
     * @param job           作业
     * @param tasks         任务列表
     * @throws Exception    异常抛出
     */
    void handleRemove(Job job, List<Job> tasks) throws Exception;
}
