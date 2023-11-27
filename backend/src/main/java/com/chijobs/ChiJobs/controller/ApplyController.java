package com.chijobs.ChiJobs.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chijobs.ChiJobs.Job;
import com.chijobs.ChiJobs.database.MySQLDatabase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class ApplyController {

    @PostMapping("/apply")
    public ResponseEntity<String> Apply(@RequestBody Job job, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            String email = session.getAttribute("email").toString();
            String message = MySQLDatabase.addApplication(job.getJobid(), email);
            if (message.equals("Job application Added Successful")) {
                return new ResponseEntity<>("Added Successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error", HttpStatus.FORBIDDEN);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/getJob")
    public ResponseEntity<Job> getJob(@RequestBody Job job, HttpServletRequest request) {
        try {
            Job jobresult = MySQLDatabase.getJob(job.getJobid());
            return new ResponseEntity<>(jobresult, HttpStatus.OK);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/checkApplication")
    public ResponseEntity<Boolean> getApplication(@RequestBody Job job, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            String email = session.getAttribute("email").toString();
            Boolean found = MySQLDatabase.getApplication(email, job.getJobid());
            if (found == true) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getJobTitles")
    public List<String> getJobTitles() {
        try {
            return MySQLDatabase.findAllTitles();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }


    }

    @GetMapping("/getBookmarkedJobs")
    public ResponseEntity<List<Job>> getBookmarkedJobs(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            String email = session.getAttribute("email").toString();
            List<Job> bookmarkedJobs = MySQLDatabase.getBookmarkedJobs(email);
            return new ResponseEntity<>(bookmarkedJobs, HttpStatus.OK);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @PostMapping("/bookmarkJob")
    public ResponseEntity<String> bookmarkJob(@RequestBody Job job, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            String email = session.getAttribute("email").toString();
            String message = MySQLDatabase.addBookmark(email, job.getJobid());
            if (message.equals("Job bookmarked successfully")) {
                return new ResponseEntity<>("Job bookmarked successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error in bookmarking job", HttpStatus.FORBIDDEN);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
