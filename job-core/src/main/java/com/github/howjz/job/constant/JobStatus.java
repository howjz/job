package com.github.howjz.job.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangjh
 * @date 2020/12/4 15:37
 */
public enum JobStatus {
    /**
     * 作业状态
     */
    SKIP("跳过", -4, true),
    EXCEPTION("发生异常", -3, true),
    STOP("已停止", -2, true),
    PAUSE("暂停中", -1, false),
    WAIT("等待中", 0, false),
    READY("已就绪", 1,false),
    RUNNING("运行中", 2, false),
    COMPLETE("已完成", 3, true)
    ;
    private String key;
    private Integer value;
    private boolean end;
    JobStatus(String key, Integer value, boolean end){
        this.key = key;
        this.value = value;
        this.end = end;
    }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public Integer getValue(){return this.value;}
    public void setValue(Integer value){this.value = value;};
    public boolean isEnd() { return end; }
    public void setEnd(boolean end) { this.end = end; }
    public static JobStatus findByValue(Integer value){
        Map<Integer, JobStatus> collect = Arrays.stream(JobStatus.values())
                .collect(Collectors.toMap(JobStatus::getValue, t -> t));
        return collect.getOrDefault(value, null);
    }
}
