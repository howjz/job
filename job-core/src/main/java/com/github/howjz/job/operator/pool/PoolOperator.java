package com.github.howjz.job.operator.pool;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.manager.GenericExecutorManager;
import com.github.howjz.job.operator.GenericOperator;
import com.github.howjz.job.operator.OperatorEnableFlag;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/13 13:08
 */
public class PoolOperator extends GenericOperator<PoolBean> {

    // 配置信息
    private final PoolProperties poolProperties;

    // 公有线程池
    private final PoolBean publicPool;

    // 私有线程池
    private final Map<String, PoolBean> privatePools;


    public PoolOperator(JobDataContext dataContext) {
        super(dataContext);
        this.poolProperties = dataContext.getExecutor().getPool();
        synchronized (this) {
            if (this.poolProperties.getPublicPool() == null) {
                this.poolProperties.setPublicPool(new PoolBean(this.poolProperties.getSize(), this.poolProperties.getSize()));
            }
            if (this.poolProperties.getPrivatePools() == null) {
                this.poolProperties.setPrivatePools(new ConcurrentHashMap<>());
            }
        }
        this.publicPool = this.poolProperties.getPublicPool();
        this.privatePools = this.poolProperties.getPrivatePools();
    }

    @Override
    public Operate operate() {
        return Operate.POOL;
    }

    @Override
    public synchronized void handleOperate(Job jobOrTask, PoolBean operator) {
        this.privatePools.put(jobOrTask.getId(), operator);
    }

    @Override
    public void handleRun(GenericExecutorManager executorManager) throws Exception {
        // 如果队列中没有任务，进行休眠
        if (executorManager.getQueueTaskCount() == 0) {
            Thread.sleep(this.poolProperties.getSleep());
        }
    }

    @Override
    public synchronized OperatorEnableFlag enable(Job job, Job task) throws Exception {
        OperatorEnableFlag flag = OperatorEnableFlag.KEEP;
        // 1、查找 线程池配置
        PoolBean poolBean = this.privatePools.get(job.getId());
        if (poolBean == null) {
            // 2、线程池配置 为空 或者 为 公用线程池，则采用公共计数
            if (this.publicPool.getCurrentSize() == 0) {
                flag = OperatorEnableFlag.RESET;
            }
        } else {
            // 3、线程池不为空，则查看当前计数
            Integer currentPoolSize = poolBean.getCurrentSize();
            if (currentPoolSize == 0) {
                // 3.1、当前计数 < 0，任务重新回到队列中等待
                flag  = OperatorEnableFlag.RESET;
            }
        }
        return flag;
    }

    private synchronized void increment(Job job, Job task) {
        if (task.getStatus() == JobStatus.PAUSE || task.getStatus() == JobStatus.STOP || task.getStatus() == JobStatus.WAIT) {
            return;
        }
        PoolBean poolBean = this.privatePools.get(job.getId());
        if (poolBean == null) {
            this.publicPool.setCurrentSize(this.publicPool.getCurrentSize() + 1);
        } else {
            poolBean.setCurrentSize(poolBean.getCurrentSize() + 1);
            this.privatePools.put(job.getId(), poolBean);
        }
        if (job.isEnd()) {
            this.privatePools.remove(job.getId());
        }
    }

    private synchronized void decrement(Job job, Job task) {
        PoolBean poolBean = this.privatePools.get(job.getId());
        if (poolBean == null) {
            this.publicPool.setCurrentSize(this.publicPool.getCurrentSize() - 1);
        } else {
            poolBean.setCurrentSize(poolBean.getCurrentSize() - 1);
            this.privatePools.put(job.getId(), poolBean);
        }
    }

    @Override
    public void handleRunningTask(Job job, Job task) throws Exception {
        this.decrement(job, task);
    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {
        this.increment(job, task);
    }

    @Override
    public void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {
        this.increment(job, task);
    }

    @Override
    public void handleSkipTask(Job job, Job task) throws Exception {
        this.increment(job, task);
    }

    @Override
    public void handlePauseTask(Job job, Job task) throws Exception {
        this.increment(job, task);
    }

    @Override
    public void handleStopTask(Job job, Job task) throws Exception {
        this.increment(job, task);
    }
}
