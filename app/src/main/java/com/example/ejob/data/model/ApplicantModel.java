package com.example.ejob.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ApplicantModel implements Parcelable {

    String applicantID;
    String applicantFullname;
    String applicantEmail;
    String applicantPhone;


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
