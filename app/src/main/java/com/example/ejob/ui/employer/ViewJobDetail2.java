package com.example.ejob.ui.employer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.ui.employer.applications.ApplicationAdapter;
import com.example.ejob.ui.employer.applications.ApplicationViewModel;
import com.example.ejob.ui.employer.applications.ApplicationViewModelFactory;
import com.example.ejob.ui.employer.applications.ApplicationViewModelFactory2;
import com.example.ejob.ui.employer.applications.ApplicationViewModel_2;
import com.example.ejob.ui.employer.applications.MyJobsAdapter;
import com.example.ejob.ui.employer.applications.MyJobsViewModel;
import com.example.ejob.ui.user.JobApplying;
import com.example.ejob.ui.user.UserHomeFragment;
import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.utils.Date;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import static android.view.View.VISIBLE;

public class ViewJobDetail2 extends AppCompatActivity implements LifecycleOwner {
    View v;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //UI
    private ShimmerFrameLayout container;
    private EditText jobID, numberNeed, jobTitle, jobDescription, cvRequired, jobLocation, companyName, deadline, salary, dateUpdated;
    private JobPostingforUser jobPosting;
    private RecyclerView applicationList;
    private ApplicationViewModel applicationViewModel;
    private ApplicationAdapter applicationAdapter;
    private RecruiteType[] option;
    private JobStatus[] option2;
    AutoCompleteTextView jobType2, statusType;
    private Button update;
    private Boolean updated = false;

    // logic variables
    int sDay, sYear, sMonth;

    private FirebaseFirestore db1, db2;
    private DatabaseReference db3;
    private FirebaseAuth fAuth;
    private Context context;
    private LifecycleRegistry lifecycleRegistry;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);
        mapping();
        initDb();
        setAdapters();
        setData();
        addTextWatchers();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allVal()){
                    if(gatherData().second == true){
                        updateJob(gatherData().first);
                    }

                }else{
                    return;
                }

            }
        });
    }

    private void addTextWatchers() {

        jobTitle.addTextChangedListener(updateTextwatcher);
        numberNeed.addTextChangedListener(updateTextwatcher);
        jobDescription.addTextChangedListener(updateTextwatcher);
        cvRequired.addTextChangedListener(updateTextwatcher);
        jobLocation.addTextChangedListener(updateTextwatcher);
        companyName.addTextChangedListener(updateTextwatcher);
        deadline.addTextChangedListener(updateTextwatcher);
        salary.addTextChangedListener(updateTextwatcher);
        jobType2.getValidator(validator);
    }

    private void setData() {
        jobPosting = getIntent().getExtras().getParcelable("myJobposting");

        jobID.setText(jobPosting.getJobId());
        jobTitle.setText(jobPosting.getJobTitle());
        numberNeed.setText(jobPosting.getNumberneed());
        jobDescription.setText(jobPosting.getJobDescription());
        cvRequired.setText(jobPosting.getCvRequired());
        jobLocation.setText(jobPosting.getJobLocation());
        companyName.setText(jobPosting.getEmployerName());
        deadline.setText(jobPosting.getJobDeadline());
        salary.setText(jobPosting.getSalary());
    }

    private boolean allVal() {

        return false;
    }

    private Pair<JobPostingforUser, Boolean> gatherData() {
        JobPostingforUser job = new JobPostingforUser();

        Boolean successUpdate = false;

        return new Pair<JobPostingforUser,Boolean>(job, successUpdate);
    }

    private void updateJob(JobPostingforUser jobPosting) {
//        db2.collection("Jobs")
//                .document(jobPosting.getJobId())
//                .set()


    }


    private void initDb() {
        db1 = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

    }

    private void setAdapters() {
        option = new RecruiteType[]{RecruiteType.FULLTIME, RecruiteType.PARTTIME, RecruiteType.ONETIME};
        ArrayAdapter arrayAdapter = new ArrayAdapter<RecruiteType>(this, R.layout.jobtype_option, option);
        jobType2.setText(arrayAdapter.getItem(0).toString(), false);
        jobType2.setAdapter(arrayAdapter);

        option2 = new JobStatus[]{JobStatus.AVAILABLE, JobStatus.UNAVAILABLE};
        ArrayAdapter arrayAdapter2 = new ArrayAdapter<JobStatus>(this, R.layout.jobtype_option, option2);
        statusType.setText(arrayAdapter2.getItem(0).toString(), false);
        statusType.setAdapter(arrayAdapter2);

    }

    private void mapping() {
        jobType2 = findViewById(R.id.autoComplete);
        statusType = findViewById(R.id.autoComplete1);
        jobID = findViewById(R.id.etJobIDEt);
        numberNeed = findViewById(R.id.etNumApplicant);
        jobTitle = findViewById(R.id.etTitle);
        jobDescription = findViewById(R.id.etDescription);

        cvRequired = findViewById(R.id.etCvRequired);
        jobLocation = findViewById(R.id.etLocation);
        companyName = findViewById(R.id.etEmployername);
        deadline = findViewById(R.id.etTime);
        update = findViewById(R.id.btnEdit);
        salary = findViewById(R.id.etSalary);
        dateUpdated = findViewById(R.id.etDateUpdate);
    }

    private TextWatcher updateTextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean allval = valNumberneed() && valJD()
                    && valCvRequired()
                    && valjobTitle()
                    && valLocation()
                    && valSalary() && valDeadline();

            if (allval) {
                update.setBackground(getDrawable(R.drawable.button_bg));
            }
            update.setEnabled(allval);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private AutoCompleteTextView.Validator validator = new AutoCompleteTextView.Validator() {
        @Override
        public boolean isValid(CharSequence text) {
            return false;
        }

        @Override
        public CharSequence fixText(CharSequence invalidText) {
            return null;
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
            String dateUpdate = String.valueOf(date2.getEpochSecond());
            Log.d("TAG", dateUpdate);
            dateUpdated.setText(Date.getInstance(sDay, sMonth, sYear).toString());

        }
    };

    private boolean valNumberneed() {
        String name = numberNeed.getText().toString();
        if (name.isEmpty()) {
            numberNeed.setError("Please enter number of slots");
            return false;
        } else {
            numberNeed.setError(null);
            return true;
        }
    }

    private boolean valJD() {
        String name = jobDescription.getText().toString();
        if (name.isEmpty()) {
            jobDescription.setError("Please enter Job Description");
            return false;
        } else {
            jobDescription.setError(null);
            return true;
        }
    }

    private boolean valCvRequired() {
        String name = cvRequired.getText().toString();
        if (name.isEmpty()) {
            cvRequired.setError("Please enter requirement of CV");
            return false;
        } else {
            cvRequired.setError(null);
            return true;
        }
    }

    private boolean valjobTitle() {
        String name = jobTitle.getText().toString();
        if (name.isEmpty()) {
            jobTitle.setError("Please enter job title");
            return false;
        } else {
            jobTitle.setError(null);
            return true;
        }
    }

    private boolean valLocation() {
        String name = jobLocation.getText().toString();
        if (name.isEmpty()) {
            jobLocation.setError("Please enter job Location");
            return false;
        } else {
            jobLocation.setError(null);
            return true;
        }
    }

    private boolean valSalary() {
        String name = salary.getText().toString();
        if (name.isEmpty()) {
            salary.setError("Please enter salary");
            return false;
        } else {
            salary.setError(null);
            return true;
        }
    }

    private boolean valDeadline() {
        String name = salary.getText().toString();
        if (name.isEmpty()) {
            deadline.setError("Please enter deadline of this job");
            return false;
        } else {
            deadline.setError(null);
            return true;
        }
    }



}

