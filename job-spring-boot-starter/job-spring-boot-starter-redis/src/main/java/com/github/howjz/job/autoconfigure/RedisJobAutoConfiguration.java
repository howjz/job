package com.github.howjz.job.autoconfigure;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.github.howjz.job.Executor;
import com.github.howjz.job.Job;
import com.github.howjz.job.constant.JobKey;
import com.github.howjz.job.operator.cross.CrossData;
import com.github.howjz.job.operator.cross.CrossType;
import com.github.howjz.job.operator.error.ErrorData;
import com.github.howjz.job.operator.execute.ExecuteData;
import com.github.howjz.job.operator.job.JobData;
import com.github.howjz.job.operator.join.JoinData;
import com.github.howjz.job.operator.link.LinkData;
import com.github.howjz.job.operator.notify.NotifyData;
import com.github.howjz.job.operator.then.ThenData;
import com.github.howjz.job.operator.waiting.WaitingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.DefaultRedisZSet;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/15 10:00
 */
@Configuration
@EnableConfigurationProperties(JobProperties.class)
@Lazy(false)
@EnableAsync
@ConditionalOnExpression("${how.job.enable:false} && '${how.job.type:local}'.equals('redis')")
public class RedisJobAutoConfiguration extends GenericJobAutoConfiguration {

    @Autowired(required = false)
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * object序列化，其他缓存的直接用JDK自带的序列化
     * @return
     */
    @Bean(JobKey.Context.REDIS_OBJECT_TEMPLATE)
    public RedisTemplate<String, Object> redisObjectTemplate(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<Object> jackson2JsonRedisSerializer = RedisSerializer.json();
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * object序列化，其他缓存的直接用JDK自带的序列化
     * @return
     */
    @Bean(JobKey.Context.REDIS_OBJECT_TEMPLATE)
    public RedisTemplate<String, String> redisStringTemplate(){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<String> jackson2JsonRedisSerializer = RedisSerializer.string();
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Override
    public JobData operatorJobData() {
        JobData jobData = new JobData();
        jobData.setJobMap(new DefaultRedisMap<String, Job>(JobKey.Context.OPERATOR_JOB_DATA_JOB_MAP, redisObjectTemplate()));
        jobData.setJobTaskRelationMap(new DefaultRedisMap<String, Set<String>>(JobKey.Context.OPERATOR_JOB_DATA_JOB_TASK_RELATION_MAP, redisObjectTemplate()));
        jobData.setTaskMap(new DefaultRedisMap<String, Job>(JobKey.Context.OPERATOR_JOB_DATA_TASK_MAP, redisObjectTemplate()));
        return jobData;
    }

    @Override
    public NotifyData operatorNotifyData() {
        NotifyData notifyData = new NotifyData();
        notifyData.setNotifyMap(new DefaultRedisMap<String, Integer>(JobKey.Context.OPERATOR_NOTIFY_DATA_NOTIFY_MAP, redisObjectTemplate()));
        return notifyData;
    }

    @Override
    public ErrorData operatorErrorData() {
        ErrorData errorData = new ErrorData();
        errorData.setAlwaysRetrySet(new DefaultRedisZSet<String>(JobKey.Context.OPERATOR_ERROR_DATA_ALWAYS_RETRY_SET, redisStringTemplate()));
        errorData.setErrorableMap(new ConcurrentHashMap<>());
        return errorData;
    }

    @Override
    public ExecuteData operatorExecuteData() {
        ExecuteData executeData = new ExecuteData();
        executeData.setExecutableMap(new ConcurrentHashMap<>());
        executeData.setExecuteRunnableMap(new ConcurrentHashMap<>());
        return executeData;
    }

    @Override
    public LinkData operatorLinkData() {
        LinkData linkData = new LinkData();
        linkData.setNotifyMap(new DefaultRedisMap<String, List<String>>(JobKey.Context.OPERATOR_LINK_DATA_NOTIFY_MAP, redisObjectTemplate()));
        linkData.setWaitMap(new DefaultRedisMap<String, List<String>>(JobKey.Context.OPERATOR_LINK_DATA_WAIT_MAP, redisObjectTemplate()));
        return linkData;
    }

    @Override
    public ThenData operatorThenData() {
        ThenData thenData = new ThenData();
        thenData.setAllThenMap(new ConcurrentHashMap<>());
        thenData.setNotifyMap(new DefaultRedisMap<String, List<String>>(JobKey.Context.OPERATOR_THEN_DATA_NOTIFY_MAP, redisObjectTemplate()));
        thenData.setWaitMap(new DefaultRedisMap<String, List<String>>(JobKey.Context.OPERATOR_THEN_DATA_WAIT_MAP, redisObjectTemplate()));
        thenData.setThenBeanMap(new ConcurrentHashMap<>());
        return thenData;
    }

    @Override
    public JoinData operatorJoinData() {
        JoinData joinData = new JoinData();
        joinData.setNotifyMap(new DefaultRedisMap<String, List<String>>(JobKey.Context.OPERATOR_JOIN_DATA_NOTIFY_MAP, redisObjectTemplate()));
        joinData.setWaitMap(new DefaultRedisMap<String, List<String>>(JobKey.Context.OPERATOR_JOIN_DATA_WAIT_MAP, redisObjectTemplate()));
        return joinData;
    }

    @Override
    public CrossData operatorCrossData() {
        CrossData crossData = new CrossData();
        crossData.setCrossTypeMap(new DefaultRedisMap<String, CrossType>(JobKey.Context.OPERATOR_CROSS_DATA_CROSS_TYPE_MAP, redisObjectTemplate()));
        return crossData;
    }

    @Override
    public WaitingData operatorWaitingData() {
        WaitingData waitingData = new WaitingData();
        waitingData.setJobLock(new ConcurrentHashMap<>());
        return waitingData;
    }

    @Override
    public Map<String, Executor> executors() {
        return new DefaultRedisMap<String, Executor>(JobKey.Context.EXECUTORS, redisObjectTemplate());
    }


    @Override
    public BlockingQueue<String> taskQueue() {
        return new DefaultRedisList<String>(JobKey.Context.TASK_QUEUE, redisStringTemplate());
    }

}
