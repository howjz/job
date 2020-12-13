package com.github.howjz.job.operator.execute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/13 17:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExecuteData implements Serializable {

    private static final long serialVersionUID = -2149742867556656277L;

    // 存放执行回调
    @JsonIgnore
    private Map<String, Executable> executableMap = new ConcurrentHashMap<>();

    // 执行线程缓存
    @JsonIgnore
    private Map<String, ExecuteRunnable> executeRunnableMap = new ConcurrentHashMap<>();

}
