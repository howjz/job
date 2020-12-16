package com.github.howjz.job.operator.then;

import com.github.howjz.job.Job;

import java.io.Serializable;

/**
 * @author zhangjh
 * @date 2020/12/11 10:22
 */
public interface Thenable extends Serializable {

    /**
     * then执行
     * @param job           做而已
     * @throws Exception    异常
     */
    void then(Job job) throws Exception;

}
