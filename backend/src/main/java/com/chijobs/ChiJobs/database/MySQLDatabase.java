package com.chijobs.ChiJobs.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import io.github.cdimascio.dotenv.Dotenv;

public class MySQLDatabase {

    private static Dotenv dotenv = Dotenv.load();
    private static String url = "jdbc:mysql://localhost:3306/chijobs";
    private static String username = dotenv.get("DB_USERNAME");
    private static String password = dotenv.get("DB_PASSWORD");

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void handleJobSQL(String keywords, Map<String, Map<String, String>> jobList) throws SQLException {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
    
        // 1. Add or update query timestamp
        addQueryTime(keywords, currentTimestamp);
    
        // 2. Add scraped jobs to the jobs table
        for (Map.Entry<String, Map<String, String>> entry : jobList.entrySet()) {
            Map<String, String> jobDetails = entry.getValue();
            String jobId = jobDetails.get("jobId");
            addJob(jobId, jobDetails.get("title"), jobDetails.get("company"), jobDetails.get("location"), jobDetails.get("description"), keywords);
        }
    
        // // 3. Link the job with the query in query_jobs table
        // for (String jobId : jobList.keySet()) {
        //     addQueryAndJob(keywords, jobId);
        // }
    }
    
    
    

    public static void addScrapedJobsToDatabase(Map<String, Map<String, String>> scrapedJobs, String query) throws SQLException {
        for (Map.Entry<String, Map<String, String>> entry : scrapedJobs.entrySet()) {
            Map<String, String> jobDetails = entry.getValue();
            String jobId = entry.getKey(); // Assuming the job ID is the entry's key
            String jobTitle = jobDetails.getOrDefault("title", "");
            String company = jobDetails.getOrDefault("company", "");
            String location = jobDetails.getOrDefault("location", "");
            String description = jobDetails.getOrDefault("description", "");

            addJob(jobId, jobTitle, company, location, description, query);
        }
    }

    public static void addJob(String jobId, String jobTitle, String company, String location, String description, String query) throws SQLException {
        String jobSql = "INSERT INTO jobs (job_id, job_title, company, location, description) VALUES (?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "job_title = VALUES(job_title), " +
                        "company = VALUES(company), " +
                        "location = VALUES(location), " +
                        "description = VALUES(description)";
    
        String queryJobSql = "INSERT IGNORE INTO query_jobs (query, job_id) VALUES (?, ?)";
    
        try (Connection conn = connect();
             PreparedStatement jobStmt = conn.prepareStatement(jobSql);
             PreparedStatement queryJobStmt = conn.prepareStatement(queryJobSql)) {
    
            // Add job or update if it already exists
            jobStmt.setString(1, jobId);
            jobStmt.setString(2, jobTitle);
            jobStmt.setString(3, company);
            jobStmt.setString(4, location);
            jobStmt.setString(5, description);
            jobStmt.executeUpdate();
    
            // Link job to query
            queryJobStmt.setString(1, query);
            queryJobStmt.setString(2, jobId);
            queryJobStmt.executeUpdate();
        }
    }
    

    public static void addQueryTime(String query, Timestamp timestamp) throws SQLException {
        String sql = "INSERT INTO query_time (query, last_used) VALUES (?, ?) ON DUPLICATE KEY UPDATE last_used = VALUES(last_used)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, query);
            pstmt.setTimestamp(2, timestamp);
            pstmt.executeUpdate();
        }
    }
}
