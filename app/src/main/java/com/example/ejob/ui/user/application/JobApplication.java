package com.example.ejob.ui.user.application;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ejob.data.model.ApplicationStatus;

public class JobApplication implements Parcelable {
    String applicationId;
    String position;
    String selfDescription;
    String skills;
    String applicationDate;
    int view;
    String cvitaeLink;
    String coverletterLink;
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

    protected JobApplication(Parcel in) {
        applicationId = in.readString();
        position = in.readString();
        selfDescription = in.readString();
        skills = in.readString();
        applicationDate = in.readString();
        view = in.readInt();
    }

    public static final Creator<JobApplication> CREATOR = new Creator<JobApplication>() {
        @Override
        public JobApplication createFromParcel(Parcel in) {
            return new JobApplication(in);
        }

        @Override
        public JobApplication[] newArray(int size) {
            return new JobApplication[size];
        }
    };

    public String getCvitaeLink() {
        return cvitaeLink;
    }

    public void setCvitaeLink(String cvitaeLink) {
        this.cvitaeLink = cvitaeLink;
    }

    public String getCoverletterLink() {
        return coverletterLink;
    }

    public void setCoverletterLink(String coverletterLink) {
        this.coverletterLink = coverletterLink;
    }

    public static Creator<JobApplication> getCREATOR() {
        return CREATOR;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(applicationId);
        dest.writeString(position);
        dest.writeString(selfDescription);
        dest.writeString(skills);
        dest.writeString(applicationDate);
        dest.writeInt(view);
    }
}
