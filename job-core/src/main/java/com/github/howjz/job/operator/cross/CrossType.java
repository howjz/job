package com.github.howjz.job.operator.cross;

/**
 * @author zhangjh
 * @date 2020/12/13 19:56
 */
public enum CrossType {

    // 不允许跨应用执行
    DISABLE,

    // 允许作业跨程序执行，即作业允许在不同程序中调用子任务，此时同时调用的子任务还是在同一程序内执行的
    JOB_CROSS,

    // 允许任务跨程序执行，即作业允许在不同程序中调用子任务，并且同时调用的子任务还允许在不同程序内执行（未实现，需要子任务实现具体的Java类，而不是采用匿名函数）
    TASK_CROSS

}
