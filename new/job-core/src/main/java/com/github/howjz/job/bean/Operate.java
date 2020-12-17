package com.github.howjz.job.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *  操作类型
 * @author zhangjh
 * @date 2020/12/16 22:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Operate implements Serializable {
    private static final long serialVersionUID = 1099672046795581966L;

    public static final Operate JOB = new Operate("JOB");
    public static final Operate NOTIFY = new Operate("NOTIFY");
    public static final Operate RESTART = new Operate("RESTART");
    public static final Operate WAITING = new Operate("WAITING");
    public static final Operate DEBUG = new Operate("DEBUG");
    public static final Operate CONFIG = new Operate("CONFIG");
    public static final Operate POOL = new Operate("POOL");
    public static final Operate ERROR = new Operate("ERROR");
    public static final Operate RETRY = new Operate("RETRY");
    public static final Operate ALWAYS_RETRY = new Operate("ALWAYS_RETRY");
    public static final Operate EXECUTE = new Operate("EXECUTE");
    // link无实际方法体现，只是 任务型作业 的具体实现
    public static final Operate LINK = new Operate("LINK");
    public static final Operate THEN = new Operate("THEN");
    public static final Operate ALL_THEN = new Operate("ALL_THEN");
    public static final Operate GENERIC_JOB = new Operate("GENERIC_JOB");
    public static final Operate JOIN = new Operate("JOIN");
    public static final Operate CROSS = new Operate("CROSS");

    private String key;

}
