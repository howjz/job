package com.github.howjz.job.operator.error;

import com.github.howjz.job.Job;

import java.io.Serializable;

/**
 * @author zhangjh
 * @date 2020/12/11 10:23
 */
public interface Errorable extends Serializable {


    /**
     * 错误监听
     * @param task              当前的子任务
     * @param exception         捕获的异常信息
     * @return 返回值，确定是否执行其他任务（任务是串行执行的，已经执行过的无法停，只能停掉未执行或者已执行的）
     */
    ErrorPolicy error(Job task, Exception exception);
    
}
