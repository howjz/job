package com.github.howjz.job.operator.then;

import com.github.howjz.job.Job;
import com.github.howjz.job.constant.JobType;
import com.github.howjz.job.util.DependUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangjh
 * @date 2020/12/12 18:35
 */
public class ThenUtil {

    public static Map<String, List<String>> getWaitMap(Job job, Map<String, ThenBean> thenBeanMap) {
        Map<String, List<String>> result = new LinkedHashMap<>(0);
        getWaitMap(job, thenBeanMap, result);
        return result;
    }

    /**
     * 任务11
     * 任务12
     * then1
     * then2
     * 任务21
     * then3
     * 任务22
     * 任务31
     * then4
     *      =>
     * then1	任务11、任务12
     * then2	任务11、任务12
     * then3	任务21
     * then4	任务22、任务31
     * @param job
     * @param result
     */
    private static void getWaitMap(Job job, Map<String, ThenBean> thenBeanMap, Map<String, List<String>> result) {
        ThenBean jobThenBean = thenBeanMap.get(job.getId());
        // 1、获取 已结束 的任务ID
        Set<String> endTaskIds = DependUtil.getEndTaskIds(job);
        // 2、拼接 join等待map
        for(Job task: job.getTasks()) {
            if (JobType.TASK_JOB == task.getType()) {
                getWaitMap(task, thenBeanMap, result);
            }
        }
        // 3、循环 thenId，拼接 then 依赖
        List<String> thenIds = jobThenBean.getThenIds();
        List<String> taskAndThenIds = jobThenBean.getTaskAndThenIds();
        for(int i = 0; i < thenIds.size(); i++) {
            String thenId = thenIds.get(i);
            int currentIndex = taskAndThenIds.indexOf("then-" + thenId);
            if (i == 0) {
                // 3.1、如果then为第一个，则寻找到 taskAndThenIds 中当前 thenId 下标，从 0 开始 到当前下标的任务ID即为then依赖
                List<String> firstThenIds = taskAndThenIds.subList(0, currentIndex)
                        .stream()
                        .filter(s -> !s.contains("then-"))
                        .filter(s -> !endTaskIds.contains(s))
                        .collect(Collectors.toList());
                result.put(thenId, firstThenIds);
            } else {
                // 3.2、其余的，则是依赖于 当前then 与 上一个then 之间的任务，如果任务为0，则一直往上找
                int currentThenIndex = i;
                int lastThenIndex = currentThenIndex - 1;
                List<String> spaceTasks = new ArrayList<>(0);
                while (spaceTasks.size() <= 0 && lastThenIndex >= 0) {
                    String lastThenId = thenIds.get(lastThenIndex);
                    if (taskAndThenIds.indexOf("then-" + lastThenId) != -1) {
                        spaceTasks = taskAndThenIds.subList(taskAndThenIds.indexOf("then-" + lastThenId), currentIndex)
                                .stream()
                                .filter(s -> !s.contains("then-"))
                                .filter(s -> !endTaskIds.contains(s))
                                .collect(Collectors.toList());
                        if (spaceTasks.size() == 0) {
                            if (result.containsKey(lastThenId)) {
                                spaceTasks = new ArrayList<>(result.get(lastThenId));
                            }
                        }
                    } else {
                        if (result.containsKey(lastThenId)) {
                            spaceTasks = new ArrayList<>(result.get(lastThenId));
                        }
                    }
                    currentThenIndex = lastThenIndex;
                    lastThenIndex = currentThenIndex - 1;
                }
                result.put(thenId, spaceTasks);
            }
        }
    }


    public static Map<String, List<String>> getNotifyMap(Job job, Map<String, ThenBean> thenBeanMap) {
        // 获取 waitMap
        Map<String, List<String>> waitMap = getWaitMap(job, thenBeanMap);
        return DependUtil.waitMapToNotifyMap(waitMap);
    }


}
