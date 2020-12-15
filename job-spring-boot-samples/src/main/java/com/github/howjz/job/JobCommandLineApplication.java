package com.github.howjz.job;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

/**
 * @author zhangjh
 * @date 2020/12/15 16:33
 */
@SpringBootApplication
public class JobCommandLineApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        JobHelper.createJob()
                .numTask(10, i -> ((job, task) -> {
                    int result = new Random().nextInt(1000);
                    Thread.sleep(result);
                    task.setResult(result);
                }))
                .then((job -> {
                    System.out.println("作业组1完成");
                }))
                .join()
                .numTask(5, i -> ((job, task) -> {
                    int result = new Random().nextInt(500);
                    Thread.sleep(result);
                    task.setResult(result);
                }))
                .then((job -> {
                    System.out.println("作业组2完成");
                }))
                .join()
                .numTask(3, i -> ((job, task) -> {
                    int result = new Random().nextInt(500);
                    Thread.sleep(result);
                    task.setResult(result);
                }))
                .then((job -> {
                    System.out.println("作业组3完成");
                }))
                .allThen((job -> {
                    System.out.println("作业完成");
                }))
                .start();
    }
}
