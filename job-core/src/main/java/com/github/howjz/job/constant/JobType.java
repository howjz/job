package com.github.howjz.job.constant;

import com.github.howjz.job.Job;
import com.github.howjz.job.operator.genericjob.GenericJob;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  作业类型
 * @author zhangjh
 * @date 2020/12/4 15:37
 */
public enum JobType {

    /**
     * 纯作业
     */
    JOB("纯作业", "job"),

    /**
     * 纯任务
     */
    TASK("纯任务", "task"),

    /**
     * 任务型作业
     */
    TASK_JOB("任务型作业", "task_job")
    ;
    private String key;
    private String value;
    JobType(String key, String value){
        this.key = key;
        this.value = value;
    }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getValue(){return this.value;}
    public void setValue(String value){this.value = value;};
    public static JobType findByValue(String value){
        Map<String, JobType> collect = Arrays.stream(JobType.values())
                .collect(Collectors.toMap(JobType::getValue, t -> t));
        return collect.getOrDefault(value, null);
    }

    public static JobType getJobType(Job jobExecutor) {
        if (StringUtils.isEmpty(jobExecutor.getParentId())) {
            // 1、父ID为空，肯定为纯作业
            return JobType.JOB;
        } else {
            if (jobExecutor.getTasks() == null || jobExecutor.getTasks().size() == 0 && !(jobExecutor instanceof GenericJob)) {
                // 2.1、父ID不为空，且不存在子任务，为 纯任务
                return JobType.TASK;
            } else {
                // 2.2、父ID不为空，且存在子任务，为 任务型作业
                return JobType.TASK_JOB;
            }
        }
    }
}
