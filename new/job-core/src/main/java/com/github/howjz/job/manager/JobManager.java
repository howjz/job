package com.github.howjz.job.manager;

import com.github.howjz.job.Job;
import com.github.howjz.job.listener.JobListener;
import com.github.howjz.job.trigger.JobTrigger;

import java.util.List;

/**
 *  作业管理器
 *      负责作业创建、查找、主动触发、被动监听
 * @author zhangjh
 * @date 2020/12/16 23:17
 */
public interface JobManager extends JobTrigger, JobListener {

    /**
     * 作业创建
     * @param id            指定的ID
     * @param purpose       用途标识
     * @return              作业
     * @throws Exception    异常抛出
     */
    Job create(String id, String purpose) throws Exception;

    /**
     * 作业查找
     * @param id            作业ID
     * @param detail        是否返回详情信息
     * @return              作业
     * @throws Exception    异常抛出
     */
    Job find(String id, boolean detail) throws Exception;

    /**
     * 查找或者根据ID创建作业
     * @param id            指定的ID
     * @return              作业
     * @throws Exception    异常抛出
     */
    Job findOrCreate(String id) throws Exception;

    /**
     * 获取所有作业
     * @return              作业列表
     * @throws Exception    异常抛出
     */
    List<Job> list() throws Exception;

    /**
     * 作业信息同步
     * @param job           作业
     * @throws Exception    异常抛出
     */
    void sync(Job job) throws Exception;

}
