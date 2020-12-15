package com.github.howjz.job.operator.cross;

import com.github.howjz.job.operator.OperatorData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/13 20:06
 */
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = false)
public class CrossData extends OperatorData {
    private static final long serialVersionUID = 7649604717331800214L;

    // 记录作业 cross 类型
    private Map<String, CrossType> crossTypeMap = new ConcurrentHashMap<>();

}
