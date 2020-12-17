package com.github.howjz.job.operator.thread;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.howjz.job.ExecutorDataContext;
import com.github.howjz.job.bean.Operate;
import com.github.howjz.job.generic.AbstractExecutorOperator;
import com.github.howjz.job.generic.GenericExecutorThread;
import com.github.howjz.job.manager.ExecutorManager;
import com.github.howjz.job.thread.ExecutorThread;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author zhangjh
 * @date 2020/12/17 9:59
 */
@Slf4j
public class RunnableOperator extends AbstractExecutorOperator<String> {

    private ExecutorManager executorManager;

    // 加载配置信息
    private ThreadProperties threadProperties;

    // 主执行线程
    @JsonIgnore
    private ExecutorThread primaryThread;

    // 额外执行线程
    @JsonIgnore
    private Map<String, ExecutorThread> threadMap;

    public RunnableOperator(ExecutorDataContext dataContext) {
        super(dataContext);
        this.threadProperties = dataContext.getExecutor().getThread();
    }

    @Override
    public Operate operate() {
        return Operate.RUNNABLE;
    }

    @Override
    public void init(ExecutorManager executorManager) throws Exception {
        this.executorManager = executorManager;
        // 1、新建主执行线程
        this.primaryThread = new GenericExecutorThread(executorManager, true, 0);
        // 2、往 threadMap 中注册 主执行器线程
        this.threadMap.put(this.primaryThread.getId(), this.primaryThread);
        // 3、启动主执行线程
        this.getDataContext().getSpareExecutorPool().execute(this.primaryThread);
    }

    @Override
    public void handleReady(ExecutorThread executorThread) throws Exception {
        // 1、线程执行前，检查当前阈值计数
        int commits = this.executorManager.getCommits();
        int threshold = (int) (commits / this.threadProperties.getThreshold());
        threshold = Math.min(threshold, this.threadProperties.getTotal());
        // 2、根据阈值计数新建新的额外线程
        if (threshold > 0) {
            for(int i = 0; i <= threshold; i++) {
                String runnableId = ThreadUtil.getRunnableId(false, i);
                if (!this.threadMap.containsKey(runnableId)) {
                    ExecutorThread leastThread = new GenericExecutorThread(executorManager, false, i);
                    this.threadMap.put(runnableId, leastThread);
                    // 3、启动额外线程
                    this.getDataContext().getSpareExecutorPool().execute(leastThread);
                }
            }
        }
    }

    @Override
    public void handleExecuted(ExecutorThread executorThread) throws Exception {
        // 1、线程执行后，获取当前线程所属阈值计数
        int threshold = executorThread.getThreshold();
        // 2、获取阈值计数
        long thresholdCount = threshold * this.threadProperties.getThreshold();
        // 3、根据当前任务总数判断
        int commits = this.executorManager.getCommits();
        if (thresholdCount > commits) {
            // 3.1、移除额外线程
            executorThread.destroy();
            this.threadMap.remove(executorThread.getId());
        }
    }

    @Override
    public void destroy() {
        this.primaryThread.destroy();
        for(String runnableId: this.threadMap.keySet()) {
            this.threadMap.get(runnableId).destroy();
        }
    }

    @Override
    public ExecutorThread getPrimaryThread() {
        return this.primaryThread;
    }

    @Override
    public Map<String, ExecutorThread> getThread() throws Exception {
        return this.threadMap;
    }

}
