package com.github.howjz.job.operator.join;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/13 17:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JoinData implements Serializable {
    private static final long serialVersionUID = 5704711698986791604L;


    /**
     * 依赖等待map
     * 存入数据：
     *  需要等待的任务ID: 依赖的任务IDList
     * 监听器读取到任务ID时，确保依赖等待map中不包含，或者依赖的IDList为空，才可执行，否则重新入队
     */
    @JsonIgnore
    private Map<String, List<String>> waitMap = new ConcurrentHashMap<>();

    /**
     * 依赖触发map
     * 存入数据：
     *  需要触发的任务ID：等待的任务ID
     * 1、任务完成时，读取该map，如果存在，则读取所有等待的任务ID
     * 2、从 依赖等待map中，读取到 依赖的任务IDList，从中移除当前的任务ID
     * 3、当 依赖的任务IDList 长度为0时，直接移除掉key，此时 需要等待的任务ID 则可在 监听器 中正常执行了
     */
    @JsonIgnore
    private Map<String, List<String>> notifyMap = new ConcurrentHashMap<>();

}
