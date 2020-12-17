package com.github.howjz.job.listener;

import com.github.howjz.job.Job;

/**
 *  任务监听
 *      任务被动监听
 * @author zhangjh
 * @date 2020/12/16 23:03
 */
public interface TaskListener {

    /**
     * 任务等待
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void onWait(Job job, Job task) throws Exception;

    /**
     * 任务就绪
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void onReady(Job job, Job task) throws Exception;

    /**
     * 任务执行
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void onRunning(Job job, Job task) throws Exception;

    /**
     * 任务完成
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void onComplete(Job job, Job task) throws Exception;

    /**
     * 任务暂停
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void onPause(Job job, Job task) throws Exception;

    /**
     * 任务停止
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void onStop(Job job, Job task) throws Exception;

    /**
     * 任务异常
     * @param job           作业
     * @param task          任务
     * @param exception     异常信息
     * @throws Exception    异常抛出
     */
    void onException(Job job, Job task, Exception exception) throws Exception;

    /**
     * 任务跳过
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void opSkip(Job job, Job task) throws Exception;
}




