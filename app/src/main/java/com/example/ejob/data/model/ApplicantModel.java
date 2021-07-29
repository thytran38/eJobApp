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
    String photoURL;
    String cvURl;

    public ApplicantModel() {
    }

    public ApplicantModel(String applicantID, String applicantFullname, String applicantEmail, String applicantPhone, String applicantSocialmedia, String applicantUniversity, String applicantAddress, String photoURL, String cvURl) {
        this.applicantID = applicantID;
        this.applicantFullname = applicantFullname;
        this.applicantEmail = applicantEmail;
        this.applicantPhone = applicantPhone;
        this.applicantSocialmedia = applicantSocialmedia;
        this.applicantUniversity = applicantUniversity;
        this.applicantAddress = applicantAddress;
        this.photoURL = photoURL;
        this.cvURl = cvURl;
    }


    protected ApplicantModel(Parcel in) {
        applicantID = in.readString();
        applicantFullname = in.readString();
        applicantEmail = in.readString();
        applicantPhone = in.readString();
        applicantSocialmedia = in.readString();
        applicantUniversity = in.readString();
        applicantAddress = in.readString();
        photoURL = in.readString();
        cvURl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(applicantID);
        dest.writeString(applicantFullname);
        dest.writeString(applicantEmail);
        dest.writeString(applicantPhone);
        dest.writeString(applicantSocialmedia);
        dest.writeString(applicantUniversity);
        dest.writeString(applicantAddress);
        dest.writeString(photoURL);
        dest.writeString(cvURl);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getCvURl() {
        return cvURl;
    }

    public void setCvURl(String cvURl) {
        this.cvURl = cvURl;
    }
}
