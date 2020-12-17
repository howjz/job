package com.github.howjz.job.runnable;

/**
 *  执行器执行线程
 * @author zhangjh
 * @date 2020/12/16 22:38
 */
public interface ExecutorRunnable extends Runnable {

    /**
     * 执行
     */
    void run();

    /**
     * 退出
     */
    void destroy();
}
