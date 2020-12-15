package com.github.howjz.job.controller;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobDataContext;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.samples.JobTestService;
import com.github.howjz.job.samples.file.DownloadFile;
import com.github.howjz.job.samples.file.DownloadJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

/**
 * @author zhangjh
 * @date 2020/12/15 12:54
 */
@RestController
public class JobController {

    @Autowired
    private JobDataContext dataContext;

    @Autowired
    private JobTestService jobTestService;

    @RequestMapping("/jobs")
    public List<Job> jobs() {
        return JobHelper.getJobs();
    }

    @RequestMapping("/status")
    public Job status(@RequestParam("jobId") String jobId) throws Exception {
        return JobHelper.findJob(jobId, true);
    }

    @RequestMapping("/download")
    public Job download(@RequestParam("url") String url) throws Exception {
        DownloadFile file = new DownloadFile(url);
        DownloadJob downloadJob = new DownloadJob();
        downloadJob.setParam(file);
        return downloadJob.ready().start();
    }

    @RequestMapping("/dataContext")
    public JobDataContext dataContext() {
        return dataContext;
    }

    @RequestMapping("/test")
    public Job test() throws Exception {
       return this.jobTestService.simpleJobThenJob();
    }

}
