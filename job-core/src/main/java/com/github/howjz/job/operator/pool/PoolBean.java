package com.github.howjz.job.operator.pool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhangjh
 * @date 2020/12/13 13:08
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PoolBean implements Serializable {
    private static final long serialVersionUID = -7587385391627055052L;

    // 公用线程池
    public static final PoolBean PUBLIC = new PoolBean(null, 0);

    // 默认的自身线程池（3个）
    public static final PoolBean PRIVATE_DEFAULT = new PoolBean(5, 5);

    // 总线程个数
    private Integer totalSize;

    // 当前线程个数
    private Integer currentSize;

    // 定义了个数的 自身线程池
    public static PoolBean PRIVATE(int poolSize) {
        return new PoolBean(poolSize, poolSize);
    }


}
