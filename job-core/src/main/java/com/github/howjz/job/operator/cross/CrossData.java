package com.github.howjz.job.operator.cross;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/13 20:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CrossData implements Serializable {
    private static final long serialVersionUID = 7649604717331800214L;

    // 记录作业 cross 类型
    private Map<String, CrossType> crossTypeMap = new ConcurrentHashMap<>();

}
