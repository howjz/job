package com.github.howjz.job.manager;

import com.github.howjz.job.Job;
import com.github.howjz.job.listener.TaskListener;
import com.github.howjz.job.trigger.TaskTrigger;

import java.util.List;

/**
 *  任务管理器
 *      负责任务创建、查找、主动触发、被动监听
 * @author zhangjh
 * @date 2020/12/16 23:17
 */
public interface TaskManager extends TaskTrigger, TaskListener {

    /**
     * 任务创建（添加任务到作业）
     * @param id            指定的任务ID
     * @param purpose       任务用途标记
     * @param job           父亲作业
     * @return              任务
     * @throws Exception    异常抛出
     */
    Job create(String id, String purpose, Job job) throws Exception;

    /**
     * 任务查找
     * @param id            任务ID
     * @return              任务
     * @throws Exception    异常抛出
     */
    Job find(String id) throws Exception;

    /**
     * 获取指定作业的所有任务
     * @param jobId         作业ID
     * @return              任务列表
     * @throws Exception    异常抛出
     */
    List<Job> list(String jobId) throws Exception;

    /**
     * 同步任务信息
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void sync(Job job, Job task) throws Exception;

    /**
     * 拉取任务
     * @return              任务
     * @throws Exception    异常抛出
     */
    Job pull() throws Exception;

    /**
     * 提交任务
     * @param task          任务
     * @throws Exception    异常抛出
     */
    void push(Job task) throws Exception;

    /**
     * 获取队列中的任务个数
     * @return              任务个数
     * @throws Exception    异常抛出
     */
    Integer getCommits() throws Exception;
}
