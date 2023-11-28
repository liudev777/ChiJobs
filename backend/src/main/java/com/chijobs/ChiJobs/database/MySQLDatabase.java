package com.chijobs.ChiJobs.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.chijobs.ChiJobs.Job;

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

        addQueryTime(keywords, currentTimestamp);

        for (Map.Entry<String, Map<String, String>> entry : jobList.entrySet()) {
            Map<String, String> jobDetails = entry.getValue();
            String jobId = jobDetails.get("jobId");
            addJob(jobId, jobDetails.get("title"), jobDetails.get("company"), jobDetails.get("location"),
                    jobDetails.get("description"), keywords);
        }

        // // 3. Link the job with the query in query_jobs table
        // for (String jobId : jobList.keySet()) {
        // addQueryAndJob(keywords, jobId);
        // }
    }

    public static void addScrapedJobsToDatabase(Map<String, Map<String, String>> scrapedJobs, String query)
            throws SQLException {
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

    public static void addJob(String jobId, String jobTitle, String company, String location, String description,
            String query) throws SQLException {
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

    public static List<String> getTopQueries() throws SQLException {
        String sql = "SELECT query FROM query_time ORDER BY last_used DESC LIMIT 3";
        List<String> topQueries = new ArrayList<>();

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                topQueries.add(rs.getString("query"));
            }
        }
        return topQueries;
    }

    public static String addUser(String firsName, String lastName, String email, String password) throws SQLException {
        String userSql = "INSERT INTO Users (first_name, last_name, email, password) VALUES (?, ?, ?, ?) ";

        try (Connection conn = connect(); PreparedStatement userStmt = conn.prepareStatement(userSql)) {

            // Add user
            userStmt.setString(1, firsName);
            userStmt.setString(2, lastName);
            userStmt.setString(3, email);
            userStmt.setString(4, password);
            userStmt.executeUpdate();
            return "User Added Successful";
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

    public static Boolean getUser(String email, String password) throws SQLException {
        String userSql = "select * from users where email='" + email + "' and password='" + password + "' ;";

        try (Connection conn = connect(); PreparedStatement userStmt = conn.prepareStatement(userSql)) {

            // Get user
            Boolean userFound = false;
            ResultSet rs = userStmt.executeQuery();
            while (rs.next()) {
                userFound = true;
            }
            return userFound;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static String addApplication(String jobid, String email) throws SQLException {
        String appSql = "INSERT INTO JobApplication (email, JobID) VALUES (?, ?) ";

        try (Connection conn = connect(); PreparedStatement userStmt = conn.prepareStatement(appSql)) {

            // Add user
            userStmt.setString(1, email);
            userStmt.setString(2, jobid);
            userStmt.executeUpdate();
            return "Job application Added Successful";
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

    public static Job getJob(String jobid) throws SQLException {
        String jobSql = "select * from jobs where job_id='" + jobid + "';";

        try (Connection conn = connect(); PreparedStatement jobStmt = conn.prepareStatement(jobSql)) {

            // Get job
            Job job = new Job("", "", "", "", "");
            ResultSet rs = jobStmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("job_id");
                String title = rs.getString("job_title");
                String company = rs.getString("company");
                String location = rs.getString("location");
                String description = rs.getString("description");
                job = new Job(id, title, company, location, description);
            }
            return job;
        } catch (Exception e) {
            System.out.println(e);
            return new Job("", "", "", "", "");
        }
    }

    public static Boolean getApplication(String email, String jobid) throws SQLException {
        String userSql = "select * from JobApplication where email='" + email + "' and JobID='" + jobid + "' ;";

        try (Connection conn = connect(); PreparedStatement userStmt = conn.prepareStatement(userSql)) {

            // Get application
            Boolean applicationFound = false;
            ResultSet rs = userStmt.executeQuery();
            while (rs.next()) {
                applicationFound = true;
            }
            return applicationFound;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static List<String> findAllTitles() throws SQLException {
        String jobTitleSql = "SELECT job_title FROM jobs GROUP BY job_title;";
        List<String> jobTitles = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement titleStmt = conn.prepareStatement(jobTitleSql)) {

            ResultSet rs = titleStmt.executeQuery();

            while (rs.next()) {
                String jobTitle = rs.getString("job_title");
                jobTitles.add(jobTitle);
            }

            return jobTitles;
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    public static String addBookmark(String email, String jobId) throws SQLException {
        String bookmarkSql = "INSERT INTO bookmarked_jobs (user_id, job_id) SELECT u.user_id, ? FROM users u WHERE u.email = ?";

        try (Connection conn = connect(); PreparedStatement bookmarkStmt = conn.prepareStatement(bookmarkSql)) {

            // Set parameters
            bookmarkStmt.setString(1, jobId);
            bookmarkStmt.setString(2, email);

            // Execute update
            int rowsAffected = bookmarkStmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Job bookmarked successfully";
            } else {
                return "Failed to bookmark job";
            }
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }


    public static List<Job> getBookmarkedJobs(String email) throws SQLException {
        List<Job> bookmarkedJobs = new ArrayList<>();
        String sql = "SELECT j.job_id, j.job_title, j.company, j.location, j.description " +
                "FROM bookmarked_jobs bj " +
                "JOIN jobs j ON bj.job_id = j.job_id " +
                "JOIN users u ON bj.user_id = u.user_id " +
                "WHERE u.email = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String jobid = rs.getString("job_id");
                    String title = rs.getString("job_title");
                    String company = rs.getString("company");
                    String location = rs.getString("location");
                    String description = rs.getString("description");
                    bookmarkedJobs.add(new Job(jobid, title, company, location, description));
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getBookmarkedJobs: " + e.getMessage());
            throw e; // Rethrow the exception to handle it externally
        }

        return bookmarkedJobs;
    }

    public static List<Job> getAppliedJobs(String email) throws SQLException {
        List<Job> appliedJobs = new ArrayList<>();
        String sql = "SELECT j.job_id, j.job_title, j.company, j.location, j.description " +
                "FROM applied_jobs aj " +
                "JOIN jobs j ON aj.job_id = j.job_id " +
                "JOIN users u ON aj.user_id = u.user_id " +
                "WHERE u.email = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String jobid = rs.getString("job_id");
                    String title = rs.getString("job_title");
                    String company = rs.getString("company");
                    String location = rs.getString("location");
                    String description = rs.getString("description");
                    appliedJobs.add(new Job(jobid, title, company, location, description));
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getAppliedJobs: " + e.getMessage());
            throw e; 
        }

        return appliedJobs;
    }

    public static List<Job> getLastXJobs(int numberOfJobs) throws SQLException {
        List<Job> lastXJobs = new ArrayList<>();
        String sql = "SELECT job_id, job_title, company, location, description " +
                     "FROM jobs " +
                     "ORDER BY RAND() " + 
                     "LIMIT ?"; 

        try (Connection conn = connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, numberOfJobs);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String jobid = rs.getString("job_id");
                    String title = rs.getString("job_title");
                    String company = rs.getString("company");
                    String location = rs.getString("location");
                    String description = rs.getString("description");
                    lastXJobs.add(new Job(jobid, title, company, location, description));
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getLastXJobs: " + e.getMessage());
            throw e;
        }

        return lastXJobs;
    }


}
