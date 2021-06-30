package com.example.ejob.ui.employer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ejob.ui.login.LoginActivity;
import com.example.ejob.R;
import com.google.firebase.auth.FirebaseAuth;

public class EmployerActivity extends AppCompatActivity {
    Button logout, newJob;
    RecyclerView jobRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);

//        jobRecyclerView.setAdapter(adapter);

        logout = findViewById(R.id.btnLogout_employer);
        newJob = findViewById(R.id.btnAddjob);
        jobRecyclerView = findViewById(R.id.rcJoblist);
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