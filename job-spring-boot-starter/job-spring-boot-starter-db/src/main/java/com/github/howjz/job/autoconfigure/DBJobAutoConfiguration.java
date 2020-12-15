package com.github.howjz.job.autoconfigure;

import com.github.howjz.job.Executor;
import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.manager.GenericExecutorManager;
import com.github.howjz.job.operator.Operator;
import com.github.howjz.job.operator.cross.CrossData;
import com.github.howjz.job.operator.error.ErrorData;
import com.github.howjz.job.operator.execute.ExecuteData;
import com.github.howjz.job.operator.join.JoinData;
import com.github.howjz.job.operator.link.LinkData;
import com.github.howjz.job.operator.notify.NotifyData;
import com.github.howjz.job.operator.then.ThenData;
import com.github.howjz.job.operator.waiting.WaitingData;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author zhangjh
 * @date 2020/12/15 10:00
 */
@Configuration
@EnableConfigurationProperties(JobProperties.class)
@Lazy(false)
@EnableAsync
@ConditionalOnExpression("${how.job.enable:false} && '${how.job.type:local}'.equals('db')")
public class DBJobAutoConfiguration extends GenericJobAutoConfiguration {

    @Override
    public NotifyData operatorNotifyData() {
        return super.operatorNotifyData();
    }

    @Override
    public ErrorData operatorErrorData() {
        return super.operatorErrorData();
    }

    @Override
    public ExecuteData operatorExecuteData() {
        return super.operatorExecuteData();
    }

    @Override
    public LinkData operatorLinkData() {
        return super.operatorLinkData();
    }

    @Override
    public ThenData operatorThenData() {
        return super.operatorThenData();
    }

    @Override
    public JoinData operatorJoinData() {
        return super.operatorJoinData();
    }

    @Override
    public CrossData operatorCrossData() {
        return super.operatorCrossData();
    }

    @Override
    public WaitingData operatorWaitingData() {
        return super.operatorWaitingData();
    }

    @Override
    public JobDataContext dataContext() {
        return super.dataContext();
    }

    @Override
    public Executor executor() {
        return super.executor();
    }

    @Override
    public Map<String, Executor> executors() {
        return super.executors();
    }

    @Override
    public Map<String, Job> jobMap() {
        return super.jobMap();
    }

    @Override
    public Map<String, Map<String, Job>> jobTaskMap() {
        return super.jobTaskMap();
    }

    @Override
    public BlockingQueue<String> taskQueue() {
        return super.taskQueue();
    }

    @Override
    public ExecutorService executorPool() {
        return super.executorPool();
    }

    @Override
    public ExecutorService spareExecutorPool() {
        return super.spareExecutorPool();
    }

    @Override
    public List<Operator<?>> operators() {
        return super.operators();
    }

    @Override
    public GenericExecutorManager executorManager() {
        return super.executorManager();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
