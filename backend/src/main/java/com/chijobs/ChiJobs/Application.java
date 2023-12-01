package com.chijobs.ChiJobs;

public class Application {
    private String firstName;
    private String lastName;
    private String email;
    private int applicationId;
    private String jobId;

    public Application(String firstName, String lastName, String email, int applicationId, String jobId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.applicationId = applicationId;
        this.jobId = jobId;
    }

    // Getter for firstName
    public String getFirstName() {
        return firstName;
    }

    // Setter for firstName
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter for lastName
    public String getLastName() {
        return lastName;
    }

    // Setter for lastName
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for applicationId
    public int getApplicationId() {
        return applicationId;
    }

    // Setter for applicationId
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    // Getter for jobId
    public String getJobId() {
        return jobId;
    }

    // Setter for jobId
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
