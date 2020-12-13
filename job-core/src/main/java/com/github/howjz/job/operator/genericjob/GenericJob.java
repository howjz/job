package com.github.howjz.job.operator.genericjob;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.listener.ExecutorListener;
import com.github.howjz.job.listener.StatusListener;
import com.github.howjz.job.manager.GenericExecutorManager;
import com.github.howjz.job.operator.execute.Executable;
import com.github.howjz.job.operator.then.Thenable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 *  通用作业对象
 *      T   作业参数类型
 * @author zhangjh
 * @date 2020/12/12 13:31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class GenericJob<T> extends Job implements Executable, Thenable, ExecutorListener, StatusListener {

    private T jobParam;

    protected GenericJob(T jobParam) throws Exception {
        this.jobParam = jobParam;
        // 这里需要手动触发 作业创建
        JobHelper.manager.handleCreateJob(this);
    }

    /**
     * 生成任务参数
     * @param tj    作业参数
     * @return      任务参数列表
     */
    public abstract List<String> generateTaskParams(T tj);

    /**
     * 存在返回结果的执行函数
     * @param job
     * @param task
     * @return
     */
    public abstract String execute(Job job, Job task, String param) throws Exception;

    /**
     * 生成任务
     * @return
     */
    public void generateTasks() throws Exception {
        // 1、设置作业参数
        this.setParam(String.valueOf(this.getJobParam()));
        // 2、获取任务参数
        List<String> taskParams = this.generateTaskParams(this.getJobParam());
        // 3、根据任务参数生成任务
        this.mappingTask(taskParams, param -> (this));
    }

    /**
     * 调用有返回结果的execute，简化任务参数设置
     * @param job
     * @param task
     * @throws Exception
     */
    @Override
    public void execute(Job job, Job task) throws Exception {
        // 1、获取任务参数
        String param = task.getParam();
        // 2、获取任务结果
        String result = this.execute(job, task, param);
        // 3、设置任务结果
        task.setResult(result);
    }

    @Override
    public void handleInit(GenericExecutorManager executorManager) {

    }

    @Override
    public void handleRun(GenericExecutorManager executorManager) throws Exception {

    }

    @Override
    public void handleCreateJob(Job job) throws Exception {

    }

    @Override
    public void handleStartJob(Job job) throws Exception {

    }

    @Override
    public void handleWaitingJob(Job job) throws Exception {

    }

    @Override
    public void handlePauseJob(Job job) throws Exception {

    }

    @Override
    public void handleStopJob(Job job) throws Exception {

    }

    @Override
    public void handleRemoveJob(Job job) throws Exception {

    }

    @Override
    public void handleFinishJob(Job job) throws Exception {

    }

    @Override
    public void handleAddTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleWaitTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleReadyTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleRunningTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {

    }

    @Override
    public void handleSkipTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handlePauseTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleStopTask(Job job, Job task) throws Exception {

    }

    @Override
    public void handleTask(Job job, Job task, JobStatus status) throws Exception {

    }
}
