package com.github.howjz.job;

import com.github.howjz.job.manager.GenericExecutorManager;
import com.github.howjz.job.operator.execute.Executable;
import com.github.howjz.job.util.IDGenerateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 *  作业上下文：主要是做 queueManager、queueExecutorThread交换数据用，防止互相依赖
 * @author zhangjh
 * @date 2020/8/29 19:29
 */
@Slf4j
public class JobHelper {

    // 全局雪花ID
    public static final IDGenerateUtil IdWorker = new IDGenerateUtil(0,0);

    // 执行管理器
    public static GenericExecutorManager manager;

    /**
     * 使用前必须init
     * @param manager   执行管理器
     */
    public static void init(GenericExecutorManager manager) {
        JobHelper.manager = manager;
    }

    public static Executor getExecutor() {
        checkInit();
        return manager.getExecutor();
    }

    public static Map<String, Executor> getExecutors() {
        checkInit();
        return manager.getExecutors();
    }

    public static List<Job> getJobs() {
        checkInit();
        return manager.getJobs();
    }

    public static Job findJob(String jobId) throws Exception {
        checkInit();
        return manager.findJob(jobId, false);
    }

    public static Job findJob(String jobId, boolean detail) throws Exception {
        checkInit();
        return manager.findJob(jobId, detail);
    }

    public static Job createJob(Executable executable) throws Exception {
        checkInit();
        return manager.createJob(null, executable);
    }

    public static Job createJob() throws Exception {
        checkInit();
        return manager.createJob(null, (parent, currentTask) -> {});
    }

    public static Job findOrCreateJob(String jobId, Executable executable) throws Exception {
        checkInit();
        return manager.findOrCreateJob(jobId, executable);
    }

    public static Job findOrCreateJob(String jobId) throws Exception {
        checkInit();
        return manager.findOrCreateJob(jobId, (parent, currentTask) -> {});
    }

    public static void startJob(Job job) throws Exception {
        checkInit();
        manager.startJob(job);
    }

    public static void pauseJob(Job job) throws Exception {
        checkInit();
        manager.pauseJob(job);
    }

    public static void stopJob(Job job) throws Exception {
        checkInit();
        manager.stopJob(job);
    }

    public static void removeJob(Job job) throws Exception {
        checkInit();
        manager.removeJob(job);
    }

    public static void waitingJob(Job jobExecutor) throws Exception {
        checkInit();
        manager.waitingJob(jobExecutor);
    }

    private static void checkInit() {
        if (manager == null) {
            throw new RuntimeException("how-job 执行器未启动！！！");
        }
    }

}
