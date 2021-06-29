package com.example.ejob.data.model;

public class JobApplication {
    String applicationId;
    String position;
    String selfDescription;
    String skills;
    String applicationDate;
    int view;
    ApplicationStatus applicationStatus;

    public JobApplication() {

    }

    public JobApplication(String applicationId, String position, String selfDescription, String skills, String applicationDate, int view, ApplicationStatus applicationStatus) {
        this.applicationId = applicationId;
        this.position = position;
        this.selfDescription = selfDescription;
        this.skills = skills;
        this.applicationDate = applicationDate;
        this.view = view;
        this.applicationStatus = applicationStatus;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSelfDescription() {
        return selfDescription;
    }

    public void setSelfDescription(String selfDescription) {
        this.selfDescription = selfDescription;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
}
