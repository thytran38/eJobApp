package com.example.ejob.ui.employer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ejob.R;
import com.example.ejob.ui.register.Register;
import com.example.ejob.utils.CommonUtils;
import com.example.ejob.utils.Date;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddJob extends AppCompatActivity {
    Button addJobButton;
    EditText jobTitle, jobDescription, jobLocation, jobSalary, employerName, oodDate, jobDateCreated, jobSkills, etJobType;
    TextInputLayout textInputLayout;
    AutoCompleteTextView tvIT, tvArt, tvBusiness, tvOther;
    FirebaseAuth fAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseFirestore firebaseFirestore2;
    String employerFullname, jobType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_job);
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore2 = FirebaseFirestore.getInstance();

//        textInputLayout = findViewById(R.id.etType);
        jobTitle = findViewById(R.id.etTitle);
        jobDescription = findViewById(R.id.etDescription);
        jobLocation = findViewById(R.id.etLocation);
        jobSalary = findViewById(R.id.etSalary);
        employerName = findViewById(R.id.etEmployername);
        oodDate = findViewById(R.id.etTime);
        jobDateCreated = findViewById(R.id.etDatecreated);
        jobSkills = findViewById(R.id.etSkills);
        addJobButton = findViewById(R.id.btnAdd);
        etJobType = findViewById(R.id.etJobType);


//        tvIT = findViewById(R.id.tvIT);
//        tvArt = findViewById(R.id.tvArt);
//        tvBusiness = findViewById(R.id.tvBusiness);
//        tvOther = findViewById(R.id.tvOther);


        FirebaseUser firebaseUser = fAuth.getCurrentUser();
        DocumentReference df = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                employerName.setText((String) documentSnapshot.get("FullName"));
//                employerFullname = ;
                Log.d("DF", "EmployerFullName" + (String) documentSnapshot.get("FullName"));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddJob.this, "Failed to find Employer", Toast.LENGTH_SHORT).show();
            }
        });

        jobDateCreated.setText(Date.getInstance().toString());

        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonUtils.checkFields(jobTitle);
                CommonUtils.checkFields(jobDescription);
                CommonUtils.checkFields(jobLocation);
                CommonUtils.checkFields(jobSalary);
//        CommonUtils.checkFields(employerName);
                CommonUtils.checkFields(oodDate);
//        CommonUtils.checkFields(jobDateCreated);
                CommonUtils.checkFields(jobSkills);

//firebaseUser.getUid().toString()
                jobType = etJobType.getText().toString();
                FirebaseUser firebaseUser = fAuth.getCurrentUser();
                DocumentReference df2 = firebaseFirestore2.collection("Jobs")
                        .document(jobType)
                        .collection("/" + employerName.getText().toString()).document();
                Map<String, Object> jobInfo = new HashMap<>();
                jobInfo.put("jobType", jobType);
                jobInfo.put("jobTitle", jobTitle.getText().toString());
                jobInfo.put("jobDescription", jobDescription.getText().toString());
                jobInfo.put("jobLocation", jobLocation.getText().toString());
                jobInfo.put("jobSalary", jobSalary.getText().toString());
                jobInfo.put("jobEmployer", employerName.getText().toString());
                jobInfo.put("jobOod", oodDate.getText().toString());
                jobInfo.put("jobSkills", jobSkills.getText().toString());
                jobInfo.put("jobDateCreated", jobDateCreated.getText().toString());
                df2.set(jobInfo);

                Toast.makeText(AddJob.this, "Job Created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),EmployerActivity.class));
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}