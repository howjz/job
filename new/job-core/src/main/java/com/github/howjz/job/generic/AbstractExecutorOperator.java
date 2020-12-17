package com.github.howjz.job.generic;

import com.github.howjz.job.ExecutorDataContext;
import com.github.howjz.job.Job;
import com.github.howjz.job.constant.TaskEnableFlag;
import com.github.howjz.job.manager.ExecutorManager;
import com.github.howjz.job.operator.ExecutorOperator;
import com.github.howjz.job.thread.ExecutorThread;

import java.util.List;
import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/17 9:59
 */
public abstract class AbstractExecutorOperator<T> implements ExecutorOperator<T> {

    private ExecutorDataContext dataContext;

    public AbstractExecutorOperator(ExecutorDataContext dataContext) {
        this.dataContext = dataContext;
    }

    public TaskEnableFlag enable(Job job, Job task) throws Exception {
        return TaskEnableFlag.ENABLE;
    }

    public void handleOperate(Job jobOrTask, T operator) throws Exception {

    }

    public void init(ExecutorManager executorManager) throws Exception {

    }

    public ExecutorDataContext getDataContext() {
        return dataContext;
    }

    public Map<String, ExecutorOperator<?>> getOperator() throws Exception {
        return null;
    }

    public ExecutorThread getPrimaryThread() throws Exception {
        return null;
    }

    public Map<String, ExecutorThread> getThread() throws Exception {
        return null;
    }

    @Override
    public void destroy() {

    }

    public Job create(String id, String purpose) throws Exception {
        return null;
    }

    public Job find(String id, boolean detail) throws Exception {
        return null;
    }

    public Job findOrCreate(String id) throws Exception {
        return null;
    }

    public List<Job> list() throws Exception {
        return null;
    }

    public void sync(Job job) throws Exception {

    }

    public void onReady(Job job) throws Exception {

    }

    public void onWait(Job job) throws Exception {

    }

    public void onRunning(Job job) throws Exception {

    }

    public void onComplete(Job job) throws Exception {

    }

    public void onPause(Job job) throws Exception {

    }

    public void onStop(Job job) throws Exception {

    }

    public void onException(Job job, Exception exception) throws Exception {

    }

    public void opSkip(Job job) throws Exception {

    }

    public Job create(String id, String purpose, Job job) throws Exception {
        return null;
    }

    public Job find(String id) throws Exception {
        return null;
    }

    public List<Job> list(String jobId) throws Exception {
        return null;
    }

    public void sync(Job job, Job task) throws Exception {

    }

    public Job pull() throws Exception {
        return null;
    }

    public void push(Job task) throws Exception {

    }

    public Integer getCommits() throws Exception {
        return 0;
    }

    public void onWait(Job job, Job task) throws Exception {

    }

    public void onReady(Job job, Job task) throws Exception {

    }

    public void onRunning(Job job, Job task) throws Exception {

    }

    public void onComplete(Job job, Job task) throws Exception {

    }

    public void onPause(Job job, Job task) throws Exception {

    }

    public void onStop(Job job, Job task) throws Exception {

    }

    public void onException(Job job, Job task, Exception exception) throws Exception {

    }

    public void opSkip(Job job, Job task) throws Exception {

    }

    public void handleReady(ExecutorThread executorThread) throws Exception {

    }

    public void handleExecuted(ExecutorThread executorThread) throws Exception {

    }

    public void handleStart(Job job) throws Exception {

    }

    public void handlePause(Job job) throws Exception {

    }

    public void handleStop(Job job) throws Exception {

    }

    public void handleRemove(Job job) throws Exception {

    }

    public void handleStart(Job job, Job task) throws Exception {

    }

    public void handleStart(Job job, List<Job> tasks) throws Exception {

    }

    public void handlePause(Job job, Job task) throws Exception {

    }

    public void handlePause(Job job, List<Job> tasks) throws Exception {

    }

    public void handleStop(Job job, Job task) throws Exception {

    }

    public void handleStop(Job job, List<Job> tasks) throws Exception {

    }

    public void handleRemove(Job job, Job task) throws Exception {

    }

    public void handleRemove(Job job, List<Job> tasks) throws Exception {

    }
}
