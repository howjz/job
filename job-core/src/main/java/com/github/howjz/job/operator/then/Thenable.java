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
     * @throws Exception
     */
    void then(Job job) throws Exception;

}
