package com.chijobs.ChiJobs;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Job> appliedJobs; // List to store applied jobs
    private List<Job> bookmarkedJobs; // List to store bookmarked jobs

    public User() {
        this.appliedJobs = new ArrayList<>();
        this.bookmarkedJobs = new ArrayList<>();
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.appliedJobs = new ArrayList<>();
        this.bookmarkedJobs = new ArrayList<>();
    }

    // Getter and Setter
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<Job> getAppliedJobs() {
        return appliedJobs;
    }

    public void setAppliedJobs(List<Job> appliedJobs) {
        this.appliedJobs = appliedJobs;
    }

    // Getters and setters for bookmarkedJobs
    public List<Job> getBookmarkedJobs() {
        return bookmarkedJobs;
    }

    public void setBookmarkedJobs(List<Job> bookmarkedJobs) {
        this.bookmarkedJobs = bookmarkedJobs;
    }

}
