package com.example.ejob.ui.user.userjob;

import android.os.Parcel;
import android.os.Parcelable;

public class JobPostingforUser implements Parcelable {
    public static final Creator<JobPostingforUser> CREATOR = new Creator<JobPostingforUser>() {
        @Override
        public JobPostingforUser createFromParcel(Parcel in) {
            return new JobPostingforUser(in);
        }

        @Override
        public JobPostingforUser[] newArray(int size) {
            return new JobPostingforUser[size];
        }
    };
    String jobId;
    String jobTitle;
    String jobDescription;
    String jobLocation;
    String salary;
    String employerName;
    String employerFbID;
    String jobDeadline;
    String jobDateCreated;
    String imageUrl;
    int jobSkills;
    int countView;
    int countLike;
    String jobStatus;
    int updateHistory;
    String jobType;
    String numberneed;
    int numberApplied;
    String empEmail;

    public JobPostingforUser() {

    }

    public JobPostingforUser(String mail, String jobId, String jobTitle, String jobDescription, String jobLocation, String salary, String employerName, String employerFbID, String jobDeadline, String jobDateCreated, String imageUrl, int jobSkills, int countView, int countLike, String jobStatus, int updateHistory, String jobType, String numberneed, int numberApplied) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobLocation = jobLocation;
        this.salary = salary;
        this.employerName = employerName;
        this.employerFbID = employerFbID;
        this.jobDeadline = jobDeadline;
        this.jobDateCreated = jobDateCreated;
        this.imageUrl = imageUrl;
        this.jobSkills = jobSkills;
        this.countView = countView;
        this.countLike = countLike;
        this.jobStatus = jobStatus;
        this.updateHistory = updateHistory;
        this.jobType = jobType;
        this.numberneed = numberneed;
        this.empEmail = mail;
        this.numberApplied = numberApplied;
    }

    public JobPostingforUser(String jobTitle, String employerName, String jobDateCreated) {
        this.jobTitle = jobTitle;
        this.employerName = employerName;
        this.jobDateCreated = jobDateCreated;
    }

    protected JobPostingforUser(Parcel in) {
        jobId = in.readString();
        jobTitle = in.readString();
        jobDescription = in.readString();
        jobLocation = in.readString();
        salary = in.readString();
        employerName = in.readString();
        employerFbID = in.readString();
        jobDeadline = in.readString();
        jobDateCreated = in.readString();
        imageUrl = in.readString();
        jobSkills = in.readInt();
        countView = in.readInt();
        countLike = in.readInt();
        jobStatus = in.readString();
        updateHistory = in.readInt();
        jobType = in.readString();
        numberApplied = in.readInt();
    }

    public static Creator<JobPostingforUser> getCREATOR() {
        return CREATOR;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getNumberneed() {
        return numberneed;
    }

    public void setNumberneed(String numberneed) {
        this.numberneed = numberneed;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobId);
        dest.writeString(jobTitle);
        dest.writeString(jobDescription);
        dest.writeString(jobLocation);
        dest.writeString(salary);
        dest.writeString(employerName);
        dest.writeString(employerFbID);
        dest.writeString(jobDeadline);
        dest.writeString(jobDateCreated);
        dest.writeString(imageUrl);
        dest.writeInt(jobSkills);
        dest.writeInt(countView);
        dest.writeInt(countLike);
        dest.writeString(jobStatus);
        dest.writeInt(updateHistory);
        dest.writeString(jobType);
        dest.writeInt(numberApplied);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getEmployerFbID() {
        return employerFbID;
    }

    public void setEmployerFbID(String employerFbID) {
        this.employerFbID = employerFbID;
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
