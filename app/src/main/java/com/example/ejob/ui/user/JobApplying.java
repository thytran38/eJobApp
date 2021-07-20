package com.example.ejob.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejob.R;
import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.data.model.ApplicationStatus;
import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.ui.user.application.ViewJobDetail;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.utils.Date;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobApplying extends AppCompatActivity {

    private EditText etPositionApplyfor, etFullname, etAddress1, etAddress2, etPhone, etEmail, etMessage;
    private Button chooseFileCV, submitApplication;

    Bundle bundle;
    private TextView employerName, positionHiring, jobtype, linkCv, linkCoverletter;
    private EditText getEtFullname, getEtPhone, getEtAddress, getEtEmail, getEtSchool, cletter;
    private RelativeLayout submit;
    private ImageView imgCv, imgLt;
    JobPostingforUser jobPosting;

    FirebaseFirestore db;
    FirebaseDatabase fbDb;
    FirebaseAuth firebaseAuth;
    DocumentReference documentReference;
    StorageReference storageReference;
    Boolean cvExist;

    String timeCreated;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_apply);

        firebaseAuth = FirebaseAuth.getInstance();
        fbDb = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        mapping();

        jobPosting = getIntent().getExtras().getParcelable("myJobposting");

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
        timeCreated = String.valueOf(Date.getEpochSecond());

        JobApplication jobApplication = new JobApplication();
        jobApplication.setPosition(positionHiring.getText().toString());
        jobApplication.setApplicationDate(String.valueOf(timeCreated));
        jobApplication.setApplicationStatus(ApplicationStatus.PENDING);
        if(cletter.getText().equals("")){
            cletter.setText("Please Enter Coverletter");
        }
        else {
            jobApplication.setSelfDescription(cletter.getText().toString());
        }


        ApplicantModel applicantModel = new ApplicantModel();
        applicantModel.setApplicantID(firebaseAuth.getCurrentUser().getUid());
        getEtFullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getEtFullname.getText().toString().isEmpty()){
                    applicantModel.setApplicantFullname(getEtFullname.getText().toString());
                }

            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((getEtFullname.getText().toString().equals("") && getEtPhone.getText().toString().equals("")
                        && isEmailValid(getEtEmail.getText().toString()) && getEtAddress.getText().toString().equals("")
                        && getEtSchool.getText().toString().equals(""))){
                    Toast.makeText(JobApplying.this, "Can not leave blank", Toast.LENGTH_SHORT).show();
                }
                else{
                    submitEvent(jobApplication, applicantModel);
                    Toast.makeText(JobApplying.this, "Submit Application successfully!", Toast.LENGTH_SHORT).show();
                }

                }

        });


    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void submitEvent(JobApplication application, ApplicantModel applicant) {

        db.collection("Applications")
                .document(jobPosting.getJobId().replaceAll(".*/", ""))
                .collection("pending")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(application)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(JobApplying.this, "Applied successfully for this " + jobPosting.getJobTitle() + " job.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(JobApplying.this, "Failed to apply. Please retry.", Toast.LENGTH_SHORT).show();
                    }
                });

        fbDb.getReference("userapplications")
                .child(jobPosting.getJobId().replaceAll(".*/", ""))
                .child(firebaseAuth.getCurrentUser().getUid())
                .setValue(application);

        fbDb.getReference("employerapplications")
                .child(jobPosting.getEmployerName())
                .child(jobPosting.getJobId().replaceAll(".*/", ""))
                .child(firebaseAuth.getCurrentUser().getUid())
                .setValue(application);

        db.collection("Applicants")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(applicant)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Applicants", "Succeeded add job to Applicants");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Applicants", "Failed add job to Applicants");

                    }
                });

        db.collection("JobAppliedHistory")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("jobs")
                .document(jobPosting.getJobId().replaceAll(".*/", ""))
                .set(application)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("AppliedHistory", "Success add job to JobAppliedHistory");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("AppliedHistory", "Failure add job to JobAppliedHistory");

                    }
                });

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