package com.github.howjz.job.operator;

import com.github.howjz.job.Job;
import com.github.howjz.job.bean.Operate;
import com.github.howjz.job.constant.TaskEnableFlag;
import com.github.howjz.job.manager.ExecutorManager;

/**
 *  操作器
 *      继承于 ExecutorManager
 *      实际上是执行器各种逻辑的具体实现
 *      ExecutorManager 只充当转发
 * @author zhangjh
 * @date 2020/12/16 22:47
 */
public interface ExecutorOperator<T> extends ExecutorManager {

    /**
     * 获取操作类型
     * @return      操作类型
     */
    Operate operate();

    /**
     * 判断当前任务是否允许执行
     *
     * @param job           作业
     * @param task          任务
     * @return              执行标志
     * @throws Exception    异常抛出
     */
    TaskEnableFlag enable(Job job, Job task) throws Exception;

    /**
     * 执行操作
     * @param jobOrTask     作业或任务
     * @param operator      操作参数
     * @throws Exception    异常抛出
     */
    void handleOperate(Job jobOrTask, T operator) throws Exception;

}
