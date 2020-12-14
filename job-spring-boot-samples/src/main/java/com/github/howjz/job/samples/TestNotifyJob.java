package com.github.howjz.job.samples;

import com.github.howjz.job.Job;
import com.github.howjz.job.operator.genericjob.GenericJob;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangjh
 * @date 2020/12/14 0:19
 */
public class TestNotifyJob extends GenericJob<Integer> {

    private static final long serialVersionUID = 3241361772948727470L;

    // 任务如果创建存在了 sec 秒后，不管怎么样都得执行了
    private int sec;

    protected TestNotifyJob(Integer jobParam) throws Exception {
        super(jobParam);
        this.sec = 5;
    }

    @Override
    public List<Object> generateTaskParams(Integer tj) {
        return Stream.iterate(1, n -> n + 1)
                .limit(tj)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized String execute(Job job, Job task, Object param) throws Exception {
        int num = (int) param;
        int result = new Random().nextInt(1000);
        Thread.sleep(result);
        this.log("任务 " + num + " 完成");
        return String.valueOf(result);
    }

    // =============================== 自定义作业内部 主动 暂停、重开 任务 ===============================
    /*
        注意：该类操作是 内部主动 触发，外部被动 触发不需要定义这些
        暂停：
            一般在 handleReadyTask 中调用，注意 任务 的执行流程如下
                WAIT READY RUNNING COMPLETE
            因此 在 handleWaitTask 和 handleReadyTask 都可以 暂停 任务
            但是一般都是在 handleReadyTask
            暂停前，需要设定复数的判断条件，防止任务被重复暂停（任务重开后又会调用 handleReadyTask，复数判断条件能防止被重复暂停）
        重开：
            任务暂停后，每当执行器检查到当前任务时，会一直调用 handlePauseTask 方法
            因此如果任务需要自动重开，在 handlePauseTask 实现
            该类操作对 外部被动 触发也有效，当任务被外部暂停后，如果符合重开条件，任务也会自动重开
            如果需要手动重开就不要定义 restart 的逻辑
        备注：
            暂停 和 重开 的判断逻辑应该互斥并且类似
            以当前作业为例
                暂停条件：( 任务参数 > 作业参数 / 2 - 1 ) && (( 当前时间 - 任务创建时间 ) <= 固定时间)
                重开条件：(( 当前时间 - 任务创建时间 ) > 固定时间)
            可以看出
                当 任务创建 过久后，任务一定会执行
     */
    /**
     * 任务可在 ready、running 之前进行暂停
     * 暂停依据：
     *      任务的参数 > 作业参数 / 2 - 1
     *  &&  任务创建时间 与 当前时间 间隔 > sec
     *  注意暂停依据必须是 复数 的
     *  原因是任务在开始执行的时候都会调用 handleReadyTask，如果不是复数的可能会造成任务重复暂停
     * @param job
     * @param task
     * @throws Exception
     */
    @Override
    public void handleReadyTask(Job job, Job task) throws Exception {
        // 如果执行了 tj 一半以后的任务，当前任务暂停
        Integer num = (Integer) task.getParam();
        // 当前任务参数 > 作业参数 / 2 - 1
        boolean flag1 = num > (((Integer) job.getParam()) / 2) - 1;
        // 任务创建时间 与 当前时间 间隔 > sec
        boolean flag2 = this.createSpace(job, task) == -1;
        if (flag1 && flag2) {
            this.log(String.format("任务 %s 被暂停了", task.getParam()));
            job.pause(task);
        }
    }

    /**
     *  handlePauseTask 在任务被暂停的期间会一直被调用
     * @param job
     * @param task
     * @throws Exception
     */
    @Override
    public void handlePauseTask(Job job, Job task) throws Exception {
        this.log("任务 " + task.getParam() + " 又又又又双叒叕被暂停了");
        // 如果想在方法内部直接再开任务，调用 restartJob 或者 restartTask 即可
        long space = this.createSpace(job, task);
        if (space != -1) {
            this.log("任务 " + task.getParam() + " 都创建 " + space +  " 秒了，我要自动重开了");
            job.restart(task);
        }
    }

    private long createSpace(Job job, Job task) {
        long current = System.currentTimeMillis();
        if ((current - task.getCreateTime().getTime()) > (sec * 1000)) {
            return (current - task.getCreateTime().getTime()) / 1000;
        }
        return -1;
    }

    @Override
    public void then(Job job) throws Exception {
        this.log("我想要移除掉我自己");
        this.remove();
    }
}
