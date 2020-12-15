package com.github.howjz.job;

import com.github.howjz.job.samples.file.DownloadFile;
import com.github.howjz.job.samples.file.DownloadJob;
import com.github.howjz.job.samples.JobTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangjh
 * @date 2020/12/11 9:50
 */
@SpringBootApplication
public class JobApplication implements CommandLineRunner {

    @Autowired
    private JobTestService jobTestService;

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("等待下载完成");
        JobHelper.createJob()
                .addTask(new DownloadJob(new DownloadFile("http://down.sandai.net/thunder9/Thunder9.1.40.898.exe")))
                .addTask(new DownloadJob(new DownloadFile("https://download.jetbrains.8686c.com/idea/ideaIU-2020.3.exe")))
                .start()
                .waiting();
        System.out.println("等待下载结束");
    }

}

