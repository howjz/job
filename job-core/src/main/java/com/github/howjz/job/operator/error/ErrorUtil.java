package com.github.howjz.job.operator.error;

import com.github.howjz.job.Job;

/**
 * @author zhangjh
 * @date 2020/12/13 1:54
 */
public class ErrorUtil {

    /**
     * 获取当前的跳过ID
     *  作业ID-任务ID-重试次数
     * 和 目标的跳过ID 比较，当重试次数为0的时候（不重试），才能匹配得上，才可跳过
     * @param task      任务
     * @return          当前的跳过ID
     */
    public static String currentSkipTaskId(Job task) {
        return String.format("skip-%s-%s-%s", task.getParentId(), task.getId(), task.get_retry());
    }

    /**
     * 获取目标的跳过ID
     *  作业ID-任务ID-0
     * @param task      任务
     * @return          目标跳过ID
     */
    public static String targetSkipTaskId(Job task) {
        return String.format("skip-%s-%s-%s", task.getParentId(), task.getId(), 0);
    }

}
