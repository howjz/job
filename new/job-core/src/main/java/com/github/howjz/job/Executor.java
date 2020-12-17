package com.github.howjz.job;

import com.github.howjz.job.operator.thread.ThreadProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *  执行器信息，注意只存放简单数据
 * @author zhangjh
 * @date 2020/12/13 16:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Executor implements Serializable {
    private static final long serialVersionUID = 5422863822558078270L;

    // 执行器ID
    private String executorId;

    /**
     * 执行器类型
     *  LOCAL   单机模式
     *  REDIS   REDIS模式
     *  DB      DB模式
     */
    private Executor.Mode mode;

    // 是否已经启动
    private boolean init;

    // 执行器线程配置信息
    private ThreadProperties thread;

    public enum Mode {

        // local
        LOCAL,

        // redis
        REDIS,

        // DB
        DB
    }

}
