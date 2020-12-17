package com.github.howjz.job.constant;

/**
 *  任务是否允许执行标记
 * @author zhangjh
 * @date 2020/12/7 17:41
 */
public enum TaskEnableFlag {

    // 继续
    KEEP,

    // 不允许
    DISABLE,

    // 重新放回
    RESET
    ;
}
