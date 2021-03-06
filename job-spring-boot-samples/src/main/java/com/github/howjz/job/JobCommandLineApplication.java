package com.github.howjz.job;

import com.github.howjz.job.samples.JobTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangjh
 * @date 2020/12/15 16:33
 */
@SpringBootApplication
public class JobCommandLineApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }

    @Autowired
    private JobTestService jobTestService;

    @Override
    public void run(String... args) throws Exception {

    }
}
