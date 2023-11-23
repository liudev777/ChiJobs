package com.chijobs.ChiJobs.controller;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
            Boolean found = MySQLDatabase.getUser(email, job.getJobid());
            if (found == true) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
