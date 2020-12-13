package com.github.howjz.job.samples;

import com.github.howjz.job.Job;
import com.github.howjz.job.operator.execute.Executable;
import com.github.howjz.job.operator.then.Thenable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Random;

/**
 * @author zhangjh
 * @date 2020/12/12 2:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TestTask implements Executable, Thenable {

    private static final long serialVersionUID = 4613472702279565420L;

    private Integer param;

    private List<Integer> result;

    public TestTask(Integer param, List<Integer> result) {
        this.param = param;
        this.result = result;
    }

    @Override
    public void execute(Job job, Job task) throws Exception {
        int j = new Random().nextInt(1000);
        task.setResult(String.valueOf(j));
        Thread.sleep(j);
        result.add(j);
        System.out.println(String.format("当前线程：[%s], 随机数任务 [%s] 完成", Thread.currentThread().getName(), this.param));
    }

    @Override
    public void then(Job job) throws Exception {
        System.out.println(String.format("当前线程：[%s], THEN：随机数个数： [%s]", Thread.currentThread().getName(), result.size()));
        System.out.println(String.format("当前线程：[%s], THEN：随机数总和： [%s] = %s", Thread.currentThread().getName(),
                StringUtils.join(result, " + "), result.stream().mapToInt(Integer::intValue).sum()));
    }
}
