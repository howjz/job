package com.github.howjz.job.operator.execute;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.operator.GenericOperator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 *  子任务执行操作
 * @author zhangjh
 * @date 2020/12/11 10:14
 */
@Slf4j
public class ExecuteOperator extends GenericOperator<Executable> {

    // 执行参数可能跨平台
    private final Map<String, Executable> executableMap;

    // 线程只在本地执行
    private final Map<String, ExecuteRunnable> executeRunnableMap;

    // ================================= 数据交换 =================================
    private final String executorId;

    private final ExecutorService executorPool;

    public ExecuteOperator(JobDataContext dataContext) {
        super(dataContext);
        this.executableMap = dataContext.getExecuteData().getExecutableMap();
        this.executeRunnableMap = dataContext.getExecuteData().getExecuteRunnableMap();
        this.executorId = dataContext.getExecutor().getExecutorId();
        this.executorPool = dataContext.getExecutorPool();
    }

    @Override
    public Operate operate() {
        return Operate.EXECUTE;
    }

    @Override
    public void handleOperate(Job jobOrTask, Executable operator) {
        executableMap.put(jobOrTask.getId(), operator);
    }

    @Override
    public synchronized void handleReadyTask(Job job, Job task) throws Exception {
        Executable executable = executableMap.get(task.getId());
        if (executable == null) {
            log.error(String.format("任务[%s:%s]的执行函数为空", task.getParentId(), task.getId()));
            return;
        }
        executeRunnableMap.put(task.getId(), new ExecuteRunnable(job, task, executable));
    }

    @Override
    public synchronized void handleRunningTask(Job job, Job task) throws Exception {
        ExecuteRunnable runnable = this.executeRunnableMap.get(task.getId());
        if (runnable == null) {
            log.error(String.format("任务[%s:%s]的执行线程为空", task.getParentId(), task.getId()));
            return;
        }
        this.executorPool.execute(runnable);
        // 设置执行器ID
        task.setExecutor(this.executorId);
        JobHelper.manager.syncTask(job, task);
    }

    @Override
    public synchronized void handleCompleteTask(Job job, Job task) throws Exception {
        if (task == null) {
            log.error("任务为空");
            return;
        }
        this.executableMap.remove(task.getId());
        this.executeRunnableMap.remove(task.getId());
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
}
