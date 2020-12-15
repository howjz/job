package com.github.howjz.job.samples;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.operator.config.ConfigBean;
import com.github.howjz.job.operator.error.ErrorPolicy;
import com.github.howjz.job.operator.pool.PoolBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjh
 * @date 2020/12/11 9:56
 */
@Slf4j
@Service
public class JobTestService {

    /**
     * 测试规定线程池
     * @return
     * @throws Exception
     */
    public Job testPoolJob() throws Exception {
        // 用两个耗时很久的任务把公用线程池占满
        JobHelper.createJob()
                .numTask(2, i -> ((job, task) -> {
                    Thread.sleep(10000);
                }))
                .start();

        return JobHelper.createJob()
                .numTask(10, i -> ((job, task) -> {
                    Thread.sleep(new Random().nextInt(1000));
                    System.out.println("公用线程池任务 " + i + " 完成");
                }))
                // 公用线程池，会等待上面的执行完再有可用执行
                // 自身线程池，会根据当前作业的线程数执行
                .pool(PoolBean.PRIVATE(3))
                .start();
    }

    /**
     * 测试错误重试
     * 注意：和错误策略冲突，error方法虽然也会调用到，但是返回的策略无效
     * 例子：随机生成 20 个 1000 以内的数，如果小于 500 则抛出异常，每个任务重试3次
     * @return
     * @throws Exception
     */
    public Job testErrorRetryJob() throws Exception {
        int nums = 1;
        int max = 1000;
        List<Integer> result = new ArrayList<>(0);
        Map<Integer, Integer> retryCount = new ConcurrentHashMap<>();
        return JobHelper.createJob()
                .config(ConfigBean.builder()
                        .retry(3)
                        .build())
                .numTask(nums, i -> ((job, task) -> {
                    int num = 400;
                    if (!retryCount.containsKey(i)) {
                        retryCount.put(i, 0);
                    }
                    retryCount.put(i, retryCount.get(i) + 1);
                    if (num < max / 2) {
                        throw new RuntimeException(String.format("任务 [%s] 生成的数字 [%s] 小于 [%s]", i, num, max / 2));
                    }
                    result.add(num);
                }))
                .error(((task, exception) -> ErrorPolicy.SKIP))
                .allThen((job) -> {
                    System.out.printf("总共生成了 [%s] 位数： [%s]%n", result.size(), result);
                    System.out.println(retryCount);
                })
                .start();
    }

    /**
     * 测试错误策略
     *  ErrorPolicy.SKIP    发生错误时，未执行的任务跳过
     *  ErrorPolicy.KEEP    发生错误时，未执行的任务继续执行
     * @return
     * @throws Exception
     */
    public Job testErrorJob() throws Exception {
        return JobHelper.createJob()
                .numTask(10, i -> ((job, task) -> {
                    Thread.sleep(new Random().nextInt(1000));
                    System.out.println("TASK：" + System.currentTimeMillis());
                    int j = 100 /0;
                }))
                .error(((task, exception) -> ErrorPolicy.SKIP))
                .allThen((job) -> {
                    System.out.println("THEN：" + System.currentTimeMillis());
                })
                .start();
    }

    /**
     * 1、如果没有join，任务21、任务22 会直接完成，任务31 会优先于任务11完成
     * 2、如果有join，任务21、任务22 会 等待 任务11 完成，任务31 会 等待 任务21、任务22 完成
     * @return
     * @throws Exception
     */
    public Job testJoinJob() throws Exception {
        return JobHelper
                .createJob()
                .addTask(((parent, currentTask) -> {
                    Thread.sleep(1500);
                    System.out.println("任务11完成");
                }))
                .then((job) -> {
                    System.out.println("任务组1完成");
                })
                .then((job) -> {
                    System.out.println("清理1");
                })
                .join()
                .addTask(((parent, currentTask) -> {
                    System.out.println("任务21完成");
                }))
                .addTask(((parent, currentTask) -> {
                    System.out.println("任务22完成");
                }))
                .then((job) -> {
                    System.out.println("任务组2完成");
                })
                .join()
                .addTask(((parent, currentTask) -> {
                    Thread.sleep(500);
                    System.out.println("任务31完成");
                }))
                .then((job) -> {
                    System.out.println("任务组3完成");
                })
                .allThen((job) -> {
                    System.out.println("所有任务完成");
                })
                .start();
    }

    /**
     * 测试匿名link作业
     * @return
     * @throws Exception
     */
    public Job testAnonymousLinkJob() throws Exception {
        System.out.println(String.format("当前线程：[%s]", Thread.currentThread().getName()));
        int nums = 10;
        List<Integer> result = new ArrayList<>(nums);
        Job randomNumberJob = JobHelper.createJob()
                .numTask(nums, i -> (parent, currentTask) -> {
                    int j = new Random().nextInt(1000);
                    Thread.sleep(j);
                    currentTask.setResult(String.valueOf(j));
                    result.add(j);
                    System.out.println(String.format("当前线程：[%s], 随机数任务 [%s] 完成", Thread.currentThread().getName(), i));
                })
                .then((job) -> {
                    System.out.println(String.format("当前线程：[%s], THEN：随机数个数： [%s]", Thread.currentThread().getName(), result.size()));
                    System.out.println(String.format("当前线程：[%s], THEN：随机数总和： [%s] = %s", Thread.currentThread().getName(),
                            StringUtils.join(result, " + "), result.stream().mapToInt(Integer::intValue).sum()));
                });
        return JobHelper.createJob()
                .addTask(randomNumberJob)
                .then((job) -> {
                    System.out.println(String.format("当前线程：[%s], THEN：子任务1 - 随机数作业完成", Thread.currentThread().getName()));
                })
                .addTask(((job, task) -> {
                    Thread.sleep(500);
                    System.out.println(String.format("当前线程：[%s], 子任务2 - 注意该子任务，是和上面的作业同级的，当前会比上面的作业优先完成", Thread.currentThread().getName()));
                }))
                .then((job) -> {
                    System.out.println(String.format("当前线程：[%s], THEN：子任务2完成", Thread.currentThread().getName()));
                })
                .start();
    }

}
