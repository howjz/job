package com.github.howjz.job.manager.executor;

import com.github.howjz.job.Executor;
import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.constant.JobKey;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.constant.JobType;
import com.github.howjz.job.manager.GenericExecutorManager;
import com.github.howjz.job.manager.GenericJobManager;
import com.github.howjz.job.manager.GenericTaskManager;
import com.github.howjz.job.operator.Operator;
import com.github.howjz.job.operator.OperatorEnableFlag;
import com.github.howjz.job.operator.debug.DebugProperties;
import com.github.howjz.job.operator.execute.Executable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.BooleanUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 *  基础执行器管理器
 *  1、复负责全局任务监听、分发
 * @author zhangjh
 * @date 2020/12/11 15:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractExecutorManager extends Thread implements ExecutorManager {

    private boolean destroy;

    private JobDataContext dataContext;

    private GenericJobManager jobManager;

    private GenericTaskManager taskManager;

    private final String executorId;

    private Map<String, Operator<?>> operators;

    private final DebugProperties debugProperties;

    private final Map<String, Executor> executors;

    // 备用的执行器
    private final ExecutorService spareExecutorPool;

    public AbstractExecutorManager(JobDataContext dataContext, GenericJobManager jobManager, GenericTaskManager taskManager) {
        this.jobManager = jobManager;
        this.taskManager = taskManager;
        this.destroy = false;
        this.dataContext = dataContext;
        this.executorId = dataContext.getExecutor().getExecutorId();
        this.debugProperties = dataContext.getExecutor().getDebug();
        this.executors = dataContext.getExecutors();
        this.spareExecutorPool = dataContext.getSpareExecutorPool();
        // 设置操作器
        this.operators = new LinkedHashMap<>(dataContext.getOperators().size());
        for(Operator<?> operator: dataContext.getOperators()) {
            Operator.Operate operate = operator.operate();
            if (operate == null) {
                throw new RuntimeException("自定义的操作器操作类型不能为空：" + operator);
            }
            this.operators.put(operate.getKey(), operator);
        }
    }

    @Override
    public void run() {
        while (!destroy) {
            try {
                // 1、执行run
                this.handleRun((GenericExecutorManager) this);
                // 2、执行任务
                this.executeTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
        this.destroy = true;
        this.dataContext.getExecutorPool().shutdown();
        this.dataContext.getSpareExecutorPool().shutdown();
    }

    @Override
    public Executor getExecutor() {
        return this.dataContext.getExecutor();
    }

    @Override
    public Map<String, Executor> getExecutors() {
        return this.dataContext.getExecutors();
    }

    @Override
    public <T> void handleOperate(Operator.Operate operate, Job jobOrTask, T operatorParam) throws Exception {
        if (operate == null) {
            throw new RuntimeException("操作类型不能为空");
        }
        Operator<T> operator = (Operator<T>) this.operators.get(operate.getKey());
        if (operator == null) {
            throw new RuntimeException("当前操作不支持：" + operate);
        }
        operator.handleOperate(jobOrTask, operatorParam);
    }

    @Override
    public void handleInit(GenericExecutorManager executorManager) {
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handleInit(executorManager);
        }
    }

    @Override
    public void handleRun(GenericExecutorManager executorManager) throws Exception {
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handleRun(executorManager);
        }
    }

    @Override
    public void handleCreateJob(Job job) throws Exception {
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handleCreateJob(job);
        }
    }

    @Override
    public void handleReadyJob(Job job) throws Exception {
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handleReadyJob(job);
        }
    }

    @Override
    public void handleStartJob(Job job) throws Exception {
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handleStartJob(job);
        }
    }

    @Override
    public void handleWaitingJob(Job job) throws Exception {
        job.setStatus(JobStatus.WAIT);
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handleWaitingJob(job);
        }
    }

    @Override
    public void handlePauseJob(Job job) throws Exception {
        job.setStatus(JobStatus.PAUSE);
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handlePauseJob(job);
        }
    }

    @Override
    public void handleStopJob(Job job) throws Exception {
        job.setStatus(JobStatus.STOP);
        job.setEndTime(new Date());
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handleStopJob(job);
        }
    }

    @Override
    public void handleRemoveJob(Job job) throws Exception {
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handleRemoveJob(job);
        }
    }


    @Override
    public void handleCompleteJob(Job job) throws Exception {
        job.setStatus(JobStatus.COMPLETE);
        job.setEndTime(new Date());
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handleCompleteJob(job);
        }
    }

    @Override
    public void handleEndJob(Job job) throws Exception {
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handleEndJob(job);
        }
    }

    @Override
    public Job createJob(String jobId, Executable executable) throws Exception {
        Job job = this.jobManager.createJob(jobId, executable);
        this.handleCreateJob(job);
        return job;
    }

    @Override
    public Job findJob(String jobId, boolean detail) throws Exception {
        return this.jobManager.findJob(jobId, detail);
    }

    @Override
    public Job findOrCreateJob(String jobId, Executable executable) throws Exception {
        return this.jobManager.findOrCreateJob(jobId, executable);
    }

    @Override
    public List<Job> getJobs() {
        return this.jobManager.getJobs();
    }

    @Override
    public List<Job> getTasks(String jobId) throws Exception {
        return this.jobManager.getTasks(jobId);
    }

    @Override
    public void startJob(Job job) throws Exception {
        // 1、判断当前作业状态
        if (job == null) {
            throw new RuntimeException("作业不能为空");
        }
        // 2、设置基础参数
        job.setIssuer(this.executorId);
        job.set_waited(true);
        job.set_restart(null);
        // 3、开始子任务
        if (job.getTasks() != null && job.getTasks().size() > 0) {
            // 3.1、设置作业任务总数及其他属性
            job.setStatus(JobStatus.WAIT);
            job.setComplete(0L);
            job.setEnd(0L);
            job.setTotal((long) job.getTasks().size());
            job.setProgress(0);
            job.setEndTime(null);
            // 3.2、同步任务详情
            CompletableFuture.allOf(CompletableFuture.runAsync(() -> {
                try {
                    this.syncJob(job);
                    this.syncTasks(job, job.getTasks());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            })).get();
            // 3.3、过滤有效任务
            List<Job> effectiveTasks = job.getTasks()
                    .stream()
                    .filter(task -> {
                        JobStatus taskStatus = task.getStatus();
                        if (JobType.TASK_JOB == task.getType()) {
                            try {
                                // 3.3.1、任务型作业 内部的子任务也需要开始
                                this.startJob(task);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // 3.3.2、子任务不能是已经 待执行、执行中 或者 执行过的任务
                        return !taskStatus.isEnd() &&
                                taskStatus != JobStatus.RUNNING &&
                                (task.get_added() == null || BooleanUtils.isFalse(task.get_added()));
                    })
                    .collect(Collectors.toList());
            // 3.4、异步提交任务
            spareExecutorPool.submit(() -> {
                try {
                    JobHelper.manager.startTasks(job, effectiveTasks);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        // 4、触发开始
        if (JobType.JOB == job.getType() || JobType.TASK_JOB == job.getType()) {
            if (JobStatus.COMPLETE == job.getStatus() && job.getTasks().size() == 0) {
                this.handleCompleteJob(job);
            } else {
                this.handleStartJob(job);
            }
        }
    }

    @Override
    public void waitingJob(Job job) throws Exception {
        this.handleWaitingJob(job);
    }

    @Override
    public void pauseJob(Job job) throws Exception {
        this.handlePauseJob(job);
    }

    @Override
    public void stopJob(Job job) throws Exception {
        this.handleStopJob(job);
    }

    @Override
    public void removeJob(Job job) throws Exception {
        this.handleRemoveJob(job);
    }

    @Override
    public void syncJob(Job job) throws Exception {
        this.jobManager.syncJob(job);
    }

    @Override
    public void syncTask(Job job, Job task) throws Exception {
        this.jobManager.syncTask(job, task);
    }

    @Override
    public void syncTasks(Job job, List<Job> tasks) throws Exception {
        this.jobManager.syncTasks(job, tasks);
    }

    @Override
    public Job addTask(Job job, Job task) throws Exception {
        task = this.taskManager.addTask(job, task);
        this.handleAddTask(job, task);
        return task;
    }

    @Override
    public Job addTask(Job job, Executable executable) throws Exception {
        Job task = this.taskManager.addTask(job, executable);
        this.handleAddTask(job, task);
        return task;
    }

    @Override
    public Job findTask(String jobId, String taskId) throws Exception {
        return this.taskManager.findTask(jobId, taskId);
    }

    @Override
    public void startTasks(Job job, List<Job> tasks) throws Exception {
        this.taskManager.startTasks(job, tasks);
    }

    @Override
    public void startTask(Job job, Job task) throws Exception {
        this.taskManager.startTask(job, task);
    }

    @Override
    public void pauseTask(Job job, Job task) throws Exception {
        this.taskManager.pauseTask(job, task);
    }

    @Override
    public void stopTask(Job job, Job task) throws Exception {
        this.taskManager.stopTask(job, task);
    }

    @Override
    public void removeTask(Job job, Job task) throws Exception {
        this.taskManager.removeTask(job, task);
    }

    @Override
    public int getQueueTaskCount() throws Exception {
        return this.taskManager.getQueueTaskCount();
    }

    @Override
    public void offerTaskId(String jobAndTaskId) throws Exception {
        this.taskManager.offerTaskId(jobAndTaskId);
    }

    @Override
    public String consumeTaskId() throws Exception {
        return this.taskManager.consumeTaskId();
    }

    @Override
    public void handleAddTask(Job job, Job task) throws Exception {
        // 1、执行作业管理器
        this.taskManager.handleAddTask(job, task);
        this.jobManager.handleAddTask(job, task);
        for(String operate: this.operators.keySet()) {
            this.operators.get(operate).handleAddTask(job, task);
        }
    }

    public OperatorEnableFlag enable(Job job, Job task) throws Exception {
        OperatorEnableFlag flag = OperatorEnableFlag.KEEP;
        for(String operate: this.operators.keySet()) {
            flag = this.operators.get(operate).enable(job, task);
            if (flag == OperatorEnableFlag.DISABLE || flag == OperatorEnableFlag.RESET) {
                break;
            }
        }
        return flag;
    }

    @Override
    public void handleWaitTask(Job job, Job task) throws Exception {
        this.handleTask(job, task, JobStatus.WAIT);
    }

    @Override
    public void handleReadyTask(Job job, Job task) throws Exception {
        this.handleTask(job, task, JobStatus.READY);
    }

    @Override
    public void handleRunningTask(Job job, Job task) throws Exception {
        this.handleTask(job, task, JobStatus.RUNNING);
    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {
        this.handleTask(job, task, JobStatus.COMPLETE);
    }

    @Override
    public void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {
        task.setException(exception);
        this.handleTask(job, task, JobStatus.EXCEPTION);
    }

    @Override
    public void handleSkipTask(Job job, Job task) throws Exception {
        this.handleTask(job, task, JobStatus.SKIP);
    }

    @Override
    public void handlePauseTask(Job job, Job task) throws Exception {
        this.handleTask(job, task, JobStatus.PAUSE);
    }

    @Override
    public void handleStopTask(Job job, Job task) throws Exception {
        this.handleTask(job, task, JobStatus.STOP);
    }

    @Override
    public void handleTask(Job job, Job task, JobStatus status) throws Exception {
        // 1、根据状态调用任务管理器方法
        switch (status) {
            case WAIT:
                this.taskManager.handleWaitTask(job, task);
                this.jobManager.handleWaitTask(job, task);
                break;
            case READY:
                this.taskManager.handleReadyTask(job, task);
                this.jobManager.handleReadyTask(job, task);
                break;
            case RUNNING:
                this.taskManager.handleRunningTask(job, task);
                this.jobManager.handleRunningTask(job, task);
                break;
            case COMPLETE:
                this.taskManager.handleCompleteTask(job, task);
                this.jobManager.handleCompleteTask(job, task);
                break;
            case EXCEPTION:
                this.taskManager.handleExceptionTask(job, task, task.getException());
                this.jobManager.handleExceptionTask(job, task, task.getException());
                break;
            case SKIP:
                this.taskManager.handleSkipTask(job, task);
                this.jobManager.handleSkipTask(job, task);
                break;
            case PAUSE:
                this.taskManager.handlePauseTask(job, task);
                this.jobManager.handlePauseTask(job, task);
                break;
            case STOP:
                this.taskManager.handleStopTask(job, task);
                this.jobManager.handleStopTask(job, task);
                break;
        }
        // 2、同步任务状态
        this.jobManager.syncTask(job, task);
        // 3、根据状态调用操作器
        this.taskManager.handleTask(job, task, status);
        this.jobManager.handleTask(job, task, status);
        this.handleTaskToOperator(job, task, status);
        // 4、调用查看是否已完成
        if (job.isEnd()) {
            this.handleEndJob(job);
        }
        if (job.isComplete()) {
            this.handleCompleteJob(job);
        }
    }

    private void handleTaskToOperator(Job job, Job task, JobStatus status) throws Exception {
        for(String operate: this.operators.keySet()) {
            Operator<?> operator = this.operators.get(operate);
            switch (status) {
                case WAIT:
                    operator.handleWaitTask(job, task);
                    break;
                case READY:
                    operator.handleReadyTask(job, task);
                    break;
                case RUNNING:
                    operator.handleRunningTask(job, task);
                    break;
                case COMPLETE:
                    operator.handleCompleteTask(job, task);
                    break;
                case EXCEPTION:
                    operator.handleExceptionTask(job, task, task.getException());
                    break;
                case SKIP:
                    operator.handleSkipTask(job, task);
                    break;
                case PAUSE:
                    operator.handlePauseTask(job, task);
                    break;
                case STOP:
                    operator.handleStopTask(job, task);
                    break;
            }
            operator.handleTask(job, task, status);
        }
    }
}
