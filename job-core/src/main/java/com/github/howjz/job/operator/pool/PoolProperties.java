package com.github.howjz.job.operator.pool;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/13 14:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PoolProperties implements Serializable {
    private static final long serialVersionUID = -3573313315704325975L;

    // =========================== 配置信息 ===========================
    // 同时允许执行的任务数，默认为 5
    private int size = 5;

    // 执行器检测任务队列的休眠时间
    private long sleep = 3000;

    // =========================== 存放其他数据，不应随便修改 ===========================
    // 公有线程池（留空，在 PoolOperator 中创建）
    private PoolBean publicPool;

    // 私有线程池
    private Map<String, PoolBean> privatePools;

}
