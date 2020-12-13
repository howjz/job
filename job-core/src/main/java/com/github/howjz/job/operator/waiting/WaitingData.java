package com.github.howjz.job.operator.waiting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 * @author zhangjh
 * @date 2020/12/13 21:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WaitingData implements Serializable {
    private static final long serialVersionUID = -1941316193353125279L;

    // 作业锁
    @JsonIgnore
    private Map<String, Lock> jobLock = new ConcurrentHashMap<>();
}
