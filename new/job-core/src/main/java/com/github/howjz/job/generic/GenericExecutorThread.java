package com.github.howjz.job.generic;

import com.github.howjz.job.manager.ExecutorManager;
import com.github.howjz.job.operator.thread.ThreadUtil;
import com.github.howjz.job.thread.ExecutorThread;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 *  通用执行器执行线程
 * @author zhangjh
 * @date 2020/12/16 23:35
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class GenericExecutorThread implements ExecutorThread {

    private boolean destroy = false;

    // 执行器线程ID：primary-0、least-1、least-2、least-3
    private String id;

    private int threshold;

    // 是否主执行线程
    private boolean primary;

    // 执行器管理器
    private ExecutorManager executorManager;

    public GenericExecutorThread(ExecutorManager executorManager, boolean primary, int threshold) throws Exception {
        this.executorManager = executorManager;
        this.primary = primary;
        this.threshold = threshold;
        this.id = ThreadUtil.getRunnableId(primary, threshold);
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!destroy) {
            // 1、触发 执行管理器 - 执行线程执行中
            this.executorManager.handleReady(this);
            // 2、具体的执行流程
            this.execute();
            // 3、触发 执行管理器 - 执行线程执行完成
            this.executorManager.handleExecuted(this);
        }
    }

    private void execute() throws Exception {

    }

    @Override
    public void destroy() {
        this.destroy = true;
    }

}
