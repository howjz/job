package com.github.howjz.job.operator.join;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.operator.OperatorEnableFlag;
import com.github.howjz.job.util.DependUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/11 10:21
 */
@Slf4j
public class JoinOperator extends GenericOperator<ObjectUtils.Null> {

    // 注意，joinBean 只在本地计算
    private Map<String, JoinBean> joinBeanMap = new HashMap<>(0);

    private final Map<String, List<String>> waitMap;

    private final Map<String, List<String>> notifyMap;

    public JoinOperator(JobDataContext dataContext) {
        super(dataContext);
        this.waitMap = dataContext.getJoinData().getWaitMap();
        this.notifyMap = dataContext.getJoinData().getNotifyMap();
    }

    @Override
    public Operate operate() {
        return Operate.JOIN;
    }

    @Override
    public void handleCreateJob(Job job) throws Exception {
        this.joinBeanMap.put(job.getId(), new JoinBean(job));
    }

    @Override
    public void handleOperate(Job jobOrTask, ObjectUtils.Null operator) {
        JoinBean joinBean = this.joinBeanMap.get(jobOrTask.getId());
        if (joinBean == null) {
            log.error(String.format("作业[%s]的join中间数据不存在", jobOrTask.getId()));
            return;
        }
        joinBean.handleOperate(jobOrTask, operator);
    }

    @Override
    public void handleAddTask(Job job, Job task) throws Exception {
        JoinBean joinBean = this.joinBeanMap.get(job.getId());
        if (joinBean == null) {
            log.error(String.format("作业[%s]的join中间数据不存在", job.getId()));
            return;
        }
        joinBean.handleAddTask(job, task);
    }

    @Override
    public void handleStartJob(Job job) throws Exception {
        JoinBean joinBean = this.joinBeanMap.get(job.getId());
        if (joinBean == null) {
            log.error(String.format("作业[%s]的join中间数据不存在", job.getId()));
            return;
        }
        DependUtil.mergeDependMap(this.waitMap, JoinUtil.getWaitMap(job, this.joinBeanMap));
        DependUtil.mergeDependMap(this.notifyMap, JoinUtil.getNotifyMap(job, this.joinBeanMap));
    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {
        DependUtil.notifyRemoveWait(task, this.waitMap, this.notifyMap);
    }

    @Override
    public OperatorEnableFlag enable(Job job, Job task) {
        List<String> waitIds = this.waitMap.get(task.getId());
        if (waitIds != null && waitIds.size() > 0) {
            return OperatorEnableFlag.RESET;
        } else {
            return OperatorEnableFlag.KEEP;
        }
    }
}
