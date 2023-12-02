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

import com.chijobs.ChiJobs.Application;
import com.chijobs.ChiJobs.Job;
import com.chijobs.ChiJobs.SearchTerm;

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

            jobStmt.setString(1, jobId);
            jobStmt.setString(2, jobTitle);
            jobStmt.setString(3, company);
            jobStmt.setString(4, location);
            jobStmt.setString(5, description);
            jobStmt.executeUpdate();

            queryJobStmt.setString(1, query);
            queryJobStmt.setString(2, jobId);
            queryJobStmt.executeUpdate();
        }
    }

    public static void addQueryTime(String query, Timestamp timestamp) throws SQLException {
        String sql = "INSERT INTO query_time (query, last_used) VALUES (?, ?) ON DUPLICATE KEY UPDATE last_used = VALUES(last_used), count = count + 1";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, query);
            pstmt.setTimestamp(2, timestamp);
            pstmt.executeUpdate();
        }
    }

    public static List<String> getTopQueries() throws SQLException {
        String sql = "SELECT query FROM query_time ORDER BY count DESC LIMIT 3";
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

    public static List<SearchTerm> getAllQueries() throws SQLException {
        String sql = "SELECT query, count FROM query_time ORDER BY count DESC";
        List<SearchTerm> topQueries = new ArrayList<>();
    
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                String query = rs.getString("query");
                String count = rs.getString("count");
                topQueries.add(new SearchTerm(query, Integer.parseInt(count)));
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

    public static String removeBookmark(String email, String jobId) throws SQLException {
        String unbookmarkSql = "DELETE FROM bookmarked_jobs WHERE user_id = (SELECT user_id FROM users WHERE email = ?) AND job_id = ?";

        try (Connection conn = connect(); PreparedStatement unbookmarkStmt = conn.prepareStatement(unbookmarkSql)) {
            // Set parameters
            unbookmarkStmt.setString(1, email);
            unbookmarkStmt.setString(2, jobId);

            int rowsAffected = unbookmarkStmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Job unbookmarked successfully";
            } else {
                return "Failed to unbookmark job";
            }
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

    public static boolean checkBookmarkStatus(String email, String jobId) throws SQLException {
        String checkBookmarkStatusSql = "SELECT COUNT(*) FROM bookmarked_jobs WHERE user_id = (SELECT user_id FROM users WHERE email = ?) AND job_id = ?";

        try (Connection conn = connect();
                PreparedStatement checkBookmarkStatusStmt = conn.prepareStatement(checkBookmarkStatusSql)) {
            // Set parameters
            checkBookmarkStatusStmt.setString(1, email);
            checkBookmarkStatusStmt.setString(2, jobId);

            try (ResultSet resultSet = checkBookmarkStatusStmt.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        return false;
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
            throw e; 
        }

        return bookmarkedJobs;
    }

    public static List<Job> getAppliedJobs(String email) throws SQLException {
        List<Job> appliedJobs = new ArrayList<>();
        String sql = "SELECT j.job_id, j.job_title, j.company, j.location, j.description " +
                "FROM JobApplication ja " +
                "JOIN jobs j ON ja.jobID = j.job_id " +
                "JOIN users u ON ja.email = u.email " +
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

    public static List<Job> getJobsApplied(String email) throws SQLException {
        List<Job> appliedJobs = new ArrayList<>();
        String sql = "select j.job_id, j.job_title, j.company, j.location, j.description from JobApplication ja JOIN jobs j ON ja.JobID = j.job_id WHERE ja.email = ?";

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

    public static List<Application> getAllApplications() throws SQLException {
        List<Application> applications = new ArrayList<>();
        String sql = "select u.first_name, u.last_name, u.email, ja.ApplicationID, ja.JobID, ja.ApplicationDate from users u join JobApplication ja ON u.email = ja.email;";

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    int applicationId = rs.getInt("ApplicationID");
                    String jobId = rs.getString("JobID");
                    applications.add(new Application(firstName, lastName, email, applicationId, jobId));
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getAppliedJobs: " + e.getMessage());
            throw e;
        }

        return applications;
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
