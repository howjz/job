package com.github.howjz.job.operator.config;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.operator.GenericOperator;

/**
 * @author zhangjh
 * @date 2020/12/13 0:11
 */
public class ConfigOperator extends GenericOperator<ConfigBean> {

    public ConfigOperator(JobDataContext dataContext) {
        super(dataContext);
    }

    @Override
    public Operate operate() {
        return Operate.CONFIG;
    }

    @Override
    public void handleOperate(Job jobOrTask, ConfigBean operator) {
        jobOrTask.setRetry(operator.getRetry());
        jobOrTask.set_retry(operator.getRetry());
        jobOrTask.set_dynamic(operator.isDynamic());
    }
}
