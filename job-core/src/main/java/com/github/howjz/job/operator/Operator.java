package com.github.howjz.job.operator;

import com.github.howjz.job.Job;
import com.github.howjz.job.listener.ExecutorListener;
import com.github.howjz.job.listener.StatusListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *  操作器：基于监听器，用于实现不同的操作
 *  例如：
 *      then、error、join
 *  单独分离操作器的原因：
 *  1、有利于逻辑追加，作业和任务的总体结构不会变化，后续追加的逻辑写在操作器内部，而其余方法只是间接调用
 *  2、有利于跨程序保存，操作器单独维护操作参数，不会和原本的逻辑混在一起
 * @author zhangjh
 * @date 2020/12/11 10:05
 */
public interface Operator<T> extends StatusListener, ExecutorListener {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public class Operate implements Serializable {
        private static final long serialVersionUID = 1099672046795581966L;
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

    /**
     * 获取操作类型
     * @return
     */
    Operate operate();

    /**
     * 判断当前任务是否允许执行
     *
     * @param job
     * @param task
     * @return
     * @throws Exception
     */
    OperatorEnableFlag enable(Job job, Job task) throws Exception;

    /**
     * 执行操作
     * @param jobOrTask 作业或任务
     * @param operator  操作参数
     */
    void handleOperate(Job jobOrTask, T operator) throws Exception;

}
