package com.chijobs.ChiJobs;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Job {

    @JsonProperty("jobId")
    private String jobid;
    private String title;
    private String company;
    private String location;
    private String description;

    public Job(String jobid, String title, String company, String location, String description) {
        this.jobid = jobid;
        this.title = title;
        this.company = company;
        this.location = location;
        this.description = description;
    }

    // Getter and Setter for jobid
    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    // Getter and Setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for company
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    // Getter and Setter for location
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
