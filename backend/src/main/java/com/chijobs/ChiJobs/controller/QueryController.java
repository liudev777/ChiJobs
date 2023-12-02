package com.chijobs.ChiJobs.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chijobs.ChiJobs.SearchTerm;
import com.chijobs.ChiJobs.database.MySQLDatabase;
import com.chijobs.ChiJobs.model.IndeedScrapper;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@CrossOrigin
public class QueryController {

    @GetMapping("/top-queries")
    public ResponseEntity<List<String>> getTopQueries() {
        try {
            List<String> topQueries = MySQLDatabase.getTopQueries();
            return ResponseEntity.ok(topQueries);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getTrend")
    public String getTrend() {
        try {
            List<SearchTerm> allQueries = MySQLDatabase.getAllQueries();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(allQueries);
            System.out.println(jsonString);
            return jsonString;
        } catch (Exception e) {
            return "";
        }
    }
}
