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
     * @return
     * @throws Exception
     */
    Job addTask(Job job, Job task) throws Exception;

    /**
     * 创建任务
     * @param job           作业
     * @param executable    执行函数
     * @return
     * @throws Exception
     */
    Job addTask(Job job, Executable executable) throws Exception;

    /**
     * 查找任务
     * @param jobId         作业ID
     * @param taskId        任务ID
     * @return
     * @throws Exception
     */
    Job findTask(String jobId, String taskId) throws Exception;

    /**
     * 开始任务
     * @param job           父作业
     * @param tasks         子任务列表
     * @throws Exception
     */
    void startTasks(Job job, List<Job> tasks) throws Exception;

    /**
     * 开始任务
     * @param job           父作业
     * @param task          子任务
     * @throws Exception
     */
    void startTask(Job job, Job task) throws Exception;

    /**
     * 暂停任务
     * @param job           父作业
     * @param task          子任务
     * @throws Exception
     */
    void pauseTask(Job job, Job task) throws Exception;

    /**
     * 停止任务
     * @param job           父作业
     * @param task          子任务
     * @throws Exception
     */
    void stopTask(Job job, Job task) throws Exception;

    /**
     * 移除任务
     * @param job           父作业
     * @param task          子任务
     * @throws Exception
     */
    void removeTask(Job job, Job task) throws Exception;

    /**
     * 获取队列中的任务个数
     * @return
     * @throws Exception
     */
    int getQueueTaskCount() throws Exception;

    /**
     * 开始任务
     * @param jobAndTaskId
     * @throws Exception
     */
    void offerTaskId(String jobAndTaskId) throws Exception;

    /**
     * 消费
     * @return
     * @throws Exception
     */
    String consumeTaskId() throws Exception;
}
