package com.github.howjz.job.operator.link;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/13 17:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LinkData implements Serializable {
    private static final long serialVersionUID = -3758627036938915823L;

    /**
     * then等待map
     */
    @JsonIgnore
    private Map<String, List<String>> linkWaitMap = new ConcurrentHashMap<>();

    /**
     * then触发map
     */
    @JsonIgnore
    private Map<String, List<String>> linkNotifyMap = new ConcurrentHashMap<>();
}
