package com.example.ejob.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Employer implements Parcelable {
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


    protected Employer(Parcel in) {
        employerId = in.readString();
        employerName = in.readString();
        employerLocation = in.readString();
    }

    public static final Creator<Employer> CREATOR = new Creator<Employer>() {
        @Override
        public Employer createFromParcel(Parcel in) {
            return new Employer(in);
        }

        @Override
        public Employer[] newArray(int size) {
            return new Employer[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(employerId);
        dest.writeString(employerName);
        dest.writeString(employerLocation);
    }
}
