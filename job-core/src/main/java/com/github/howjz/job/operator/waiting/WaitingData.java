package com.github.howjz.job.operator.waiting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.howjz.job.operator.OperatorData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 * @author zhangjh
 * @date 2020/12/13 21:51
 */
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = false)
public class WaitingData extends OperatorData {
    private static final long serialVersionUID = -1941316193353125279L;

    // 作业锁
    @JsonIgnore
    private Map<String, Lock> jobLock = new ConcurrentHashMap<>();
}
