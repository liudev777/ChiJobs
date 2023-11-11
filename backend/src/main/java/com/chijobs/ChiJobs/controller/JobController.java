package com.chijobs.ChiJobs.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.chijobs.ChiJobs.model.IndeedScrapper;


@RestController
public class JobController {

    @GetMapping("/test")
    public String test() {
        return "testing";
    }

     @GetMapping("/searchJobs")
    public String searchJobs(@RequestParam String keyword, @RequestParam String zipcode) {
        // Your logic to handle the request
        // For example, you might call a service to get jobs based on these parameters
        IndeedScrapper is = new IndeedScrapper();
        String jobJSON = is.getJobListJSON(keyword, zipcode);
        return jobJSON;
    }
}