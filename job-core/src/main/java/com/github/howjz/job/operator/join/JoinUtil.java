package com.github.howjz.job.operator.join;

import com.github.howjz.job.Job;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.constant.JobType;
import com.github.howjz.job.util.DependUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangjh
 * @date 2020/12/12 18:33
 */
public class JoinUtil {


    /**
     * 获取 join等待map
     * 数据结构如下：
     *  当前任务ID:需要等待的任务ID
     * 规则如下：
     *  joinCount 等于 0，无等待
     *  joinCount 大于 0，等待 joinCount - 1 的任务ID
     * @param job           作业
     * @param joinBeanMap   joinBean缓存map
     * @return              等待map
     */
    public static Map<String, List<String>> getWaitMap(Job job, Map<String, JoinBean> joinBeanMap) {
        Map<String, List<String>> result = new HashMap<>(0);
        // 拼接自身子任务依赖
        result.put(job.getId(), job.getTasks().stream().map(Job::getId).collect(Collectors.toList()));
        getWaitMap(job, result, joinBeanMap);
        return result;
    }

    private static void getWaitMap(Job job, Map<String, List<String>> result, Map<String, JoinBean> joinBeanMap) {
        JoinBean jobJoinBean = joinBeanMap.get(job.getId());
        if (jobJoinBean != null) {
            // 1、获取 已结束 的任务ID
            Set<String> endTaskIds = DependUtil.getEndTaskIds(job);
            // 2、拼接 join等待map
            for(Job task: job.getTasks()) {
                String taskId = task.getId();
                JobStatus taskStatus = task.getStatus();
                if (!taskStatus.isEnd()) {
                    // 2.1、当前join的任务，不能是 已结束 的
                    Integer taskJoinCount = jobJoinBean.getTaskIdCounts().get(taskId);
                    if (taskJoinCount != null && taskJoinCount > 0) {
                        // 2.1.1、获取当前 joinCount 需要等待的taskIds（即上一级的任务）
                        List<String> joinWaitIds = jobJoinBean.getCountTaskIds().get(taskJoinCount - 1);
                        // 2.1.2、筛选掉 已结束 的任务
                        joinWaitIds = joinWaitIds.stream()
                                .filter(i -> !endTaskIds.contains(i))
                                .collect(Collectors.toList());
                        // 2.1.3、绑定一起
                        result.put(taskId, joinWaitIds);
                    }
                }
                getTaskJobJoinWaitMap(task, result, joinBeanMap);
            }
        }
    }



    private static void getTaskJobJoinWaitMap(Job task, Map<String, List<String>> result, Map<String, JoinBean> joinBeanMap) {
        if (JobType.TASK_JOB == task.getType()) {
            getWaitMap(task, result, joinBeanMap);
            String taskId = task.getId();
            // 当前 任务型作业 必须等待子作业执行完成
            List<String> jobTaskWaitIds = new ArrayList<>(0);
            List<String> jobTaskWaitChildIds = task.getTasks().stream().map(Job::getId).collect(Collectors.toList());
            jobTaskWaitIds.addAll(jobTaskWaitChildIds);
            if (result.containsKey(taskId)) {
                jobTaskWaitIds.addAll(result.get(taskId));
            }
            result.put(taskId, jobTaskWaitIds);
            // 当前 任务型作业 包含的依赖，如果除了子作业外还有，那么则需要子作业添加上相关依赖
            List<String> jobTaskChildTaskWaitIds = new ArrayList<>(jobTaskWaitIds);
            jobTaskChildTaskWaitIds.removeAll(jobTaskWaitChildIds);
            if (jobTaskChildTaskWaitIds.size() > 0) {
                jobTaskWaitChildIds.forEach(jobTaskChildTaskId -> {
                    List<String> strings = new ArrayList<>(jobTaskChildTaskWaitIds);
                    if (result.containsKey(jobTaskChildTaskId)) {
                        strings.addAll(result.get(jobTaskChildTaskId));
                    }
                    result.put(jobTaskChildTaskId, strings);
                });
            }
        }
    }

    /**
     * 获取join触发map
     * 数据结构如下：
     *  当前任务ID:需要触发的任务ID（为 getJoinWaitMap 的key）
     * 直接从 waitMap 获取即可
     * @param job               作业
     * @param joinBeanMap       joinBean缓存map
     * @return                  触发mao
     */
    public static Map<String, List<String>> getNotifyMap(Job job, Map<String, JoinBean> joinBeanMap) {
        // 获取 waitMap
        Map<String, List<String>> joinWaitMap = getWaitMap(job, joinBeanMap);
        return DependUtil.waitMapToNotifyMap(joinWaitMap);
    }

}
