package com.github.howjz.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.howjz.job.util.IDGenerateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 *  执行器数据上下文
 *      全局使用的数据上下文对象
 * @author zhangjh
 * @date 2020/8/31 22:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExecutorDataContext implements Serializable {
    private static final long serialVersionUID = 1634103958556888640L;

    // 全局雪花ID
    public static final IDGenerateUtil IdWorker = new IDGenerateUtil(0,0);

    // 执行器信息
    private Executor executor;

    // 执行器信息列表
    private Map<String, Executor> executors;

    // 任务队列
    @JsonIgnore
    private BlockingQueue<String> taskQueue;

    // 主线程池
    @JsonIgnore
    private ExecutorService executorPool;

    // 备用线程池
    @JsonIgnore
    private ExecutorService spareExecutorPool;

}
