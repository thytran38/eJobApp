package com.example.ejob.ui.job;

public class JobPosting {
    String jobId;
    String jobTitle;
    String jobDescription;
    String jobLocation;
    String salary;
    String employerName;
    String jobDeadline;
    String jobDateCreated;
    String imageUrl;
    int jobSkills;
    int countView;
    int countLike;
    boolean jobStatus;
    int updateHistory;

    int numberApplied;

    public JobPosting() {

    }

    public JobPosting(String jobId, String jobTitle, String jobDescription, String jobLocation, String salary, String employerName, String jobDeadline, String jobDateCreated, int jobSkills, int countView, int countLike, boolean jobStatus, int updateHistory) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobLocation = jobLocation;
        this.salary = salary;
        this.employerName = employerName;
        this.jobDeadline = jobDeadline;
        this.jobDateCreated = jobDateCreated;
        this.jobSkills = jobSkills;
        this.countView = countView;
        this.countLike = countLike;
        this.jobStatus = jobStatus;
        this.updateHistory = updateHistory;
    }


    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getJobDeadline() {
        return jobDeadline;
    }

    public void setJobDeadline(String jobDeadline) {
        this.jobDeadline = jobDeadline;
    }

    public String getJobDateCreated() {
        return jobDateCreated;
    }

    public void setJobDateCreated(String jobDateCreated) {
        this.jobDateCreated = jobDateCreated;
    }

    public int getJobSkills() {
        return jobSkills;
    }

    public void setJobSkills(int jobSkills) {
        this.jobSkills = jobSkills;
    }

    public int getCountView() {
        return countView;
    }

    public void setCountView(int countView) {
        this.countView = countView;
    }

    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }

    public boolean isJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(boolean jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getUpdateHistory() {
        return updateHistory;
    }

    public void setUpdateHistory(int updateHistory) {
        this.updateHistory = updateHistory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getNumberApplied() {
        return numberApplied;
    }

    public void setNumberApplied(int numberApplied) {
        this.numberApplied = numberApplied;
    }
}
