package com.example.ejob.data.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

public class ApplicantModel implements Parcelable {

    String applicantID;
    String applicantFullname;
    String applicantEmail;
    String applicantPhone;
    String applicantSocialmedia;
    String applicantUniversity;
    String applicantAddress;

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public ApplicantModel(){

    }

    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public String getApplicantFullname() {
        return applicantFullname;
    }

    public void setApplicantFullname(String applicantFullname) {
        this.applicantFullname = applicantFullname;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }

    public String getApplicantSocialmedia() {
        return applicantSocialmedia;
    }

    public void setApplicantSocialmedia(String applicantSocialmedia) {
        this.applicantSocialmedia = applicantSocialmedia;
    }

    public String getApplicantUniversity() {
        return applicantUniversity;
    }

    public void setApplicantUniversity(String applicantUniversity) {
        this.applicantUniversity = applicantUniversity;
    }

    public static Creator<ApplicantModel> getCREATOR() {
        return CREATOR;
    }

    protected ApplicantModel(Parcel in) {
        applicantID = in.readString();
        applicantFullname = in.readString();
        applicantEmail = in.readString();
        applicantPhone = in.readString();
    }

    public static final Creator<ApplicantModel> CREATOR = new Creator<ApplicantModel>() {
        @Override
        public ApplicantModel createFromParcel(Parcel in) {
            return new ApplicantModel(in);
        }

        @Override
        public ApplicantModel[] newArray(int size) {
            return new ApplicantModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(applicantID);
        dest.writeString(applicantFullname);
        dest.writeString(applicantEmail);
        dest.writeString(applicantPhone);
    }

}
