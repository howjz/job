package com.github.howjz.job.operator.thread;

/**
 * @author zhangjh
 * @date 2020/12/17 11:00
 */
public class ThreadUtil {

    /**
     * 获取执行器线程ID
     * @param primary   是否是主线程
     * @param id        线程ID
     * @return          执行器线程ID
     */
    public static String getRunnableId(boolean primary, int id) {
        return (primary ? "primary-" : "least-") + id;
    }

}
