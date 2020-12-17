package com.github.howjz.job.bean;

import com.github.howjz.job.Job;

/**
 *  空作业，用于用于避免异常操作
 * @author zhangjh
 * @date 2020/11/27 10:14
 */
public class NullJob extends Job {

    private static final long serialVersionUID = 4632482370125126951L;
    private static NullJob instance;

    public NullJob() {
        this.setId(null);
        this.setType(null);
        this.setParentId(null);
        this.setStatus(null);
        this.setExceptions(null);
        this.setCreateTime(null);
        this.setTasks(null);
        this.setRetry(null);
        this.set_retry(null);
    }

    public static Job getInstance() {
        if (instance == null) {
            instance = new NullJob();
        }
        return instance;
    }

}
