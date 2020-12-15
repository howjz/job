package com.github.howjz.job.operator.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.howjz.job.Job;
import com.github.howjz.job.operator.OperatorData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/15 22:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JobData extends OperatorData {
    private static final long serialVersionUID = -3099399490663957238L;

    // 作业详情
    @JsonIgnore
    private Map<String, Job> jobMap = new ConcurrentHashMap<>();

    // 作业任务关联
    private Map<String, Set<String>> jobTaskRelationMap = new ConcurrentHashMap<>();

    // 任务详情
    @JsonIgnore
    private Map<String, Job> taskMap = new ConcurrentHashMap<>();

}
