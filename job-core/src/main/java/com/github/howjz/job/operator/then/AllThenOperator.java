package com.github.howjz.job.operator.then;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.operator.GenericOperator;

import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/12 18:54
 */
public class AllThenOperator extends GenericOperator<Thenable> {

    private final Map<String, Thenable> allThenMap;

    public AllThenOperator(JobDataContext dataContext) {
        super(dataContext);
        this.allThenMap = dataContext.getThenData().getAllThenMap();
    }

    @Override
    public Operate operate() {
        return Operate.ALL_THEN;
    }

    @Override
    public void handleOperate(Job jobOrTask, Thenable operator) {
        this.allThenMap.put(jobOrTask.getId(), operator);
    }

    @Override
    public void handleEndJob(Job job) throws Exception {
        Thenable thenable = this.allThenMap.get(job.getId());
        if (thenable != null) {
            thenable.then(job);
            this.allThenMap.remove(job.getId());
        }
    }

}
