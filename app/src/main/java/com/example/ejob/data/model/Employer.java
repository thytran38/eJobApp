package com.example.ejob.data.model;

public class Employer {
    String employerId;
    String employerName;
    Industry industry;
    String employerLocation;
    JobPostHistory jobPostHistory;

    public Employer() {

    }

    public Employer(String employerId, String employerName, Industry industry, String employerLocation, JobPostHistory jobPostHistory) {
        this.employerId = employerId;
        this.employerName = employerName;
        this.industry = industry;
        this.employerLocation = employerLocation;
        this.jobPostHistory = jobPostHistory;
    }


    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public String getEmployerLocation() {
        return employerLocation;
    }

    public void setEmployerLocation(String employerLocation) {
        this.employerLocation = employerLocation;
    }

    public JobPostHistory getJobPostHistory() {
        return jobPostHistory;
    }

    public void setJobPostHistory(JobPostHistory jobPostHistory) {
        this.jobPostHistory = jobPostHistory;
    }
}
