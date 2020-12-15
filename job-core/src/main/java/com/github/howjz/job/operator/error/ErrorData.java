package com.github.howjz.job.operator.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.howjz.job.operator.OperatorData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author zhangjh
 * @date 2020/12/13 17:58
 */
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorData extends OperatorData {
    private static final long serialVersionUID = -4894241807476526008L;

    // 记录总是 无限重试 作业
    private Set<String> alwaysRetrySet = new CopyOnWriteArraySet<>();

    // 记录错误回调
    @JsonIgnore
    private Map<String, Errorable> errorableMap = new ConcurrentHashMap<>(0);

}
