package com.github.howjz.job.handler;

import com.github.howjz.job.Job;
import com.github.howjz.job.operator.execute.Executable;

import java.util.List;

/**
 * @author zhangjh
 * @date 2020/12/4 16:26
 */
public interface TaskHandler {

    /**
     * 创建任务
     * @param job           作业
     * @param task          任务
     * @return              作业
     * @throws Exception    异常
     */
    Job addTask(Job job, Job task) throws Exception;

    /**
     * 创建任务
     * @param job           作业
     * @param executable    执行函数
     * @return              作业
     * @throws Exception    异常
     */
    Job addTask(Job job, Executable executable) throws Exception;

    /**
     * 查找任务
     * @param jobId         作业ID
     * @param taskId        任务ID
     * @return              作业
     * @throws Exception    异常
     */
    Job findTask(String jobId, String taskId) throws Exception;

    /**
     * 开始任务
     * @param job           父作业
     * @param tasks         子任务列表
     * @throws Exception    异常
     */
    void startTasks(Job job, List<Job> tasks) throws Exception;

    /**
     * 开始任务
     * @param job           父作业
     * @param task          子任务
     * @throws Exception    异常
     */
    void startTask(Job job, Job task) throws Exception;

    /**
     * 暂停任务
     * @param job           父作业
     * @param task          子任务
     * @throws Exception    异常
     */
    void pauseTask(Job job, Job task) throws Exception;

    /**
     * 停止任务
     * @param job           父作业
     * @param task          子任务
     * @throws Exception    异常
     */
    void stopTask(Job job, Job task) throws Exception;

    /**
     * 移除任务
     * @param job           父作业
     * @param task          子任务
     * @throws Exception    异常
     */
    void removeTask(Job job, Job task) throws Exception;

    /**
     * 获取队列中的任务个数
     * @return              任务个数
     * @throws Exception    异常
     */
    int getQueueTaskCount() throws Exception;

    /**
     * 开始任务
     * @param jobAndTaskId  作业ID-任务ID
     * @throws Exception    异常
     */
    void offerTaskId(String jobAndTaskId) throws Exception;

    /**
     * 消费
     * @return              作业ID-任务ID
     * @throws Exception    异常
     */
    String consumeTaskId() throws Exception;
}
