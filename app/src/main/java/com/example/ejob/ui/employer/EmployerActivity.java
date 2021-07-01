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
import android.widget.LinearLayout;

import com.example.ejob.ui.job.JobAdapter;
import com.example.ejob.ui.job.JobPosting;
import com.example.ejob.ui.job.JobViewModel;
import com.example.ejob.ui.login.LoginActivity;
import com.example.ejob.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class EmployerActivity extends AppCompatActivity {
    Button logout, newJob;
    RecyclerView jobRecyclerView;
    private JobViewModel jobViewModel;
    private JobAdapter jobAdapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);

//        jobRecyclerView.setAdapter(adapter);

        logout = findViewById(R.id.btnLogout_employer);
        newJob = findViewById(R.id.btnAddjob);
        jobRecyclerView = findViewById(R.id.rcJoblist);
        swipeRefreshLayout = findViewById(R.id.swipeJoblist);

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
                jobAdapter = new JobAdapter(jobPostings);
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

                startActivity(new Intent(getApplicationContext(),AddJob.class));
                finish();

            }
        });
    }


}