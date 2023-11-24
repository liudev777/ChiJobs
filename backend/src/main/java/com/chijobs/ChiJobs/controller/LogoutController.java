package com.chijobs.ChiJobs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class LogoutController {
    @PostMapping("/logout")
    public ResponseEntity<String> invalidate(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        System.out.println("Session ID is : " + session.getId());

        // Invalidate the session
        session.invalidate();

        return new ResponseEntity<>("Logged Out", HttpStatus.OK);
    }
}
