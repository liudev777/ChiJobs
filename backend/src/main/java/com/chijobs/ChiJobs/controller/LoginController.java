package com.chijobs.ChiJobs.controller;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chijobs.ChiJobs.User;
import com.chijobs.ChiJobs.database.MySQLDatabase;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<String> Login(@RequestBody User user, HttpServletRequest request) {
        try {
            Boolean message = MySQLDatabase.getUser(user.getEmail(), user.getPassword());
            if (message == true) {
                // Set user information in session
                request.getSession().setAttribute("email", user.getEmail());
                System.out.println("Login Session ID is : " + request.getSession().getId());
                return new ResponseEntity<>("Authorized", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
