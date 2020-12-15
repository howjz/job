package com.github.howjz.job.operator.debug;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.manager.GenericExecutorManager;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.operator.pool.PoolBean;
import org.apache.commons.lang.ObjectUtils;

import java.util.Date;
import java.util.Map;

/**
 *  debug操作器
 *  1、配置一些debug数据（如最后访问时间）
 *  2、打印日志
 * @author zhangjh
 * @date 2020/12/12 1:00
 */
public class DebugOperator extends GenericOperator<ObjectUtils.Null> {

    // 配置信息
    private final DebugProperties debugProperties;

    // ================================== pool相关 ==================================
    // 公有线程池
    private final PoolBean publicPool;

    // 私有线程池
    private final Map<String, PoolBean> privatePools;

    public DebugOperator(JobDataContext dataContext) {
        super(dataContext);
        this.debugProperties = dataContext.getExecutor().getDebug();
        this.publicPool = dataContext.getExecutor().getPool().getPublicPool();
        this.privatePools = dataContext.getExecutor().getPool().getPrivatePools();
    }

    @Override
    public Operate operate() {
        return Operate.DEBUG;
    }

    @Override
    public void handleRun(GenericExecutorManager executorManager) throws Exception {
        if (this.debugProperties.isLog()) {
//            Thread.sleep(this.debugProperties.getSleep());
//            System.out.println("==========================handleRun=========================");
//            System.out.println(String.format("剩余任务个数：                            [%s]", executorManager.getQueueTaskCount()));
//            System.out.println("==========================handleRun=========================");
        }
        this.debugProperties.setLastTime(new Date());
        this.debugProperties.setTaskCount(executorManager.getQueueTaskCount());
    }

    @Override
    public synchronized void handleCompleteTask(Job job, Job task) throws Exception {
        if (this.debugProperties.isLog()) {
//            System.out.println("=====================handleCompleteTask=====================");
//            System.out.println(String.format("作业主键：                               [%s]", job.getId()));
//            System.out.println(String.format("作业类型：                               [%s]", job.getType()));
//            System.out.println(String.format("作业进度：                               [%s / %s]", job.getSnapshot().getComplete(), job.getSnapshot().getTotal()));
//            System.out.println("=====================handleCompleteTask=====================");
//            Thread.sleep(this.debugProperties.getSleep());
        }
    }

    @Override
    public void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {
        if (this.debugProperties.isLog()) {
            System.out.println("=====================handleExceptionTask====================");
            System.out.println(String.format("任务 [%s:%s] 发生异常：                   [%s]", task.getParentId(), task.getId(), exception.getLocalizedMessage()));
            System.out.println("=====================handleExceptionTask====================");
            Thread.sleep(this.debugProperties.getSleep());
        }
    }


    @Override
    public synchronized void handleTask(Job job, Job task, JobStatus status) throws Exception {
        if (this.debugProperties.isLog()) {
//            System.out.println("=========================handleTask=========================");
//            System.out.println(String.format("当前公有线程池：                          [ %s / %s ]", this.publicPool.getCurrentSize(), this.publicPool.getTotalSize()));
//            PoolBean poolBean = this.privatePools.get(job.getId());
//            if (poolBean != null) {
//                System.out.println(String.format("当前私有线程池 [%s]：     [ %s / %s ]", job.getId(), poolBean.getCurrentSize(), poolBean.getTotalSize()));
//            }
//            System.out.println("=========================handleTask=========================");
//            Thread.sleep(this.debugProperties.getSleep());
        }
    }
}
