package com.github.howjz.job.operator.notify;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.constant.JobType;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.operator.OperatorEnableFlag;

import java.util.Map;
import java.util.Set;

/**
 * @author zhangjh
 * @date 2020/12/13 21:06
 */
public class NotifyOperator extends GenericOperator<NotifyBean> {

    private final Map<String, Integer> notifyMap;

    public NotifyOperator(JobDataContext dataContext) {
        super(dataContext);
        this.notifyMap = dataContext.getNotifyData().getNotifyMap();
    }

    @Override
    public Operate operate() {
        return Operate.NOTIFY;
    }

    @Override
    public synchronized OperatorEnableFlag enable(Job job, Job task) throws Exception {
        OperatorEnableFlag flag = OperatorEnableFlag.KEEP;
        // 1、获取当前任务的目标触发状态
        String targetNotifyTaskId = NotifyUtil.targetNotifyTaskId(task);
        Integer i = this.notifyMap.get(targetNotifyTaskId);
        if (i != null) {
            JobStatus status = JobStatus.findByValue(i);
            switch (status) {
                case PAUSE:
                    // 2.1、重新放回任务队列
                    flag = OperatorEnableFlag.RESET;
                    // 2.2、手动触发任务暂停事件
                    JobHelper.manager.handlePauseTask(job, task);
                    break;
                case STOP:
                    // 3.1、直接停了
                    flag = OperatorEnableFlag.DISABLE;
                    // 3.2、手动触发任务停止事件
                    JobHelper.manager.handleStopTask(job, task);
                    break;
                case COMPLETE:
                    // 3.2、直接完成
                    flag = OperatorEnableFlag.DISABLE;
                    // 3.3、手动触发任务完成时间
                    JobHelper.manager.handleCompleteTask(job, task);
            }
        }
        return flag;
    }

    @Override
    public synchronized void handleOperate(Job jobOrTask, NotifyBean operator) throws Exception {
        switch (operator.getNotifyType()) {
            case PAUSE:
                this.notifyJob(operator, JobStatus.PAUSE);
                break;
            case STOP:
                this.notifyJob(operator, JobStatus.STOP);
                break;
            case COMPLETE:
                this.notifyJob(operator, JobStatus.COMPLETE);
                break;
            case REMOVE:
                if (!jobOrTask.isEnd()) {
                    throw new RuntimeException("当前作业未结束，无法移除");
                }
                // 主动触发 remove
                Job job = operator.getJob();
                this.getExecutorManager().handleRemoveJob(job);
                break;
        }
    }

    private synchronized void notifyJob(NotifyBean notifyBean, JobStatus status) throws Exception {
        Job job = notifyBean.getJob();
        Job task = notifyBean.getTask();
        if (job.getType() != JobType.JOB && job.getType() != JobType.TASK_JOB) {
            throw new RuntimeException("当前获取到的是任务对象，无法直接调用 pause 或 stop 方法");
        }
        if (task == null) {
            for (Job t : job.getTasks()) {
               this.notifyTask(notifyBean, job, t, status);
            }
            // 3、手动触发时间
            switch (status) {
                case PAUSE:
                    JobHelper.manager.handlePauseJob(job);
                    break;
                case STOP:
                    JobHelper.manager.handleStopJob(job);
                    break;
                case COMPLETE:
                    JobHelper.manager.handleCompleteJob(job);
                    break;
            }
        } else {
            this.notifyTask(notifyBean, job, task, status);
        }
    }

    private synchronized void notifyTask(NotifyBean notifyBean, Job job, Job task, JobStatus status) throws Exception {
        if (this.pauseable(task)) {
            this.notifyMap.put(NotifyUtil.targetNotifyTaskId(task), status.getValue());
            // 1、手动触发 pause / stop
            switch (status) {
                case PAUSE:
                case STOP:
                    break;
            }
        }
        // 2、子任务触发
        if (task.getType() == JobType.TASK_JOB) {
            this.notifyJob(new NotifyBean(notifyBean.getNotifyType(), task, null), status);
        }
    }

    // 筛选出 未结束 / 异常但是重试次数 > 0 的任务
    private boolean pauseable(Job task) {
        JobStatus currentStatus = task.getStatus();
        if (JobStatus.EXCEPTION == currentStatus && task.get_retry() != null && task.get_retry() > 0) {
            return true;
        }
        return JobStatus.EXCEPTION != currentStatus && !currentStatus.isEnd();
    }
}
