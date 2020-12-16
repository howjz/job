package com.github.howjz.job.operator.link;

import com.github.howjz.job.Job;
import com.github.howjz.job.constant.JobType;
import com.github.howjz.job.operator.job.JobUtil;
import com.github.howjz.job.util.DependUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取link等待map、触发map
 *  原理：
 *      总作业
 *          子任务1
 *          子任务型作业1
 *              子任务型作业1任务1
 *              子任务型作业1任务2
 *  其中，子任务型作业1 的执行函数 依赖于 子任务型作业1任务1、子任务型作业1任务2
 *  子任务完成时，
 *      1、先查找 触发map 中的相应数据，如 子任务型作业1任务2 完成时，查找到 [子任务型作业1]
 *      2、循环查找到的 [子任务型作业1]，在 等待map中移除当前的任务，如 [子任务型作业1任务1, 子任务型作业1任务2] 中移除 子任务型作业1任务2
 *      3、依次，直达 [子任务型作业1任务1, 子任务型作业1任务2] 全部移除，此时 等待map 中 子任务型作业1 不存在任务等待，才可执行
 * @author zhangjh
 * @date 2020/12/12 0:22
 */
public class LinkUtil {

    private static void getWaitMap(Job job, Map<String, List<String>> result) {
        for(Job task: job.getTasks()) {
            if (JobType.TASK_JOB == task.getType()) {
                String taskId = task.getId();
                List<String> taskIds = job.getTasks().stream()
                        .filter(JobUtil::executable)
                        .map(Job::getId)
                        .collect(Collectors.toList());
                if (result.containsKey(taskId)) {
                    taskIds.addAll(result.get(taskId));
                }
                result.put(taskId, taskIds);
            }
            getWaitMap(task, result);
        }
    }

    /**
     * 获取link等待map
     *  该方法最终返回：
     *      {
     *          子任务型作业1: [子任务型作业1任务1, 子任务型作业1任务2]
     *      }
     * @param job   作业
     * @return  等待map
     */
    public static Map<String, List<String>> getWaitMap(Job job) {
        Map<String, List<String>> result = new HashMap<>(0);
        getWaitMap(job, result);
        return result;
    }


    /**
     * 获取link触发map
     *  该方法最终返回：
     *      {
     *          子任务型作业1任务1: [子任务型作业1],
     *          子任务型作业1任务2: [子任务型作业1]
     *      }
     * @param job   作业
     * @return      触发map
     */
    public static Map<String, List<String>> getNotifyMap(Job job) {
        return DependUtil.waitMapToNotifyMap(getWaitMap(job));
    }

}
