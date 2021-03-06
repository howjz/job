package com.github.howjz.job.util;

import com.github.howjz.job.Job;
import com.github.howjz.job.constant.JobStatus;

import java.util.*;

/**
 * @author zhangjh
 * @date 2020/11/13 16:00
 */
public class DependUtil {

    /**
     * 触发移除等待
     * @param task      当前任务
     * @param waitMap   等待map
     * @param notifyMap 触发map
     * @return          当前需要触发的等待ID
     */
    public static synchronized List<String> notifyRemoveWait(Job task, Map<String, List<String>> waitMap, Map<String, List<String>> notifyMap) {
        String taskId = task.getId();
        List<String> waitTaskIds = notifyMap.remove(taskId);
        if (waitTaskIds != null && waitTaskIds.size() > 0) {
            // 1、获取当前任务需要触发join的任务
            for(String waitTaskId: waitTaskIds) {
                // 2、从触发join的任务移除当前任务ID的依赖
                if (waitMap.containsKey(waitTaskId)) {
                    List<String> waitNotifyTaskIds = waitMap.get(waitTaskId);
                    waitNotifyTaskIds.remove(taskId);
                    if (waitNotifyTaskIds.size() > 0) {
                        waitMap.put(waitTaskId, waitNotifyTaskIds);
                    } else {
                        waitMap.remove(waitTaskId);
                    }
                }
            }
        }
        return waitTaskIds;
    }

    /**
     * waitMap 转换为 notifyMap
     * @param waitMap   等待map
     * @return          触发map
     */
    public static Map<String, List<String>> waitMapToNotifyMap(Map<String, List<String>> waitMap) {
        return waitMapToNotifyMap(waitMap, new HashMap<>(0));
    }

    private static Map<String, List<String>> waitMapToNotifyMap(Map<String, List<String>> waitMap, Map<String, List<String>> result) {
        for(String waitId: waitMap.keySet()) {
            List<String> notifyIds = waitMap.get(waitId);
            notifyIds.forEach(notifyId -> {
                List<String> ids = new ArrayList<>();
                if (result.containsKey(notifyId)) {
                    ids.addAll(result.get(notifyId));
                }
                ids.add(waitId);
                result.put(notifyId, ids);
            });
        }
        return result;
    }

    /**
     * 获取结束的任务ID
     * @param job       作业
     * @return          结束的任务ID列表
     */
    public static Set<String> getEndTaskIds(Job job) {
        Set<String> endTaskIds = new HashSet<>(0);
        // 2、循环任务列表，筛选出已结束的任务ID
        for(Job task: job.getTasks()) {
            String taskId = task.getId();
            JobStatus taskStatus = task.getStatus();
            if (taskStatus.isEnd()) {
                endTaskIds.add(taskId);
            }
        }
        return endTaskIds;
    }


    /**
     * 合并
     * @param currentMap    当前map
     * @param targetMap     目标map
     */
    public synchronized static void mergeDependMap(Map<String, List<String>> currentMap, Map<String, List<String>> targetMap) {
        for(String id: targetMap.keySet()) {
            List<String> strings = targetMap.get(id);
            if (currentMap.containsKey(id)) {
                strings.addAll(currentMap.get(id));
                strings = new ArrayList<>(new HashSet<>(strings));
            }
            currentMap.put(id, strings);
        }
    }

}
