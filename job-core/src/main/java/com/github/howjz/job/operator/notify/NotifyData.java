package com.github.howjz.job.operator.notify;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/13 21:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NotifyData implements Serializable {
    private static final long serialVersionUID = 8718383066115080147L;

    /**
     * 触发状态MAP
     * 存入数据：
     *  jobId-taskId:状态
     * 支持状态：
     *  PAUSE，STOP，SKIP
     *  当 PAUSE 时重新放入任务队列
     *  当 STOP 时，不执行任务，修改任务状态为 STOP
     *  当 SKIP 时，不执行任务，修改任务状态为 SKIP
     */
    @JsonIgnore
    private Map<String, Integer> notifyMap = new ConcurrentHashMap<>();
}
