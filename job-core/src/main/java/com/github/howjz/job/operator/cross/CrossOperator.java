package com.github.howjz.job.operator.cross;

import com.github.howjz.job.Executor;
import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.operator.GenericOperator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/13 19:55
 */
@Slf4j
public class CrossOperator extends GenericOperator<CrossType> {

    private final Executor.Mode executorMode;

    private final Map<String, CrossType> crossTypeMap;

    public CrossOperator(JobDataContext dataContext) {
        super(dataContext);
        this.executorMode = dataContext.getExecutor().getMode();
        this.crossTypeMap = dataContext.getCrossData().getCrossTypeMap();
    }

    @Override
    public Operate operate() {
        return Operate.CROSS;
    }

    @Override
    public void handleOperate(Job jobOrTask, CrossType operator) {
        jobOrTask.set_cross(operator);
        if (this.executorMode == Executor.Mode.LOCAL) {
            log.error("当前为 [LOCAL] 模式，不支持 cross");
            jobOrTask.set_cross(CrossType.DISABLE);
        }

    }
}
