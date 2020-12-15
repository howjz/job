package com.github.howjz.job.autoconfigure;

import com.github.howjz.job.Executor;
import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.constant.JobKey;
import com.github.howjz.job.manager.GenericExecutorManager;
import com.github.howjz.job.operator.Operator;
import com.github.howjz.job.operator.config.ConfigOperator;
import com.github.howjz.job.operator.cross.CrossData;
import com.github.howjz.job.operator.cross.CrossOperator;
import com.github.howjz.job.operator.debug.DebugOperator;
import com.github.howjz.job.operator.error.AlwaysRetryOperator;
import com.github.howjz.job.operator.error.ErrorData;
import com.github.howjz.job.operator.error.ErrorOperator;
import com.github.howjz.job.operator.error.RetryOperator;
import com.github.howjz.job.operator.execute.ExecuteData;
import com.github.howjz.job.operator.execute.ExecuteOperator;
import com.github.howjz.job.operator.genericjob.GenericJobOperator;
import com.github.howjz.job.operator.join.JoinData;
import com.github.howjz.job.operator.join.JoinOperator;
import com.github.howjz.job.operator.link.LinkData;
import com.github.howjz.job.operator.link.LinkOperator;
import com.github.howjz.job.operator.notify.NotifyData;
import com.github.howjz.job.operator.notify.NotifyOperator;
import com.github.howjz.job.operator.pool.PoolOperator;
import com.github.howjz.job.operator.restart.RestartOperator;
import com.github.howjz.job.operator.then.AllThenOperator;
import com.github.howjz.job.operator.then.ThenData;
import com.github.howjz.job.operator.then.ThenOperator;
import com.github.howjz.job.operator.waiting.WaitingData;
import com.github.howjz.job.operator.waiting.WaitingOperator;
import com.github.howjz.job.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 *  执行器启动流程
 *  1、定义操作器数据
 *  2、定义数据上下文，传入操作器数据
 *  3、定义数据上下文其他数据
 *  4、定义执行器管理器
 *  5、监听程序启动，启动执行器管理器（本身是一个线程）
 * @author zhangjh
 * @date 2020/12/11 9:46
 */
@Configuration
@EnableConfigurationProperties(JobProperties.class)
@Lazy(false)
@EnableAsync
@ConditionalOnExpression("${how.job.enable:false} && '${how.job.type:local}'.equals('local')")
@ConditionalOnMissingBean(GenericJobAutoConfiguration.class)
public class GenericJobAutoConfiguration {

    @Autowired
    private JobProperties jobProperties;

    @Value("${server.port:8080}")
    private String port;

    /**
     * 1.1、定义 状态触发操作数据
     * @return  状态触发操作数据
     */
    @Bean(JobKey.Context.OPERATOR_NOTIFY_DATA)
    public NotifyData operatorNotifyData() {
        return new NotifyData();
    }

    /**
     * 1.2、定义 错误触发操作数据
     * @return  错误触发操作数据
     */
    @Bean(JobKey.Context.OPERATOR_ERROR_DATA)
    public ErrorData operatorErrorData() {
        return new ErrorData();
    }

    /**
     * 1.3、定义 执行触发操作数据
     * @return  执行触发操作数据
     */
    @Bean(JobKey.Context.OPERATOR_EXECUTE_DATA)
    public ExecuteData operatorExecuteData() {
        return new ExecuteData();
    }

    /**
     * 1.4、定义 串联触发操作数据
     * @return  串联触发操作数据
     */
    @Bean(JobKey.Context.OPERATOR_LINK_DATA)
    public LinkData operatorLinkData() {
        return new LinkData();
    }

    /**
     * 1.5、定义 then触发操作数据
     * @return  then触发操作数据
     */
    @Bean(JobKey.Context.OPERATOR_THEN_DATA)
    public ThenData operatorThenData() {
        return new ThenData();
    }

    /**
     * 1.6、定义 join触发操作数据
     * @return  join触发操作数据
     */
    @Bean(JobKey.Context.OPERATOR_JOIN_DATA)
    public JoinData operatorJoinData() {
        return new JoinData();
    }

    /**
     * 1.7、定义 cross触发操作数据
     * @return  cross触发操作数据
     */
    @Bean(JobKey.Context.OPERATOR_CROSS_DATA)
    public CrossData operatorCrossData() {
        return new CrossData();
    }

    /**
     * 1.8、定义 waiting触发操作数据
     * @return  waiting触发操作数据
     */
    @Bean(JobKey.Context.OPERATOR_WAITING_DATA)
    public WaitingData operatorWaitingData() {
        return new WaitingData();
    }

    /**
     * 1.9、定义 数据上下文
     *  注意：
     *      operator 和 dataContext
     *      需要小心互相依赖
     * @return  数据上下文
     */
    @Bean(JobKey.Context.INSTANCE)
    public JobDataContext dataContext() {
        JobDataContext dataContext = new JobDataContext();
        dataContext.setNotifyData(operatorNotifyData());
        dataContext.setErrorData(operatorErrorData());
        dataContext.setExecuteData(operatorExecuteData());
        dataContext.setLinkData(operatorLinkData());
        dataContext.setThenData(operatorThenData());
        dataContext.setJoinData(operatorJoinData());
        dataContext.setCrossData(operatorCrossData());
        dataContext.setWaitingData(operatorWaitingData());
        return dataContext;
    }

    /**
     * 1.10、定义 执行器信息
     * @return  执行器信息
     */
    @Bean(JobKey.Context.EXECUTOR)
    public Executor executor() {
        Executor executor = new Executor();
        executor.setExecutorId(IPUtil.getSiteLocalAddress() + ":" + this.port);
        executor.setDebug(this.jobProperties.getDebug());
        executor.setPool(this.jobProperties.getPool());
        return executor;
    }

    /**
     * 1.11、定义 执行器信息列表
     * @return  执行器信息列表
     */
    @Bean(JobKey.Context.EXECUTORS)
    public Map<String, Executor> executors() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 1.12、定义 作业详情列表
     * @return  作业详情列表
     */
    @Bean(JobKey.Context.JOB_MAP)
    public Map<String, Job> jobMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 1.13、定义 作业任务详情列表
     * @return  作业任务详情列表
     */
    @Bean(JobKey.Context.JOB_TASK_MAP)
    public Map<String, Map<String, Job>> jobTaskMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 1.14、定义 任务队列
     * @return  任务队列
     */
    @Bean(JobKey.Context.TASK_QUEUE)
    public BlockingQueue<String> taskQueue() {
        return new LinkedBlockingQueue<String>();
    }

    /**
     * 1.15、定义 主线程池
     * @return  主线程池
     */
    @Bean(JobKey.Context.EXECUTOR_POOL)
    public ExecutorService executorPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        // 设置核心线程数
        executor.setCorePoolSize(3);
        // 设置最大线程数
        executor.setMaxPoolSize(10);
        // 设置队列容量
        executor.setQueueCapacity(25);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(300);
        // 设置默认线程名称
        executor.setThreadNamePrefix("job-executor-");
        executor.setAwaitTerminationSeconds(60);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor.getThreadPoolExecutor();
    }

    /**
     * 1.16、定义 备用线程池
     * @return  备用线程池
     */
    @Bean(JobKey.Context.SPARE_EXECUTOR_POOL)
    public ExecutorService spareExecutorPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        // 设置核心线程数
        executor.setCorePoolSize(3);
        // 设置最大线程数
        executor.setMaxPoolSize(10);
        // 设置队列容量
        executor.setQueueCapacity(25);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(300);
        // 设置默认线程名称
        executor.setThreadNamePrefix("job-spare-executor-");
        executor.setAwaitTerminationSeconds(60);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor.getThreadPoolExecutor();
    }

    /**
     * 1.17、定义 操作器
     * @return  操作器
     */
    @Bean(JobKey.Context.OPERATORS)
    public List<Operator<?>> operators() {
        JobDataContext dataContext = dataContext();
        ConfigOperator configOperator = new ConfigOperator(dataContext);
        AlwaysRetryOperator alwaysRetryOperator = new AlwaysRetryOperator(dataContext);
        RestartOperator restartOperator = new RestartOperator(dataContext);
        NotifyOperator notifyOperator = new NotifyOperator(dataContext);
        RetryOperator retryOperator = new RetryOperator(dataContext);
        ErrorOperator errorOperator = new ErrorOperator(dataContext);
        LinkOperator linkOperator = new LinkOperator(dataContext);
        ThenOperator thenOperator = new ThenOperator(dataContext);
        AllThenOperator allThenOperator = new AllThenOperator(dataContext);
        JoinOperator joinOperator = new JoinOperator(dataContext);
        GenericJobOperator genericJobOperator = new GenericJobOperator(dataContext);
        PoolOperator poolOperator = new PoolOperator(dataContext);
        WaitingOperator waitingOperator = new WaitingOperator(dataContext);
        CrossOperator crossOperator = new CrossOperator(dataContext);
        ExecuteOperator executeOperator = new ExecuteOperator(dataContext);
        DebugOperator debugOperator = new DebugOperator(dataContext);
        return Arrays.asList(
          configOperator, alwaysRetryOperator, restartOperator, notifyOperator, retryOperator, errorOperator, linkOperator,
          thenOperator, allThenOperator, joinOperator, genericJobOperator, poolOperator, waitingOperator, crossOperator, executeOperator, debugOperator
        );
    }

    /**
     * 2、定义 执行器管理器
     * @return  执行器管理器
     */
    @Bean
    public GenericExecutorManager executorManager() {
        JobDataContext dataContext = dataContext();
        dataContext.setExecutor(executor());
        dataContext.setExecutors(executors());
        dataContext.setJobMap(jobMap());
        dataContext.setJobTaskMap(jobTaskMap());
        dataContext.setTaskQueue(taskQueue());
        dataContext.setExecutorPool(executorPool());
        dataContext.setSpareExecutorPool(spareExecutorPool());
        dataContext.setOperators(operators());
        return new GenericExecutorManager(dataContext());
    }

    /**
     * 执行器开始
     */
    @PostConstruct
    public void init() {
        GenericExecutorManager executorManager = executorManager();
        // 1、定义作业管理器
        JobHelper.init(executorManager);
        // 2、启动任务监听器
        spareExecutorPool().execute(executorManager);
        // 3、设置为已启动
        executor().setInited(true);
    }

    /**
     * 执行器停止
     */
    @PreDestroy
    public void destroy() {
        GenericExecutorManager executorManager = executorManager();
        executorManager.destroy();
    }
}
