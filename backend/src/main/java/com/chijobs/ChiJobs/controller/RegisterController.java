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

@RestController
@CrossOrigin
public class RegisterController {
    @PostMapping("/signup")
    public ResponseEntity<String> Signup(@RequestBody User user) {
        try {
            String message = MySQLDatabase.addUser(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPassword());
            if (message.equals("User Added Successful")) {
                return new ResponseEntity<>("Added Successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
