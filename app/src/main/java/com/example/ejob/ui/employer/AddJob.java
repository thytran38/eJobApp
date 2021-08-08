package com.example.ejob.ui.employer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ejob.R;
import com.example.ejob.utils.CommonUtils;
import com.example.ejob.utils.Date;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AddJob extends AppCompatActivity {
    Button addJobButton;
    EditText jobTitle, jobDescription, jobLocation, jobSalary, employerName, oodDate, jobDateCreated, numberApplicant, cvRequired;
    TextInputLayout textInputLayout;
    AutoCompleteTextView jobType;
    FirebaseAuth fAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseFirestore firebaseFirestore2;
    String jobTypeStr;
    Calendar c;
    int sYear, sMonth, sDay;
    String timeCreated, timeDeadline;
    private AutoCompleteTextView autoCompleteTextView;
    private RecruiteType[] option;
    private Context addJobContext;
    DocumentReference df;
    FirebaseUser firebaseUser;


    private TextWatcher addjobTextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean allval = valOodDate() && valTitle()
                    && valNumberOfApplicant()
                    && valJd()
                    && valLoca()
                    && valSalary() && valCvRequired();

            if (allval) {
                addJobButton.setBackground(getDrawable(R.drawable.button_bg));
            }
            addJobButton.setEnabled(allval);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            sYear = year;
            sMonth = month;
            sDay = dayOfMonth;
            Date date2 = Date.getInstance(sDay, sMonth, sYear);

            long des = Date.getEpochSecond(sDay, sMonth, sYear);
            timeDeadline = String.valueOf(date2.getEpochSecond());
            Log.d("TAG", timeDeadline);
            oodDate.setText(Date.getInstance(sDay, sMonth, sYear).toString());

        }
    };

    private boolean valSalary() {
        String text = jobSalary.getText().toString();
        if (text.isEmpty()) {
            jobSalary.setError("Please enter salary");
            return false;
        } else {
            jobSalary.setError(null);
            return true;
        }
    }

    private boolean valLoca() {
        String text = jobLocation.getText().toString();
        if (text.isEmpty()) {
            jobLocation.setError("Please enter location");
            return false;
        } else {
            jobLocation.setError(null);
            return true;
        }
    }



    private boolean valJd() {
        String jd = jobDescription.getText().toString();
        if (jd.isEmpty()) {
            jobDescription.setError("Please enter job description");
            return false;
        } else {
            jobDescription.setError(null);
            return true;
        }
    }

    private boolean valTitle() {
        String title = jobTitle.getText().toString();
        if (title.isEmpty()) {
            jobTitle.setError("Please enter title");
            return false;
        } else {
            jobTitle.setError(null);
            return true;
        }
    }

    private boolean valNumberOfApplicant() {
        String num = numberApplicant.getText().toString();
        if (num.isEmpty()) {
            numberApplicant.setError("Please enter number");
            return false;
        } else {
            numberApplicant.setError(null);
            return true;
        }
    }

    private boolean valCvRequired() {
        String num = cvRequired.getText().toString();
        if (num.isEmpty()) {
            cvRequired.setError("Please enter cv requirement");
            return false;
        } else {
            cvRequired.setError(null);
            return true;
        }
    }

    private boolean valOodDate() {
        String deadlineDate = oodDate.getText().toString();
        if (deadlineDate.isEmpty()) {
            oodDate.setError("Please enter expire date for this Job");
            return false;
        } else {
            oodDate.setError(null);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        FirebaseApp.initializeApp(AddJob.this);

        addJobContext = this;
        mapping();
        initActivity();
        setJobTypeAdapter();

        jobTitle.addTextChangedListener(addjobTextwatcher);
        jobDescription.addTextChangedListener(addjobTextwatcher);
        jobLocation.addTextChangedListener(addjobTextwatcher);
        jobSalary.addTextChangedListener(addjobTextwatcher);
        numberApplicant.addTextChangedListener(addjobTextwatcher);
        oodDate.addTextChangedListener(addjobTextwatcher);
        cvRequired.addTextChangedListener(addjobTextwatcher);

        oodDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                Date date = Date.getInstance();
                sYear = date.getYear();
                sMonth = date.getMonth();
                sDay = date.getDate();

                DatePickerDialog _date = new DatePickerDialog(AddJob.this, dateSetListener, sYear, sMonth, sDay) {

                    @Override
                    public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
                        super.onDateChanged(view, year, month, dayOfMonth);
                        if (year < sYear)
                            view.updateDate(sYear, sMonth, sDay);

                        if (month < sMonth && year == sYear) {
                            view.updateDate(sYear, sMonth, sDay);
                        }

                        if (dayOfMonth < sDay && year == sYear && month == sMonth) {
                            view.updateDate(sYear, sMonth, sDay);
                        }

                    }
                };

                _date.show();
            }
        });

        jobDateCreated.setText(Date.getInstance().toString());
        timeCreated = String.valueOf(Date.getEpochSecond());


        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();
                Context context;
                AlertDialog.Builder builder = new AlertDialog.Builder(addJobContext);
                builder.setTitle("Job Posting alert");
                builder.setMessage("You cannot edit the Job Description & Salary after posting. \nAre you sure you want to continue?");
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                postJob();
                                Toast.makeText(AddJob.this, "Job Created", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(addJobContext, EmployerActivity.class));
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }
                    }
                };
                builder.setPositiveButton("Yes", dialogListener);
                builder.setNegativeButton("No", dialogListener);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void postJob() {
        jobTypeStr = jobType.getText().toString();
        Random r = new Random();
        int low = 100000;
        int high = 1000000;
        int jobId = r.nextInt(high-low) + low;
        int employerPublicId = r.nextInt(high-low) + low;
        FirebaseUser firebaseUser = fAuth.getCurrentUser();
        DocumentReference df2 = firebaseFirestore2.collection("Jobs")
                .document();
        Map<String, Object> jobInfo = new HashMap<>();
        jobInfo.put("empId", firebaseUser.getUid());
        jobInfo.put("employerEmail", firebaseUser.getEmail());
        jobInfo.put("jobId", String.valueOf(jobId));
        jobInfo.put("jobType", jobType.getEditableText().toString());
        jobInfo.put("numberNeed", numberApplicant.getText().toString());
        jobInfo.put("jobTitle", jobTitle.getText().toString());
        jobInfo.put("jobDescription", jobDescription.getText().toString());
        jobInfo.put("jobLocation", jobLocation.getText().toString());
        jobInfo.put("jobSalary", jobSalary.getText().toString());
        jobInfo.put("jobEmployer", employerName.getText().toString());
        jobInfo.put("isAvailable", "1");
        jobInfo.put("jobOod", timeDeadline);
        jobInfo.put("jobDateCreated", timeCreated);
        jobInfo.put("cvRequired",cvRequired.getText().toString());
        df2.set(jobInfo);


        DocumentReference df3 = firebaseFirestore2.collection("JobsEmp")
                .document(firebaseUser.getUid())
                .collection("myjobs")
                .document(String.valueOf(jobId));
        Map<String, Object> jobInfo2 = new HashMap<>();
        jobInfo2.put("jobId", String.valueOf(jobId));
        jobInfo2.put("employerEmail", firebaseUser.getEmail());
        jobInfo2.put("jobType", jobType.getEditableText().toString());
        jobInfo2.put("numberNeed", numberApplicant.getText().toString());
        jobInfo2.put("jobTitle", jobTitle.getText().toString());
        jobInfo2.put("jobDescription", jobDescription.getText().toString());
        jobInfo2.put("jobLocation", jobLocation.getText().toString());
        jobInfo2.put("jobSalary", jobSalary.getText().toString());
        jobInfo2.put("jobEmployer", employerName.getText().toString());
        jobInfo2.put("isAvailable", "1");
        jobInfo2.put("jobOod", timeDeadline);
        jobInfo2.put("jobDateCreated", timeCreated);
        df3.set(jobInfo2);

    }

    private void setJobTypeAdapter() {
        option = new RecruiteType[]{RecruiteType.FULLTIME, RecruiteType.PARTTIME, RecruiteType.ONETIME};
        ArrayAdapter arrayAdapter = new ArrayAdapter<RecruiteType>(this, R.layout.jobtype_option, option);
        jobType.setText(arrayAdapter.getItem(0).toString(), false);

        jobType.setAdapter(arrayAdapter);
    }

    private void initActivity() throws NullPointerException{
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore2 = FirebaseFirestore.getInstance();
        firebaseUser = fAuth.getCurrentUser();

        df = firebaseFirestore.collection("Users").document(firebaseUser.getUid());

        addJobButton.setEnabled(false);
        addJobButton.setBackground(getDrawable(R.drawable.button_grayout));
        employerName.setEnabled(false);
        jobDateCreated.setEnabled(false);
        FirebaseUser firebaseUser = fAuth.getCurrentUser();
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
    }

    private void validate() {
        CommonUtils.checkFields(jobTitle);
        CommonUtils.checkFields(jobDescription);
        CommonUtils.checkFields(jobLocation);
        CommonUtils.checkFields(jobSalary);
        CommonUtils.checkFields(oodDate);
    }

    private void mapping() {
        jobType = findViewById(R.id.autoComplete);
        jobTitle = findViewById(R.id.etTitle);
        jobDescription = findViewById(R.id.etDescription);
        jobLocation = findViewById(R.id.etLocation);
        jobSalary = findViewById(R.id.etSalary);
        employerName = findViewById(R.id.etEmployername);
        oodDate = findViewById(R.id.etTime);
        jobDateCreated = findViewById(R.id.etDatecreated);
        addJobButton = findViewById(R.id.btnAdd);
        numberApplicant = findViewById(R.id.etNumApplicant);
        cvRequired = findViewById(R.id.etCvRequired);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(addJobContext, EmployerActivity.class));
//        getFragmentManager().popBackStackImmediate();

    }


}