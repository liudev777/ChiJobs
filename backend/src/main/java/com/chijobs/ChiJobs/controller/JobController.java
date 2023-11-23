package com.chijobs.ChiJobs.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chijobs.ChiJobs.model.IndeedScrapper;

@RestController
@CrossOrigin
public class JobController {

    @GetMapping("/test")
    public String test() {
        return "testing";
    }

    @GetMapping("/searchJobs")
    public String searchJobs(@RequestParam String keyword, @RequestParam String zipcode) {

        IndeedScrapper is = new IndeedScrapper();
        String jobJSON = is.getJobListJSON(keyword, zipcode);
        return jobJSON;
    }

}