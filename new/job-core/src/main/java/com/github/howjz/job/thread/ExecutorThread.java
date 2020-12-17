package com.github.howjz.job.thread;

/**
 *  执行器执行线程
 * @author zhangjh
 * @date 2020/12/16 22:38
 */
public interface ExecutorThread extends Runnable {

    /**
     * 执行
     */
    void run();

    /**
     * 退出
     */
    void destroy();

    /**
     * 是否主线程
     * @return  是否主线程
     */
    boolean isPrimary();

    /**
     * 获取ID
     * @return  线程ID
     */
    String getId();

    /**
     * 获取阈值计数
     * @return
     */
    int getThreshold();
}
