package com.github.howjz.job.operator.execute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.howjz.job.operator.OperatorData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/13 17:55
 */
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = false)
public class ExecuteData extends OperatorData {

    private static final long serialVersionUID = -2149742867556656277L;

    // 存放执行回调
    @JsonIgnore
    private Map<String, Executable> executableMap = new ConcurrentHashMap<>();

    // 执行线程缓存
    @JsonIgnore
    private Map<String, ExecuteRunnable> executeRunnableMap = new ConcurrentHashMap<>();

}
