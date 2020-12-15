package com.github.howjz.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.howjz.job.operator.Operator;
import com.github.howjz.job.operator.cross.CrossData;
import com.github.howjz.job.operator.error.ErrorData;
import com.github.howjz.job.operator.execute.ExecuteData;
import com.github.howjz.job.operator.join.JoinData;
import com.github.howjz.job.operator.link.LinkData;
import com.github.howjz.job.operator.notify.NotifyData;
import com.github.howjz.job.operator.then.ThenData;
import com.github.howjz.job.operator.waiting.WaitingData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 *  数据上下文：保存了全局要使用到的数据
 * @author zhangjh
 * @date 2020/8/31 22:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JobDataContext implements Serializable {
    private static final long serialVersionUID = 1634103958556888640L;

    // 执行器信息
    private Executor executor;

    // 执行器信息列表
    private Map<String, Executor> executors;

    // 作业详情列表
    @JsonIgnore
    private Map<String, Job> jobMap;

    // 作业任务详情列表
    @JsonIgnore
    private Map<String, Map<String, Job>> jobTaskMap;

    // 任务队列
    @JsonIgnore
    private BlockingQueue<String> taskQueue;

    // 主线程池
    @JsonIgnore
    private ExecutorService executorPool;

    // 备用线程池
    @JsonIgnore
    private ExecutorService spareExecutorPool;

    // 操作器
    @JsonIgnore
    private List<Operator<?>> operators;

    // =============================== 操作器需要数据 ===============================
    // 状态触发操作数据
    private NotifyData notifyData;

    // 错误触发操作数据
    private ErrorData errorData;

    // 执行触发操作数据
    private ExecuteData executeData;

    // 串联触发操作数据
    private LinkData linkData;

    // then触发操作数据
    private ThenData thenData;

    // join触发操作数据
    private JoinData joinData;

    // cross触发操作数据
    private CrossData crossData;

    // waiting触发操作数据
    private WaitingData waitingData;
}
