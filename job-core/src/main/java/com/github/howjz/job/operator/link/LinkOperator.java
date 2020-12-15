package com.github.howjz.job.operator.link;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.operator.OperatorEnableFlag;
import com.github.howjz.job.util.DependUtil;
import org.apache.commons.lang.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/12 0:14
 */
public class LinkOperator extends GenericOperator<ObjectUtils.Null> {

    private final Map<String, List<String>> waitMap;

    private final Map<String, List<String>> notifyMap;

    public LinkOperator(JobDataContext dataContext) {
        super(dataContext);
        this.waitMap = dataContext.getLinkData().getWaitMap();
        this.notifyMap = dataContext.getLinkData().getNotifyMap();
    }

    @Override
    public Operate operate() {
        return Operate.LINK;
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

    @Override
    public void handleStartJob(Job job) throws Exception {
        DependUtil.mergeDependMap(this.waitMap, LinkUtil.getWaitMap(job));
        DependUtil.mergeDependMap(this.notifyMap, LinkUtil.getNotifyMap(job));
    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {
        DependUtil.notifyRemoveWait(task, this.waitMap, this.notifyMap);
    }

}
