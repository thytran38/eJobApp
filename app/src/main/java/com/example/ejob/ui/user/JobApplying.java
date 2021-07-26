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
import android.view.View;
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
import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.ui.user.application.ViewJobDetail;
import com.example.ejob.ui.user.pdf.UploadPdf;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.utils.Date;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

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
    Uri fileUri;

    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    if (result != null) {
//                        avatar.setImageURI(result);
                        fileUri = result;
                    }
                }
            });
    private TextView employerName, positionHiring, jobtype, linkCv;
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

            boolean allVal = valFullName() && valAddress() && valPhone() && valSchool() && valSelfDescription() && valSocialmedia();
            if (allVal) {
                submit.setBackground(getDrawable(R.drawable.button_green));
            }
            submit.setEnabled(allVal);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private UploadTask urlTask;

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
        getEtEmail.setEnabled(false);
        timeCreated = String.valueOf(date);


        upCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPdfEvent();

            }
        });

        getEtFullname.addTextChangedListener(jobApplyTextWatcher);
        getEtSchool.addTextChangedListener(jobApplyTextWatcher);
        getEtAddress.addTextChangedListener(jobApplyTextWatcher);
        getEtPhone.addTextChangedListener(jobApplyTextWatcher);
        getEtDescription.addTextChangedListener(jobApplyTextWatcher);
        getEtSocialMedia.addTextChangedListener(jobApplyTextWatcher);

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

    private JobApplication gatherData() {

        JobApplication jobApplication = new JobApplication();
//        int orderNumber = new Random().
        String cvUrl = getCvUrl();
        String imgUrl = getImgUrl();
        jobApplication.setApplicationId(jobPosting.getJobId().replaceAll(".*/", "") + "/" + jobApplication.getApplicantID());
        jobApplication.setPosition(positionHiring.getText().toString());
        jobApplication.setApplicationDate(String.valueOf(timeCreated));
        jobApplication.setApplicationStatus(ApplicationStatus.SUBMITTED);
        jobApplication.setApplicantID(firebaseAuth.getCurrentUser().getUid()); //
        jobApplication.setCvitaeLink(cvUrl);
        jobApplication.setApplicantAddress(getEtAddress.getText().toString());
        jobApplication.setSelfDescription(getEtDescription.getText().toString());
        jobApplication.setApplicantEmail(getEtEmail.getText().toString());
        jobApplication.setApplicantPhone(getEtPhone.getText().toString());
        jobApplication.setApplicantSocialmedia(getEtSocialMedia.getText().toString());
        jobApplication.setApplicantFullname(getEtFullname.getText().toString());
        jobApplication.setApplicantUniversity(getEtSchool.getText().toString());
        jobApplication.setPhotoURL(imgUrl);


        return jobApplication;
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

    private boolean valSocialmedia() {
        String name = getEtSocialMedia.getText().toString();
        if (name.isEmpty()) {
            getEtSocialMedia.setError("Please enter your Social media");
            return false;
        } else {
            getEtSocialMedia.setError(null);
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

    private void submitEvent(JobApplication jobApplication) {

        jobApplying = jobApplication;

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

        linkCv = findViewById(R.id.pdfLinks);
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

        if (fileUri != null) {
            int imgRandomLink = new Random().nextInt(5000);
//            imageUri = String.valueOf(imgRandomLink);
            StorageReference reference = fStorage.getInstance().getReference().child("images/" + imgRandomLink);
            reference.putFile(fileUri)
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

    private String getCvUrl() {
        return "f";

    }

    private String getImgUrl() {
        return "f";

    }




/*
            folder.putFile(fileUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                progressBar.dismiss();
                                task.getResult().getMetadata().getReference().getDownloadUrl();
                                folder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        upCv.setImageResource(R.drawable.ic_baseline_check_ok_24);
                                        linkCv.setText("Uploaded Successfully file: " + fileUri.getLastPathSegment());
                                        progressBar.dismiss();

                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("cvLink", String.valueOf(file_name.getDownloadUrl().toString()));
                                        hashMap.put("cvUri", String.valueOf(task.getResult()));
                                        hashMap.put("fileName",fileUri.getLastPathSegment());
                                        jobApplying.setCvitaeLink(String.valueOf(task.getResult()));

                                        fbDb.getReference("cvUploads")
                                            .child(firebaseAuth.getCurrentUser().getUid())
                                            .setValue(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(uploadPdf, "Done uploading!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });

                            } else {
                                progressBar.dismiss();
                                Toast.makeText(JobApplying.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

            file_name.putFile(fileUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            upCv.setImageResource(R.drawable.ic_baseline_check_ok_24);
                            linkCv.setText("Uploaded Successfully with URL: " + folder.child(possibleNameFile).getDownloadUrl());
                            progressBar.dismiss();
                            file_name.getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("cvLink", String.valueOf(file_name.getDownloadUrl().toString()));
                                    hashMap.put("cvUri", String.valueOf(task.getResult()));
                                    jobApplying.setCvitaeLink(String.valueOf(task.getResult()));

                                    fbDb.getReference("cvUploads")
                                            .child(firebaseAuth.getCurrentUser().getUid())
                                            .setValue(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(uploadPdf, "Done uploading!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressBar.setMessage("Failed");
                            progressBar.dismiss();
                        }
                    });


            file_name.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            upCv.setImageResource(R.drawable.ic_baseline_check_ok_24);
                            file_name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                }
                            });
                        }
                    });

        */


    private void uploadPdfEvent() throws IllegalStateException, NullPointerException {
        ProgressDialog progressDialog = new ProgressDialog(jobApplyingContext);
        progressDialog.setTitle("Upload CV");
        progressDialog.setMessage("Progress Bar");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });

        getContent.launch("application/pdf");
        if (fileUri != null) {
            int cvRandom = new Random().nextInt(5000);
            double progress = 0;
            StorageReference folder = fStorage.getInstance().getReference().child("CVFiles/");
            String possibleNameFile = "file" + fileUri.getLastPathSegment();
            StorageReference file_name = folder.child(possibleNameFile);
            Task task = file_name.putFile(fileUri);

            folder.putFile(fileUri)
                    .addOnProgressListener(snapshot -> {
                        double progress1 = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.show();
                        progressDialog.setProgress(((int) progress1));
                        if(progress1 == 100){
                            progressDialog.setMessage("Uploaded " + progress1 + " %");
                        }
                    })
//                    .continueWithTask(task12 -> {
//                        if (!task12.isSuccessful()) {
//                            throw task12.getException();
//                        }
//                        else{
//                            return file_name.getDownloadUrl();
//                        }
//                    })
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
//                            Uri downloadUri = task1.getResult();
//
//                            if (downloadUri == null) {
//                                return;
                            if(task1.isComplete()){
                                upCv.setImageResource(R.drawable.ic_baseline_check_ok_24);
                                linkCv.setText("Uploaded Successfully file: " + fileUri.getLastPathSegment());
                                progressDialog.setCancelable(true);
                                progressDialog.dismiss();

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("cvLink", String.valueOf(file_name.getDownloadUrl().toString()));
                                hashMap.put("cvUri", String.valueOf(task1.getResult()));
                                hashMap.put("fileName", fileUri.getLastPathSegment());
//                                jobApplying.setCvitaeLink(String.valueOf(task1.getResult()));

                                fbDb.getReference("cvUploads")
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(jobApplyingContext, "Done uploading!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {

                            }
                        }
                    });


//            UploadTask uploadTask = file_name.putFile(fileUri);
//            Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//
//                    }
//                    return file_name.getDownloadUrl();
//                }
//            })
//                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//                        Uri downloadUri = task.getResult();
//                        if (downloadUri == null) {
//                            return;
//                        } else {
//                            upCv.setImageResource(R.drawable.ic_baseline_check_ok_24);
//                            linkCv.setText("Uploaded Successfully file: " + fileUri.getLastPathSegment());
//                            progressBar.dismiss();
//
//                            HashMap<String, String> hashMap = new HashMap<>();
//                            hashMap.put("cvLink", String.valueOf(file_name.getDownloadUrl().toString()));
//                            hashMap.put("cvUri", String.valueOf(task.getResult()));
//                            hashMap.put("fileName", fileUri.getLastPathSegment());
//                            jobApplying.setCvitaeLink(String.valueOf(task.getResult()));
//
//                            fbDb.getReference("cvUploads")
//                                    .child(firebaseAuth.getCurrentUser().getUid())
//                                    .setValue(hashMap)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Toast.makeText(uploadPdf, "Done uploading!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        }
//                    }
//                }
//            });


        }

    }

}
