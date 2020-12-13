package com.github.howjz.job.operator.execute;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.bean.NullJob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zhangjh
 * @date 2020/12/11 15:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExecuteRunnable extends Thread implements Runnable, Serializable {

    private static final long serialVersionUID = 3304920933709178252L;

    @ToString.Exclude
    private Job job;

    private Job task;

    private Executable executable;

    public ExecuteRunnable(Job job, Job task, Executable executable) {
        this.job = job;
        this.task = task;
        this.executable = executable;
    }

    @SneakyThrows
    @Override
    public void run() {
        if (job != null && !(job instanceof NullJob)) {
            try {
                // 1、开始执行任务
                executable.execute(job, task);
                // 3、通知 作业上下文 执行任务完成
                JobHelper.manager.handleCompleteTask(job, task);
            } catch (Exception e) {
                // 4、通知 作业上下文 任务执行异常
                JobHelper.manager.handleExceptionTask(job, task, e);
            }
        }
    }
}
