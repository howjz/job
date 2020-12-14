package com.github.howjz.job.operator.waiting;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.operator.GenericOperator;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhangjh
 * @date 2020/12/13 21:09
 */
public class WaitingOperator extends GenericOperator<String> {

    private final Map<String, Lock> jobLock;

    public WaitingOperator(JobDataContext dataContext) {
        super(dataContext);
        this.jobLock = dataContext.getWaitingData().getJobLock();
    }

    @Override
    public Operate operate() {
        return Operate.WAITING;
    }

    @Override
    public synchronized void handleOperate(Job jobOrTask, String operator) throws Exception {
        // 1、开一个锁，等待异步线程唤醒
        Lock lock = this.getLock(jobOrTask);
        // 2、主线程等待
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEndJob(Job job) throws Exception {
        // 1、注意到end，这里要解开锁
        String jobId = job.getId();
        if (this.jobLock.containsKey(jobId)) {
            // 异步唤醒锁
            Lock waitingLock = this.jobLock.remove(jobId);
            synchronized (waitingLock) {        // 获取对象锁
                waitingLock.notify();           // 子线程唤醒
            }
        }
    }

    private synchronized Lock getLock(Job job) {
        String jobId = job.getId();
        if (!this.jobLock.containsKey(jobId)) {
            Lock lock = new ReentrantLock(false);
            this.jobLock.put(jobId, lock);
        }
        return this.jobLock.get(jobId);
    }
}
