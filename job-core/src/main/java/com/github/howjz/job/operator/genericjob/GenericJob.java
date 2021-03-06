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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private static final long serialVersionUID = 8708949801206772106L;
    private boolean ready;

    protected GenericJob() {
        this.ready = false;
        try {
            // 这里需要手动触发 作业创建
            JobHelper.manager.handleCreateJob(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成任务参数
     * @param tj            作业参数
     * @return              任务参数列表
     * @throws Exception    异常
     */
    public abstract List<Object> generateTaskParams(T tj) throws Exception;

    /**
     * 存在返回结果的执行函数
     * @param job           作业
     * @param task          任务
     * @param param         参数
     * @return              执行结果
     * @throws Exception    异常
     */
    public abstract String execute(Job job, Job task, Object param) throws Exception;

    /**
     * 必须调用init方法
     * @return              作业
     * @throws Exception    异常
     */
    @Override
    public Job ready() throws Exception {
        // 当前为 GenericJob，生成任务并设置then
        this.handleReadyJob(this);
        this.ready = true;
        this.generateTasks();
        this.then((Thenable) this);
        return this;
    }

    /**
     * 生成任务
     * @throws Exception    异常
     */
    public void generateTasks() throws Exception {
        // 1、获取任务参数
        List<Object> taskParams = this.generateTaskParams((T) this.getParam());
        // 2、根据任务参数生成任务
        this.mappingTask(taskParams, param -> (this));
    }

    /**
     * 调用有返回结果的execute，简化任务参数设置
     * @param job           作业
     * @param task          任务
     * @throws Exception    异常
     */
    @Override
    public void execute(Job job, Job task) throws Exception {
        // 1、获取任务参数
        Object param = task.getParam();
        // 2、获取任务结果
        String result = this.execute(job, task, param);
        // 3、设置任务结果
        task.setResult(result);
    }

    /**
     * @param text              打印文字
     * @throws IOException      异常
     */
    public synchronized void log(String text) throws IOException {
        System.out.println(text);
        File logFile = new File("log/job_" + this.getId() + ".txt");
        if (!logFile.exists()) {
            FileUtils.touch(logFile);
        }
        FileWriter fileWriter = new FileWriter(logFile, true);
        fileWriter
                .append(text)
                .append("\n")
                .close();
    }

    @Override
    public void handleInit(GenericExecutorManager executorManager) {

    }

    @Override
    public void handleRun(GenericExecutorManager executorManager) throws Exception {

    }

    @Override
    public void handleReadyJob(Job job) throws Exception {

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
    public void handleCompleteJob(Job job) throws Exception {

    }

    @Override
    public void handleEndJob(Job job) throws Exception {

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

    @Override
    public Job start() throws Exception {
        if (!this.ready) {
            throw new RuntimeException("开启自定义任务前必须调用ready");
        }
        return super.start();
    }
}
