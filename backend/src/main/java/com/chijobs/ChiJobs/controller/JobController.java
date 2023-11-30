package com.chijobs.ChiJobs.controller;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chijobs.ChiJobs.Job;
import com.chijobs.ChiJobs.database.MySQLDatabase;
import com.chijobs.ChiJobs.model.IndeedScrapper;
import com.chijobs.ChiJobs.model.Recommender;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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

    @GetMapping("/recommendJobs")
    public String recommendJobs(@RequestParam String userEmail) {

        try {
            List<Job> appliedJobs = MySQLDatabase.getAppliedJobs(userEmail);
            String appliedJobTitles = "";
            for (Job job : appliedJobs) {
                appliedJobTitles += (job.getTitle() + ": " + job.getJobid() + ',');
            }
            List<Job> sampleJobs = MySQLDatabase.getLastXJobs(30);
            String sampleJobTitles = "";
            for (Job job : sampleJobs) {
                sampleJobTitles += (job.getTitle() + ": " + job.getJobid() + ',');
            }
            List<String> recommendedJobIds = Recommender.sendChatRequest(appliedJobTitles, sampleJobTitles);

            Set<String> idSet = new HashSet<>(recommendedJobIds);
            List<Job> recommendedJobs = sampleJobs.stream()
                    .filter(job -> idSet.contains(job.getJobid()))
                    .collect(Collectors.toList());

            String jsonString;
            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writeValueAsString(recommendedJobs);
            return jsonString;

        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }

    @GetMapping("/getjobsApplied")
    public List<Job> getAppliedJobs(HttpServletRequest request) throws SQLException {
        HttpSession session = request.getSession(false);
        String email = session.getAttribute("email").toString();
        return MySQLDatabase.getJobsApplied(email);
    }

    @GetMapping("/getJobsBookmarked")
    public List<Job> getBookmarkedJobs(HttpServletRequest request) throws SQLException {
        HttpSession session = request.getSession(false);
        String email = session.getAttribute("email").toString();
        return MySQLDatabase.getBookmarkedJobs(email);
    }

}