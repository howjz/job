package com.github.howjz.job.bean;

import com.github.howjz.job.Job;

/**
 *  空作业，用于用于避免异常操作
 * @author zhangjh
 * @date 2020/11/27 10:14
 */
public class NullJob extends Job {

    private static NullJob instance;

    public static Job getInstance() {
        if (instance == null) {
            instance = new NullJob();
        }
        return instance;
    }

}
