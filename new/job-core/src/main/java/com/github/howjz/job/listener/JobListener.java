package com.github.howjz.job.listener;

import com.github.howjz.job.Job;

/**
 *  作业监听器
 *      作业被动监听
 * @author zhangjh
 * @date 2020/12/16 23:03
 */
public interface JobListener {

    /**
     * 作业就绪
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void onReady(Job job) throws Exception;

    /**
     * 作业等待
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void onWait(Job job) throws Exception;

    /**
     * 作业执行
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void onRunning(Job job) throws Exception;

    /**
     * 作业完成
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void onComplete(Job job) throws Exception;

    /**
     * 作业暂停
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void onPause(Job job) throws Exception;

    /**
     * 作业停止
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void onStop(Job job) throws Exception;

    /**
     * 作业异常
     * @param job           作业
     * @param exception     异常信息
     * @throws Exception    异常抛出
     */
    void onException(Job job, Exception exception) throws Exception;

    /**
     * 作业跳过
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void opSkip(Job job) throws Exception;
}




