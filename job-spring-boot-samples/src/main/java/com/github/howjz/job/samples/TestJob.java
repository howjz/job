package com.github.howjz.job.samples;

import com.github.howjz.job.Job;
import com.github.howjz.job.operator.genericjob.GenericJob;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangjh
 * @date 2020/12/12 13:49
 */
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class TestJob extends GenericJob<Integer> {

    private static final long serialVersionUID = 5501628500061861103L;

    // 注意，公用的数据最好加上锁，否则数据很容易不同步
    private final List<Integer> result;

    public TestJob(Integer param, List<Integer> result) throws Exception {
        super(param);
        this.result = result;
    }

    @Override
    public void handleCompleteTask(Job job, Job task) throws Exception {
        if (job.isFinish()) {
            System.out.println("作业完成");
        }
    }

    /**
     * 生成 1 - nums 的数
     * 注意 taskParams 生成 的参数不应该复杂，最后是能弄成简单 String
     * @param nums
     * @return
     */
    @Override
    public List<String> generateTaskParams(Integer nums) {
        return Stream.iterate(1, n -> n + 1)
                .limit(nums)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public String execute(Job job, Job task, String param) throws Exception {
        int j = new Random().nextInt(100);
        Thread.sleep(j);
        result.add(j);
        System.out.println(String.format("当前线程：[%s], 随机数任务 [%s] 完成", Thread.currentThread().getName(), Integer.parseInt(param)));
        return String.valueOf(j);
    }

    @Override
    public void then(Job job) throws Exception {
        System.out.println(String.format("当前线程：[%s], THEN：随机数个数： [%s]", Thread.currentThread().getName(), result.size()));
        System.out.println(String.format("当前线程：[%s], THEN：随机数总和： [%s] = %s", Thread.currentThread().getName(),
                StringUtils.join(result, " + "), result.stream().mapToInt(Integer::intValue).sum()));
    }
}
