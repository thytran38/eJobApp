package com.example.ejob.ui.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejob.R;
import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.data.model.ApplicationStatus;
import com.example.ejob.ui.employer.AddJob;
import com.example.ejob.ui.employer.EmployerActivity;
import com.example.ejob.ui.register.RegisterUsr;
import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.ui.user.application.ViewJobDetail;
import com.example.ejob.ui.user.pdf.UploadPdf;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.utils.Date;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobApplying extends AppCompatActivity {
    String date;
    Bundle bundle;
    JobPostingforUser jobPosting;
    FirebaseFirestore db;
    FirebaseDatabase fbDb;
    FirebaseAuth firebaseAuth;
    DocumentReference documentReference;
    StorageReference storageReference;
    FirebaseStorage fStorage;
    Boolean cvExist;
    String timeCreated;
    String jobId, employerId, jobStatus;
    JobApplication jobApplying;
    ApplicantModel applyModel;
    UploadPdf uploadPdf;
    Uri imageUri, pdfUri;
    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    if (result != null) {
//                        avatar.setImageURI(result);
                        imageUri = result;
                    }
                }
            });
    private TextView employerName, positionHiring, jobtype, linkCv, linkCoverletter;
    private EditText getEtFullname, getEtPhone, getEtAddress, getEtEmail, getEtSchool, getEtDescription, getEtSocialMedia;
    private RelativeLayout submit;
    private ImageView upCv;
    private Context jobApplyingContext;
    private TextWatcher jobApplyTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (valFullName() && valAddress() && valPhone() && valSchool() && valSelfDescription()) {
                submit.setBackground(getDrawable(R.drawable.button_green));
            }
            submit.setEnabled(valFullName() && valAddress()
                    && valPhone() && valSchool() && valSelfDescription());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_apply);
        mapping();
        initFb();
        jobApplyingContext = this;

        jobPosting = getIntent().getExtras().getParcelable("myJobposting");

        jobId = jobPosting.getJobId();
        employerId = jobPosting.getEmployerFbID();
        jobStatus = jobPosting.getJobStatus();
        positionHiring.setText(jobPosting.getJobTitle());
        employerName.setText(jobPosting.getEmployerName());
        jobtype.setText(jobPosting.getJobType());
        getEtEmail.setText(firebaseAuth.getCurrentUser().getEmail());

        timeCreated = String.valueOf(date);


        upCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPdf = new UploadPdf();
                uploadPdf.openSomeActivityForResult();

            }
        });

        getEtFullname.addTextChangedListener(jobApplyTextWatcher);
        getEtSchool.addTextChangedListener(jobApplyTextWatcher);
        getEtAddress.addTextChangedListener(jobApplyTextWatcher);
        getEtPhone.addTextChangedListener(jobApplyTextWatcher);
        getEtDescription.addTextChangedListener(jobApplyTextWatcher);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(valFullName() && valAddress() && valPhone() && valSchool() && valSelfDescription())) {
                    Toast.makeText(JobApplying.this, "Can not leave blank", Toast.LENGTH_SHORT).show();
                } else {
                    gatherData();

                    AlertDialog.Builder builder = new AlertDialog.Builder(jobApplyingContext);
                    builder.setTitle("Job Applying alert");
                    builder.setMessage("You cannot edit the application after this submission. \nAre you sure you want to continue?");
                    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    submitEvent(gatherData());
                                    Toast.makeText(JobApplying.this, "Application Submitted!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(jobApplyingContext, UserActivity.class));
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    builder.setPositiveButton("Yes", dialogListener);
                    builder.setNegativeButton("No", dialogListener);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    private Pair<JobApplication, ApplicantModel> gatherData() {
        ApplicantModel applicantModel = new ApplicantModel();
        applicantModel.setApplicantID(firebaseAuth.getCurrentUser().getUid());
        applicantModel.setApplicantUniversity(getEtSchool.getText().toString());
        applicantModel.setApplicantFullname(getEtFullname.getText().toString());
        applicantModel.setApplicantEmail(getEtEmail.getText().toString());
        applicantModel.setApplicantPhone(getEtPhone.getText().toString());
        applicantModel.setApplicantAddress(getEtAddress.getText().toString());
        applicantModel.setApplicantSocialmedia(getEtSocialMedia.getText().toString());

        JobApplication jobApplication = new JobApplication();
//        int orderNumber = new Random().
        jobApplication.setApplicationId(jobPosting.getJobId().replaceAll(".*/", "") + "/" + applicantModel.getApplicantID());
        jobApplication.setCvitaeLink(linkCoverletter.getText().toString());
        jobApplication.setSelfDescription(getEtDescription.getText().toString());
        jobApplication.setPosition(positionHiring.getText().toString());
        jobApplication.setApplicationDate(String.valueOf(timeCreated));
        jobApplication.setApplicationStatus(ApplicationStatus.SUBMITTED);
        jobApplication.setApplicantID(firebaseAuth.getCurrentUser().getUid());


        return new Pair<JobApplication, ApplicantModel>(jobApplication, applicantModel);
    }

    private void initFb() {
        firebaseAuth = FirebaseAuth.getInstance();
        fbDb = FirebaseDatabase.getInstance();
        fStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        Date dateInsc = Date.getInstance();
        date = dateInsc.toString();
        submit.setEnabled(false);
        submit.setBackground(getDrawable(R.drawable.button_grayout));
    }

    private boolean valFullName() {
        String name = getEtFullname.getText().toString();
        if (name.isEmpty()) {
            getEtFullname.setError("Please enter your name");
            return false;
        } else {
            getEtFullname.setError(null);
            return true;
        }
    }

    private boolean valPhone() {
        String name = getEtPhone.getText().toString();
        if (name.isEmpty()) {
            getEtPhone.setError("Please enter your phone number");
            return false;
        } else {
            getEtPhone.setError(null);
            return true;
        }
    }

    private boolean valAddress() {
        String name = getEtAddress.getText().toString();
        if (name.isEmpty()) {
            getEtAddress.setError("Please enter your Address");
            return false;
        } else {
            getEtAddress.setError(null);
            return true;
        }
    }

    private boolean valSelfDescription() {
        String name = getEtDescription.getText().toString();
        if (name.isEmpty()) {
            getEtDescription.setError("Please enter your description");
            return false;
        } else {
            getEtDescription.setError(null);
            return true;
        }
    }

    private boolean valSchool() {
        String name = getEtSchool.getText().toString();
        if (name.isEmpty()) {
            getEtSchool.setError("Please enter your School");
            return false;
        } else {
            getEtSchool.setError(null);
            return true;
        }
    }

    private void submitEvent(Pair<JobApplication, ApplicantModel> thispair) {

        jobApplying = thispair.first;
        applyModel = thispair.second;


        db.collection("Applications")
                .document(jobPosting.getJobId().replaceAll(".*/", ""))
                .collection("applied")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(jobApplying)
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
                .setValue(jobApplying);

        fbDb.getReference("employerapplications")
                .child(jobPosting.getEmployerFbID())
                .child(jobPosting.getJobId().replaceAll(".*/", ""))
                .child(firebaseAuth.getCurrentUser().getUid())
                .setValue(jobApplying);

        db.collection("Applicants")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(applyModel)
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

        db.collection("JobApplied")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("submitted")
                .document(jobPosting.getJobId().replaceAll(".*/", ""))
                .set(jobApplying)
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(jobApplyingContext);
        builder.setTitle("Job Applying alert");
        builder.setMessage("You are about to quit this page. \nAre you sure you want to continue?");
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Toast.makeText(JobApplying.this, "Application Cancelled!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(jobApplyingContext, ViewJobDetail.class));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        builder.setPositiveButton("Yes", dialogListener);
        builder.setNegativeButton("No", dialogListener);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void mapping() {
        positionHiring = findViewById(R.id.titleJob);
        employerName = findViewById(R.id.nameEmp);
        jobtype = findViewById(R.id.typeJob);
        submit = findViewById(R.id.btnApply);
        upCv = findViewById(R.id.cvAttach);

        linkCoverletter = findViewById(R.id.pdfLinks);
        getEtFullname = findViewById(R.id.etFullname);
        getEtSchool = findViewById(R.id.etInsitution);
        getEtPhone = findViewById(R.id.etPersonalPhone);
        getEtEmail = findViewById(R.id.etFacebook);
        getEtAddress = findViewById(R.id.etPersonalAddress);
        getEtDescription = findViewById(R.id.etSelfDescription);
        getEtSocialMedia = findViewById(R.id.etSocialMedia);
    }

    private void uploadImageEvent() {
        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Uploading........");
        getContent.launch("image/*");

        if (imageUri != null) {
            int imgRandomLink = new Random().nextInt(5000);
//            imageUri = String.valueOf(imgRandomLink);
            StorageReference reference = fStorage.getInstance().getReference().child("images/" + imgRandomLink);
            reference.putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                progressBar.dismiss();
                                task.getResult().getMetadata().getReference().getDownloadUrl();
                                Toast.makeText(JobApplying.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();

                            } else {
                                progressBar.dismiss();
                                Toast.makeText(JobApplying.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    private void uploadPdfEvent() {
        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Uploading........");
        getContent.launch("image/*");

        if (imageUri != null) {
            int imgRandomLink = new Random().nextInt(5000);
//            imageUri = String.valueOf(imgRandomLink);
            StorageReference reference = fStorage.getInstance().getReference().child("images/" + imgRandomLink);
            reference.putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                progressBar.dismiss();
                                task.getResult().getMetadata().getReference().getDownloadUrl();
                                Toast.makeText(JobApplying.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();

                            } else {
                                progressBar.dismiss();
                                Toast.makeText(JobApplying.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

}
