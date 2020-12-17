package com.github.howjz.job.generic;

import com.github.howjz.job.manager.ExecutorManager;
import com.github.howjz.job.runnable.ExecutorRunnable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

/**
 *  通用执行器执行线程
 * @author zhangjh
 * @date 2020/12/16 23:35
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GenericExecutorRunnable implements ExecutorRunnable {

    private boolean destroy = false;

    // 执行器线程ID：primary-0、least-1、least-2、least-3
    private String id;

    private ExecutorManager executorManager;

    public GenericExecutorRunnable(String id, ExecutorManager executorManager) throws Exception {
        this.id = id;
        this.executorManager = executorManager;
        // 往执行器管理器注册自身
        this.executorManager.registerRunnable(this);
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
