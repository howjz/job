package com.github.howjz.job.operator.error;

/**
 * @author zhangjh
 * @date 2020/12/12 20:14
 */
public enum ErrorPolicy {

    SKIP,           // 跳过其他未执行的任务
    KEEP,           // 继续执行其他未执行的任务

}
