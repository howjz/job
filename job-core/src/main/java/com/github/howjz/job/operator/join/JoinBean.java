package com.github.howjz.job.operator.join;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.howjz.job.Job;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  计算job依赖
 *  目前用于：
 *      1、串联任务中，任务型作业 对于 子任务 的依赖
 *      2、join语法的实现
 * @author zhangjh
 * @date 2020/11/11 10:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JoinBean implements Serializable {
    private static final long serialVersionUID = -6968720476267771976L;

    @JsonIgnore
    @ToString.Exclude
    private Job job;

    public JoinBean(Job job) {
        this.job = job;
    }

    // join 的计数     ———— 中间计算用，并无实际意义
    @JsonIgnore
    private Integer count = 0;

    // join 的缓存map  ———— 中间计算用，并无实际意义
    @JsonIgnore
    private Map<Integer, List<String>> countTaskIds = new HashMap<>(0);

    // join 的缓存map  ———— 中间计算用，并无实际意义
    @JsonIgnore
    private Map<String, Integer> taskIdCounts = new HashMap<>(0);

    public void handleOperate(Job jobOrTask, ObjectUtils.Null operator) {
        if (this.countTaskIds.containsKey(this.count)) {
            // 如果当前计数有包含任务ID则为有效计数，才允许继续累加
            this.count += 1;
        }
    }

    public void handleAddTask(Job job, Job task) {
        // 1、将 任务ID 和 计数 绑定一起
        this.taskIdCounts.put(task.getId(), this.count);
        // 2、将 计数 和 任务ID 绑定一起
        List<String> taskIds = new ArrayList<>(0);
        if (this.countTaskIds.containsKey(this.count)) {
            taskIds = this.countTaskIds.get(this.count);
        }
        taskIds.add(task.getId());
        this.countTaskIds.put(this.count, taskIds);
    }


}
