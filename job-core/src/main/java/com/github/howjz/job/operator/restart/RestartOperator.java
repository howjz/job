package com.github.howjz.job.operator.restart;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.constant.JobType;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.operator.notify.NotifyBean;
import com.github.howjz.job.operator.notify.NotifyUtil;
import javafx.concurrent.Task;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangjh
 * @date 2020/12/14 0:35
 */
public class RestartOperator extends GenericOperator<RestartBean> {

    private final Map<String, Integer> notifyMap;

    public RestartOperator(JobDataContext dataContext) {
        super(dataContext);
        this.notifyMap = dataContext.getNotifyData().getNotifyMap();
    }

    @Override
    public Operate operate() {
        return Operate.RESTART;
    }

    @Override
    public synchronized void handleOperate(Job jobOrTask, RestartBean operator) throws Exception {
        this.restartJob(operator);
    }

    private void restartJob(RestartBean restartBean) throws Exception {
        Job job = restartBean.getJob();
        Job task = restartBean.getTask();
        if (job.getType() != JobType.JOB && job.getType() != JobType.TASK_JOB) {
            throw new RuntimeException("当前获取到的是任务对象，无法直接调用restart方法");
        }
        if (task == null) {
            for (Job t1 : job.getTasks()) {
               this.restartTask(job, t1);
            }
            // 1.3、主动触发作业 wait
            JobHelper.manager.handleWaitingJob(job);
        } else {
            this.restartTask(job, task);
        }
    }

    private void restartTask(Job job, Job task) throws Exception {
        if (JobStatus.PAUSE == task.getStatus()) {
            // 1、移除 当前作业 的触发状态
            this.notifyMap.remove(NotifyUtil.targetNotifyTaskId(task));
            // 2、主动触发任务 wait
            JobHelper.manager.handleWaitTask(job, task);
            // 3、子任务重开
            if (task.getType() == JobType.TASK_JOB) {
                this.restartJob(new RestartBean(task, null));
            }
        }
    }
}
