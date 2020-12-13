package com.github.howjz.job.operator.notify;

import com.github.howjz.job.Job;
import com.github.howjz.job.constant.JobStatus;

/**
 * @author zhangjh
 * @date 2020/12/13 23:31
 */
public class NotifyUtil {

    /**
     * 获取目标的状态触发ID
     *  notify-作业ID-任务ID
     * @param task
     * @return
     */
    public static String targetNotifyTaskId(Job task) {
        return String.format("%s-%s-%s", "notify", task.getParentId(), task.getId());
    }

}
