package com.github.howjz.job.operator.waiting;

import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.operator.GenericOperator;

/**
 * @author zhangjh
 * @date 2020/12/13 21:09
 */
public class WaitingOperator extends GenericOperator<String> {
    public WaitingOperator(JobDataContext dataContext) {
        super(dataContext);
    }

    @Override
    public Operate operate() {
        return Operate.WAITING;
    }
}
