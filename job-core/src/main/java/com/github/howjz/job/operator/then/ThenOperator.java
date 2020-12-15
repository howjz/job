package com.github.howjz.job.operator.then;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.util.DependUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/11 10:22
 */
@Slf4j
public class ThenOperator extends GenericOperator<Thenable> {

    private final Map<String, ThenBean> thenBeanMap;

    private final Map<String, List<String>> waitMap;

    private final Map<String, List<String>> notifyMap;

    public ThenOperator(JobDataContext dataContext) {
        super(dataContext);
        this.thenBeanMap = dataContext.getThenData().getThenBeanMap();
        this.waitMap = dataContext.getThenData().getWaitMap();
        this.notifyMap = dataContext.getThenData().getNotifyMap();
    }

    @Override
    public Operate operate() {
        return Operate.THEN;
    }

    @Override
    public void handleOperate(Job jobOrTask, Thenable operator) {
        ThenBean thenBean = this.thenBeanMap.get(jobOrTask.getId());
        if (thenBean == null) {
            log.error(String.format("作业[%s]的then中间数据不存在", jobOrTask.getId()));
            return;
        }
        thenBean.handleOperate(jobOrTask, operator);
    }

    @Override
    public void handleCreateJob(Job job) {
        this.thenBeanMap.put(job.getId(), new ThenBean(this.getDataContext().getExecutor().getExecutorId()));
    }

    @Override
    public void handleAddTask(Job job, Job task) throws Exception {
        ThenBean thenBean = this.thenBeanMap.get(job.getId());
        if (thenBean == null) {
            log.error(String.format("作业[%s]的then中间数据不存在", job.getId()));
            return;
        }
        thenBean.handleAddTask(job, task);
    }

    @Override
    public void handleStartJob(Job job) throws Exception {
        ThenBean thenBean = this.thenBeanMap.get(job.getId());
        if (thenBean == null) {
            log.error(String.format("作业[%s]的then中间数据不存在", job.getId()));
            return;
        }
        DependUtil.mergeDependMap(this.waitMap, ThenUtil.getWaitMap(job, thenBeanMap));
        DependUtil.mergeDependMap(this.notifyMap, ThenUtil.getNotifyMap(job, thenBeanMap));
    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {
        // 1、移除 notify
        DependUtil.notifyRemoveWait(task, this.waitMap, this.notifyMap);
        // 2、根据 thenId 判断
        List<String> thenIds = this.notifyMap.get(task.getId());
        boolean remove = false;
        if (thenIds != null && thenIds.size() > 0) {
            for(String thenId: thenIds) {
                List<String> waitIds = this.waitMap.get(thenId);
                if (waitIds == null || waitIds.size() == 0) {
                    ThenBean thenBean = this.thenBeanMap.get(job.getId());
                    if (thenBean != null) {
                        thenBean.start(thenId, job,null);
                    }
                    this.waitMap.remove(thenId);
                }
                remove = !this.waitMap.containsKey(thenId);
            }
        }
        if (remove) {
            this.notifyMap.remove(task.getId());
        }
    }

    @Override
    public void handleCompleteJob(Job job) throws Exception {
        ThenBean thenBean = this.thenBeanMap.get(job.getId());
        if (thenBean != null) {
            thenBean.startAll(job, null);
        }
    }

}
