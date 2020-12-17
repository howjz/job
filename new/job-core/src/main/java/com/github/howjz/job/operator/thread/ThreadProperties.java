package com.github.howjz.job.operator.thread;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author zhangjh
 * @date 2020/12/17 10:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ThreadProperties implements Serializable {
    private static final long serialVersionUID = 5591462605801647116L;

    // 额外执行器线程的总数
    private int total = 5;

    // 任务阈值，当任务达到阈值时，会启动额外执行器线程
    /*
    例如
        total = 5 & threshold = 100
        当任务总数 >= 100 时，启动 额外执行器-1
        当任务总数 >= 200 时，启动 额外执行器-2

        当任务总数 <  100 时，会移除相应的额外线程
     */
    private long threshold;

}
