package com.github.howjz.job;

import com.github.howjz.job.operator.debug.DebugProperties;
import com.github.howjz.job.operator.pool.PoolProperties;
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

    // debug配置
    private DebugProperties debug;

    // pool配置
    private PoolProperties pool;

    // 是否已经启动
    private boolean inited;

    public enum Mode {

        // local
        LOCAL,

        // redis
        REDIS,

        // DB
        DB
    }

}
