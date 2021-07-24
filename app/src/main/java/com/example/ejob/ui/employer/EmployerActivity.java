package com.example.ejob.ui.employer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ejob.ui.employer.job.JobAdapter;
import com.example.ejob.ui.employer.job.JobPosting;
import com.example.ejob.ui.employer.job.JobViewModel;
import com.example.ejob.ui.login.LoginActivity;
import com.example.ejob.R;
import com.example.ejob.ui.user.ApplicationFragment;
import com.example.ejob.ui.user.UserFavoriteFragment;
import com.example.ejob.ui.user.UserHomeFragment;
import com.example.ejob.ui.user.UserProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.List;

public class EmployerActivity extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {
    Button logout, newJob;
    RecyclerView jobRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private JobViewModel jobViewModel;
    private JobAdapter jobAdapter;
    private ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);

//        jobRecyclerView.setAdapter(adapter);

        logout = findViewById(R.id.btnLogout_employer);
        newJob = findViewById(R.id.btnAddjob);
        jobRecyclerView = findViewById(R.id.rcJoblist);
        swipeRefreshLayout = findViewById(R.id.swipeJoblist);

        chipNavigationBar = findViewById(R.id.menu);
        chipNavigationBar.setMenuOrientation(ChipNavigationBar.MenuOrientation.HORIZONTAL);
        chipNavigationBar.setOnItemSelectedListener(this);
        chipNavigationBar.setItemSelected(R.id.nav_main, true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jobAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((this));
        jobRecyclerView.setLayoutManager(linearLayoutManager);

        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);
        jobViewModel.getmListJobLivedata().observe(this, new Observer<List<JobPosting>>() {
            @Override
            public void onChanged(List<JobPosting> jobPostings) {
                jobAdapter = new JobAdapter(jobPostings, new JobAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(JobPosting jobPost) {
                        Toast.makeText(getApplicationContext(), jobPost.getJobTitle(), Toast.LENGTH_LONG).show();

                    }
                });

                jobRecyclerView.setAdapter(jobAdapter);

            }


        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        newJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), AddJob.class));
                finish();

            }
        });
    }


    @Override
    public void onItemSelected(int i) {
        androidx.fragment.app.Fragment selectedFragment = null;
        switch (i) {
            case R.id.nav_main:
                selectedFragment = new EmployerHome();
                break;

            case R.id.nav_saved:
                selectedFragment = new EmployersApplications();
                break;

            case R.id.nav_profile:
                selectedFragment = new EmployerProfiles();
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }
}