package com.github.howjz.job.operator.then;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.howjz.job.operator.OperatorData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/13 17:53
 */
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = false)
public class ThenData extends OperatorData {
    private static final long serialVersionUID = 7658789327199668094L;

    /**
     * 记录then回调
     */
    @JsonIgnore
    private Map<String, ThenBean> thenBeanMap = new ConcurrentHashMap<>();

    @JsonIgnore
    private Map<String, Thenable> allThenMap = new ConcurrentHashMap<>();

    /**
     * then等待map
     */
    private Map<String, List<String>> waitMap = new ConcurrentHashMap<>();

    /**
     * then触发map
     */
    private Map<String, List<String>> notifyMap = new ConcurrentHashMap<>();

}
