package com.github.howjz.job.manager;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.constant.JobKey;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.constant.JobType;
import com.github.howjz.job.manager.executor.AbstractExecutorManager;
import com.github.howjz.job.operator.OperatorEnableFlag;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhangjh
 * @date 2020/12/7 15:27
 */
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class GenericExecutorManager extends AbstractExecutorManager {

    public GenericExecutorManager(JobDataContext dataContext) {
        super(dataContext, new GenericJobManager(dataContext), new GenericTaskManager(dataContext));
        // 注意，这里就调用了 init，因此在其他的 operator 中就可以
        this.handleInit(this);
    }

    @Override
    public void executeTask() throws Exception {
        String jobAndTaskId = this.consumeTaskId();
        Job job;
        Job task;
        JobStatus status;
        // 1、当前ID不能为空
        if (StringUtils.isEmpty(jobAndTaskId)) {
            return;
        }
        // 2、判断当前ID格式
        String[] jobAndTaskIds = jobAndTaskId.split(JobKey.SEPARATOR);
        if (jobAndTaskIds.length != 2) {
            return;
        }
        String jobId = jobAndTaskIds[0];
        String taskId = jobAndTaskIds[1];
        job = this.findJob(jobId, true);
        task = this.findTask(jobId, taskId);
        if (job == null || task == null) {
            return;
        }
        // 3、判断当前状态，当前状态不能是结束
        if (task.getStatus().isEnd()) {
            if (JobType.TASK_JOB == task.getType()) {
                this.handleCompleteTask(job, task);
            }
            return;
        }
        // 4、最后一步，根据监听器配置判断是否允许执行
        OperatorEnableFlag enableFlag = this.enable(job, task);
        switch (enableFlag) {
            case DISABLE:
                return;
            case RESET:
                this.offerTaskId(jobAndTaskId);
                break;
            case KEEP:
                // 6、真正开始执行任务
                JobType jobType = task.getType();
                if (jobType != null) {
                    switch (jobType) {
                        case TASK:
                            // 3.1.1、任务 就绪
                            this.handleReadyTask(job, task);
                            // 3.1.2、任务 就绪后，根据状态继续执行
                            enableFlag = this.enable(job, task);
                            switch (enableFlag) {
                                case KEEP:
                                    // 3.1.3、任务开始
                                    this.handleRunningTask(job, task);
                                    break;
                                case RESET:
                                    this.offerTaskId(jobAndTaskId);
                                    break;
                            }
                            break;
                        case TASK_JOB:
                            this.handleCompleteTask(job, task);
                            break;
                        default:
                            break;
                    }
                }
                break;
        }
    }

}
