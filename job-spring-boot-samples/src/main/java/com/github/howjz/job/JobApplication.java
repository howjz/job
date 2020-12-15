package com.github.howjz.job;

import com.github.howjz.job.samples.JobTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangjh
 * @date 2020/12/11 9:50
 */
@SpringBootApplication
public class JobApplication {

    @Autowired
    private JobTestService jobTestService;

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }


}

