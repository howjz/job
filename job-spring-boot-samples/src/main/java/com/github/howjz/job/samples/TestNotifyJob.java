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

    private boolean paused;

    protected TestNotifyJob(Integer jobParam) throws Exception {
        super(jobParam);
        this.paused = false;
    }

    /**
     * 产生 0 - tj 任务
     * @param tj    作业参数
     * @return
     */
    @Override
    public List<String> generateTaskParams(Integer tj) {
        return Stream.iterate(1, n -> n + 1)
                .limit(tj)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized String execute(Job job, Job task, String param) throws Exception {
        int num = Integer.parseInt(param);
        int result = new Random().nextInt(1000);
        Thread.sleep(500);
        // 如果执行了 tj 一半以后的任务，当前任务暂停
        if (num > ( this.getJobParam() / 2 - 1)) {
            if (!this.paused) {
                // 注意，这里的 pause 是针对整个作业，因此只需要暂停一次
                // 如果不用一个标志位，可能会造成 暂停了 A、B、C
                // 然后 A 重开，又循环暂停 A、B、C，会导致死循环，因此要么就用标志位只暂停一次，要么就只暂停当前任务
                this.pause();
                System.out.println("任务主动暂停了");
//            this.stop();
//            this.remove(); // 当前作业未完成，移除会失败
                this.paused = true;
                return null;
            }
        }
        System.out.println("任务 " + num + " 完成");
        return String.valueOf(result);
    }

//     handleStopJob 比 handleStopTask 先触发，并且 只会触发一次
    @Override
    public void handleStopJob(Job job) throws Exception {
        System.out.println("作业被停止了");
    }

    // handleStopTask 也只会触发一次
    @Override
    public void handleStopTask(Job job, Job task) throws Exception {
        System.out.println("任务 " + task.getParam() + " 被停止了");
    }

    // handlePauseJob 比 handlePauseTask 先触发，并且 只会触发一次
    @Override
    public void handlePauseJob(Job job) throws Exception {
        System.out.println("作业被暂停了");
    }

    // 注意任务暂停之后，这个 handlePauseTask 会一直触发的
    @Override
    public void handlePauseTask(Job job, Job task) throws Exception {
        System.out.println("任务 " + task.getParam() + " 又又又又双叒叕被暂停了");
        // 如果想在方法内部直接再开任务，调用 restartJob 或者 restartTask 即可
        int sec = 10;
        long current = System.currentTimeMillis();
        if ((current - task.getCreateTime().getTime()) > (sec * 1000)) {
            System.out.println("任务 " + task.getId() + " 都创建 " + ((current - task.getCreateTime().getTime()) / 1000) +  " 秒了，不应该再暂停下去了");
            this.restart(task);
        }
    }

    // handleRemoveJob 在调用 remove 后触发，并且 只会触发一次
    @Override
    public void handleRemoveJob(Job job) throws Exception {
        System.out.println("作业被移除了");
    }

    @Override
    public void then(Job job) throws Exception {
        System.out.println("我想要移除掉我自己");
        this.remove();
    }
}
