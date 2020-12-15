package com.github.howjz.job.operator.link;

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
 * @date 2020/12/13 17:54
 */
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = false)
public class LinkData extends OperatorData {
    private static final long serialVersionUID = -3758627036938915823L;

    /**
     * then等待map
     */
    private Map<String, List<String>> waitMap = new ConcurrentHashMap<>();

    /**
     * then触发map
     */
    private Map<String, List<String>> notifyMap = new ConcurrentHashMap<>();
}
