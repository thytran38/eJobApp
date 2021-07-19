package com.example.ejob.ui.user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejob.R;
import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.data.model.ApplicationStatus;
import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.utils.Date;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class JobApplying extends AppCompatActivity {

    private EditText etPositionApplyfor, etFullname, etAddress1, etAddress2, etPhone, etEmail, etMessage;
    private Button chooseFileCV, submitApplication;

    Bundle bundle;
    private TextView employerName, positionHiring, jobtype, linkCv, linkCoverletter;
    private EditText getEtFullname, getEtPhone, getEtAddress, getEtEmail, getEtSchool, cletter;
    private RelativeLayout submit;
    private ImageView imgCv, imgLt;

    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DocumentReference documentReference;
    StorageReference storageReference;
    Boolean cvExist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_apply);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mapping();

        JobPostingforUser jobPosting = getIntent().getExtras().getParcelable("myJobposting");

        if(firebaseAuth.getCurrentUser().getDisplayName() == null){
            getEtFullname.setText("Enter Full Name");
        }else{
            getEtFullname.setText(firebaseAuth.getCurrentUser().getDisplayName());
        }

        positionHiring.setText(jobPosting.getJobTitle());
        employerName.setText(jobPosting.getEmployerName());
        jobtype.setText(jobPosting.getJobDeadline());
        getEtEmail.setText(firebaseAuth.getCurrentUser().getEmail());
        if(firebaseAuth.getCurrentUser().getPhoneNumber()==null){
            getEtPhone.setText("Enter Phone Number");
        }else{
            getEtPhone.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setPosition(positionHiring.getText().toString());
        jobApplication.setApplicationDate(String.valueOf(Date.getEpochSecond()));
        jobApplication.setApplicationStatus(ApplicationStatus.PENDING);
        if(cletter.getText().equals("")){
            cletter.setText("Please Enter Coverletter");
        }
        else {
            jobApplication.setSelfDescription(cletter.getText().toString());
        }


        ApplicantModel applicantModel = new ApplicantModel();
        applicantModel.setApplicantID(firebaseAuth.getCurrentUser().getUid());
        if(!getEtFullname.getText().toString().isEmpty()){
            applicantModel.setApplicantFullname(getEtFullname.getText().toString());
        }
        if(!getEtEmail.getText().toString().isEmpty()){
            applicantModel.setApplicantEmail(getEtEmail.getText().toString());
        }
        if(!getEtPhone.getText().toString().isEmpty()){
            applicantModel.setApplicantPhone(getEtPhone.getText().toString());
        }
        if(!getEtSchool.getText().toString().isEmpty()){
            applicantModel.setApplicantUniversity(getEtSchool.getText().toString());
        }
        if(!getEtAddress.getText().toString().isEmpty()){
            applicantModel.setApplicantAddress(getEtAddress.getText().toString());
        }



    }

    public void mapping(){
        positionHiring = findViewById(R.id.titleJob);
        employerName = findViewById(R.id.nameEmp);
        jobtype = findViewById(R.id.typeJob);
        submit = findViewById(R.id.btnApply);
        imgCv = findViewById(R.id.cvAttach);
        imgLt = findViewById(R.id.letterAttach);

        getEtFullname = findViewById(R.id.etFullname);
        getEtSchool = findViewById(R.id.etInsitution);
        getEtPhone = findViewById(R.id.etPersonalPhone);
        getEtEmail = findViewById(R.id.etFacebook);
        getEtAddress = findViewById(R.id.etPersonalAddress);
        cletter = findViewById(R.id.etCoverletter);
    }
}
