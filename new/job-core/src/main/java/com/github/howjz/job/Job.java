package com.github.howjz.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.howjz.job.bean.Snapshot;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.constant.JobType;
import com.github.howjz.job.sugar.Expressible;
import com.github.howjz.job.sugar.Operable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *  作业详情信息对象：包含了作业的详情信息
 * @author zhangjh
 * @date 2019/3/4 15:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Job implements Serializable, Operable, Expressible {
    private static final long serialVersionUID = 3808845971230379160L;

    // ============================== 通用数据 ==============================
    private String id;
    // 判断当前是 作业 / 任务 / 任务型作业
    private JobType type;
    private String parentId;
    // 用途标记
    private String purpose;
    // 执行详情
    private Object param;
    private Object result;
    private JobStatus status;
    private Exception exception;
    // 初始重试次数
    private Integer retry;
    // 时间相关
    private Date createTime;
    private Date startTime;
    private Date endTime;
    // 发布者 —— 执行者
    private String issuer;
    private String executor;

    // 一些特殊设置的信息
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    // ============================== 作业数据 ==============================
    // 进度相关
    private Long complete;
    private Long end;
    private Long total;
    private Integer progress;
    // 执行快照
    private Snapshot snapshot;
    // 子任务
    @ToString.Exclude
    private List<Job> tasks;
    // 子作业异常信息
    @JsonIgnore
    @ToString.Exclude
    private Map<String, Exception> exceptions;

    // ============================== 中间数据 ==============================
    private Integer _retry;

    // 是否允许动态添加子任务
    private Boolean _dynamic;

    // 是否已添加，主要是对 任务 有效，判断任务是否已添加防止重复添加
    @JsonIgnore
    private Boolean _added;

    // 是否已等待，主要是对 作业 有效，判断作业是否已进入等待，如果进入等待，禁止了动态添加任务就会无法动态添加任务了
    @JsonIgnore
    private Boolean _waited;

    // 是否为重启任务 ———— 如果为true，说明该 作业/任务 为重启前的任务
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean _restart;

    /**
     * 创建一个作业
     */
    public Job(){
        this.setId(String.valueOf(ExecutorDataContext.IdWorker.nextId()));
        this.setType(JobType.JOB);
        this.setParentId(null);
        this.setStatus(JobStatus.WAIT);
        this.setExceptions(new HashMap<>(0));
        this.setCreateTime(new Date());
        this.tasks = new CopyOnWriteArrayList<>();
        this.setRetry(0);
        this.set_retry(0);
    }

}
