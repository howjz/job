package com.github.howjz.job.generic;

import com.github.howjz.job.ExecutorDataContext;
import com.github.howjz.job.Job;
import com.github.howjz.job.manager.ExecutorManager;
import com.github.howjz.job.operator.ExecutorOperator;
import com.github.howjz.job.thread.ExecutorThread;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/17 9:37
 */
@Slf4j
public class GenericExecutorManager implements ExecutorManager {

    private ExecutorDataContext dataContext;

    // 执行器
    private Map<String, ExecutorOperator<?>> operatorMap = new LinkedHashMap<>(0);

    public GenericExecutorManager(ExecutorDataContext dataContext) throws Exception {
        this.dataContext = dataContext;
        // 1、拼接操作器
        for(ExecutorOperator<?> operator: dataContext.getOperators()) {
            operatorMap.put(operator.operate().getKey(), operator);
        }
        // 2、启动调用当前的启动方法
        this.init(this);
    }

    @Override
    public void init(ExecutorManager executorManager) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).init(executorManager);
        }
    }

    @Override
    public ExecutorDataContext getDataContext() {
        return dataContext;
    }

    @Override
    public Map<String, ExecutorOperator<?>> getOperator() throws Exception {
        return this.operatorMap;
    }

    @Override
    public ExecutorThread getPrimaryThread() throws Exception {
        ExecutorThread result = null;
        for(String operate: this.operatorMap.keySet()) {
            result = this.operatorMap.get(operate).getPrimaryThread();
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public Map<String, ExecutorThread> getThread() throws Exception {
        Map<String, ExecutorThread> result = null;
        for(String operate: this.operatorMap.keySet()) {
            result = this.operatorMap.get(operate).getThread();
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public void destroy() {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).destroy();
        }
    }

    @Override
    public void onWait(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onWait(job, task);
        }
    }

    @Override
    public void onReady(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onReady(job, task);
        }
    }

    @Override
    public void onRunning(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onRunning(job, task);
        }
    }

    @Override
    public void onComplete(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onComplete(job, task);
        }
    }

    @Override
    public void onPause(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onPause(job, task);
        }
    }

    @Override
    public void onStop(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onStop(job, task);
        }
    }

    @Override
    public void onException(Job job, Job task, Exception exception) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onException(job, task, exception);
        }
    }

    @Override
    public void opSkip(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).opSkip(job, task);
        }
    }

    @Override
    public Job create(String id, String purpose) throws Exception {
        Job result = null;
        for(String operate: this.operatorMap.keySet()) {
            result = this.operatorMap.get(operate).create(id, purpose);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public Job find(String id, boolean detail) throws Exception {
        Job result = null;
        for(String operate: this.operatorMap.keySet()) {
            result = this.operatorMap.get(operate).find(id, detail);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public Job findOrCreate(String id) throws Exception {
        Job result = null;
        for(String operate: this.operatorMap.keySet()) {
            result = this.operatorMap.get(operate).findOrCreate(id);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public List<Job> list() throws Exception {
        List<Job> result = null;
        for(String operate: this.operatorMap.keySet()) {
            result = this.operatorMap.get(operate).list();
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public void sync(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).sync(job);
        }
    }

    @Override
    public void onReady(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onReady(job);
        }
    }

    @Override
    public void onWait(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onWait(job);
        }
    }

    @Override
    public void onRunning(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onRunning(job);
        }
    }

    @Override
    public void onComplete(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onComplete(job);
        }
    }

    @Override
    public void onPause(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onPause(job);
        }
    }

    @Override
    public void onStop(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onStop(job);
        }
    }

    @Override
    public void onException(Job job, Exception exception) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).onException(job, exception);
        }
    }

    @Override
    public void opSkip(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).opSkip(job);
        }
    }

    @Override
    public Job create(String id, String purpose, Job job) throws Exception {
        Job result = null;
        for(String operate: this.operatorMap.keySet()) {
            result = this.operatorMap.get(operate).create(id, purpose, job);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public Job find(String id) throws Exception {
        Job result = null;
        for(String operate: this.operatorMap.keySet()) {
            result = this.operatorMap.get(operate).find(id);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public List<Job> list(String jobId) throws Exception {
        List<Job> result = null;
        for(String operate: this.operatorMap.keySet()) {
            result = this.operatorMap.get(operate).list();
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public void sync(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).sync(job, task);
        }
    }

    @Override
    public Job pull() throws Exception {
        Job result = null;
        for(String operate: this.operatorMap.keySet()) {
            result = this.operatorMap.get(operate).pull();
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public void push(Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).push(task);
        }
    }

    @Override
    public Integer getCommits() throws Exception {
        Integer result = null;
        for(String operate: this.operatorMap.keySet()) {
            result = this.operatorMap.get(operate).getCommits();
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public void handleReady(ExecutorThread executorThread) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handleReady(executorThread);
        }
    }

    @Override
    public void handleExecuted(ExecutorThread executorThread) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handleExecuted(executorThread);
        }
    }

    @Override
    public void handleStart(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handleStart(job);
        }
    }

    @Override
    public void handlePause(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handlePause(job);
        }
    }

    @Override
    public void handleStop(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handleStop(job);
        }
    }

    @Override
    public void handleRemove(Job job) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handleRemove(job);
        }
    }

    @Override
    public void handleStart(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handleStart(job);
        }
    }

    @Override
    public void handleStart(Job job, List<Job> tasks) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handleStart(job, tasks);
        }
    }

    @Override
    public void handlePause(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handlePause(job, task);
        }
    }

    @Override
    public void handlePause(Job job, List<Job> tasks) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handlePause(job, tasks);
        }
    }

    @Override
    public void handleStop(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handleStop(job, task);
        }
    }

    @Override
    public void handleStop(Job job, List<Job> tasks) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handleStop(job, tasks);
        }
    }

    @Override
    public void handleRemove(Job job, Job task) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handleRemove(job, task);
        }
    }

    @Override
    public void handleRemove(Job job, List<Job> tasks) throws Exception {
        for(String operate: this.operatorMap.keySet()) {
            this.operatorMap.get(operate).handleRemove(job, tasks);
        }
    }
}
