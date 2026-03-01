package com.northwind.ordersummaryetl.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job orderSummaryJob;
    @PostMapping("/run-etl")
    public String runJob(){
        try{
            JobParameters params = new JobParametersBuilder()
                    .addLong("time",System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(orderSummaryJob, params);
            return "ETL Job completed successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Job execution failed: " + e.getMessage();
        }
    }
}
