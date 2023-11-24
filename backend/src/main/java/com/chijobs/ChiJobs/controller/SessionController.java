package com.chijobs.ChiJobs.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class SessionController {

    @GetMapping("/check-session")
    public ResponseEntity<Map<String, Boolean>> checkSession(HttpServletRequest request) {
        Map<String, Boolean> response = new HashMap<>();

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("email") != null) {
            // Session is valid
            response.put("sessionValid", true);
        } else {
            // Session is not valid
            response.put("sessionValid", false);
        }

        return ResponseEntity.ok(response);
    }
}