package com.github.howjz.job.operator.then;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.howjz.job.Job;
import com.github.howjz.job.JobHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author zhangjh
 * @date 2020/10/28 9:15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ThenBean implements Serializable {
    private static final long serialVersionUID = -6560770881868908401L;

    // 经过格式化后的执行器ID
    @JsonIgnore
    private String formatExecutorId;

    // 中间计数，无实际意义
    private Long count;

    // 当前的thenId
    private String thenId;

    // 中间计数
    private List<String> taskAndThenIds;

    private List<String> thenIds;

    // 具体的回调函数
    private Map<String, Thenable> functions;

    public ThenBean(String executorId) {
        // 192.100.1.15:9090    -> 192-100-1-15_9090-
        this.formatExecutorId = executorId.replace(".", "-").replace(":", "_");
        this.count = 0L;
        this.thenId = String.format("%s-%s", this.formatExecutorId, this.count);
        this.taskAndThenIds = new ArrayList<>(0);
        this.thenIds = new ArrayList<>(0);
        this.functions = new LinkedHashMap<>(0);
    }

    /**
     * 设置then，注意此时then的顺序是错误的，例子如下：
     *  .addTask(任务11)
     *  .addTask(任务12)
     *  .then(then1)
     *  .addTask(任务21)
     *  .addTask(任务22)
     * 此时，任务21、任务22是再then1后添加的，却绑定上了 then1，因此需要修改，即
     *  查找 thenIds 中 当前thenId的位置 + 1，即为实际then
     *  如果不存在，则默认为 0，即无then
     * 具体逻辑在：reviseThen
     */
    public void handleAddTask(Job job, Job task) {
        this.taskAndThenIds.add(task.getId());
    }

    public void handleOperate(Job jobOrTask, Thenable operator) {
        if (operator == null) {
            throw new RuntimeException("then函数不能为空");
        }
        // 1、then累加，并记录历史
        this.count = JobHelper.IdWorker.nextId();
        this.thenId = String.format("%s-%s", this.formatExecutorId, this.count);
        // 2、绑定 thenId 和 相应回调函数
        this.functions.put(this.thenId, operator);
        // 3、添加thenId到中间计数
        this.thenIds.add(thenId);
        this.taskAndThenIds.add("then-" + thenId);
    }

    public void start(String thenId, Job job, Executor executor) throws Exception {
        // 当前无then，不执行
        if (this.count == 0) {
            return;
        }
        Thenable function = this.functions.get(thenId);
        if (function != null) {
            function.then(job);
        }
        this.functions.remove(thenId);
    }

    public void startAll(Job job, Executor executor) throws Exception {
        for(String thenId: this.functions.keySet()) {
            this.functions.get(thenId).then(job);
        }
        this.functions.clear();
    }

}
