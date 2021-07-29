package com.example.ejob.ui.employer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    private ShimmerFrameLayout container;
    private RelativeLayout buttonApply;
    private TextView jobTitle, employerName, jobType, jd, email, jobId, numberApplied,test;
    private JobPostingforUser jobPosting;
    private TextView applyTv, numberneed, salary, location;
    private ImageView appIcon;
    private RecyclerView applicationList;
    private ApplicationViewModel applicationViewModel;
    private ApplicationAdapter applicationAdapter;


    private FirebaseFirestore db1, db2;
    private DatabaseReference db3;
    private FirebaseAuth fAuth;
    private Context context;
    private LifecycleRegistry lifecycleRegistry;


//    @NonNull
//    @Override
//    public Lifecycle getLifecycle() {
//        return lifecycleRegistry;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobdetail_employer);
        mapping();

        initDb();

        jobPosting = getIntent().getExtras().getParcelable("myJobposting");
        String appliedApplicants = getIntent().getExtras().getParcelable("appliedNum");
        String appliedNumbers = getAppliedNumber();
        jobTitle.setText(jobPosting.getJobTitle());
        jd.setText(jobPosting.getJobDescription());
        jobType.setText(jobPosting.getJobType());
        jobId.setText(jobPosting.getJobId());
        numberApplied.setText(appliedApplicants);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        applicationList.setLayoutManager(linearLayoutManager);

        //ViewModel1 vm1 = ViewModelProviders.of(this, new MyViewModelFactory(getApplication(), "something")).get(ViewModel1.class);

        applicationViewModel = new ViewModelProvider(this, new ApplicationViewModelFactory2(jobPosting.getJobId()))
                .get(ApplicationViewModel.class);
//        applicationViewModel = new ViewModelProvider(this).get(ApplicationViewModel_2.class);

        applicationViewModel.getmListApplicationsLivedata().observe(this, new Observer<List<JobApplication>>() {
            @Override
            public void onChanged(List<JobApplication> jobApplications) {

                applicationAdapter = new ApplicationAdapter(jobApplications, new ApplicationAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(JobApplication application) {
                    }
                });

                applicationList.setAdapter(applicationAdapter);
            }
        });



    }

//    private LifecycleOwner getLifeCycleOwner() throws InstantiationException, IllegalAccessException {
//        return LifecycleOwner.class.newInstance();
//    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapping();

        initDb();

        jobPosting = getIntent().getExtras().getParcelable("myJobposting");
        String appliedApplicants = getIntent().getExtras().getParcelable("appliedNum");
        String appliedNumbers = getAppliedNumber();
        jobTitle.setText(jobPosting.getJobTitle());
        jd.setText(jobPosting.getJobDescription());
        jobType.setText(jobPosting.getJobType());
        jobId.setText(jobPosting.getJobId());
        numberApplied.setText(appliedApplicants);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        applicationList.setLayoutManager(linearLayoutManager);

        //ViewModel1 vm1 = ViewModelProviders.of(this, new MyViewModelFactory(getApplication(), "something")).get(ViewModel1.class);

        applicationViewModel = new ViewModelProvider(this, new ApplicationViewModelFactory(getApplication(), jobPosting.getJobId()))
                .get(ApplicationViewModel.class);

        applicationViewModel.getmListApplicationsLivedata().observe(lifecycleOwner, new Observer<List<JobApplication>>() {
            @Override
            public void onChanged(List<JobApplication> jobApplications) {
                applicationAdapter = new ApplicationAdapter(jobApplications, new ApplicationAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(JobApplication application) {
                        Toast.makeText(context, "", Toast.LENGTH_LONG).show();

                    }
                });

                applicationList.setAdapter(applicationAdapter);
            }
        });


    }


 */

    private String getAppliedNumber() {
        return "";


    }

    private void initDb() {
        db1 = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

    }


    private void mapping() {
        test = findViewById(R.id.tvTest123);
        jobTitle = findViewById(R.id.tvJobTitle);
        jd = findViewById(R.id.description);
        jobId = findViewById(R.id.tvthisJobID);
        jobType = findViewById(R.id.typeJob);
        numberApplied = findViewById(R.id.tvNumerApplied1);
        applicationList = findViewById(R.id.rcvApplicants);
    }


}

