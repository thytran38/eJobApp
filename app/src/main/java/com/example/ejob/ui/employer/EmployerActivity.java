package com.example.ejob.ui.employer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainer;
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
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.List;

public class EmployerActivity extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {
    private ChipNavigationBar chipNavigationBar;
    FragmentContainer fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);

        chipNavigationBar = findViewById(R.id.menu2);
        chipNavigationBar.setMenuOrientation(ChipNavigationBar.MenuOrientation.HORIZONTAL);
        chipNavigationBar.setOnItemSelectedListener(this);
        chipNavigationBar.setItemSelected(R.id.nav_main, true);

    }


    @Override
    public void onItemSelected(int i) {
        androidx.fragment.app.Fragment selectedFragment = null;
        switch (i) {
            case R.id.nav_employer_main:
                selectedFragment = new EmployerHome();
                break;

            case R.id.nav_employer_case:
                selectedFragment = new JobApplicationEmp();
                break;

            case R.id.nav_applications:
                selectedFragment = new ApplicationsEmp();
                break;

            case R.id.nav_employer_profile:
                selectedFragment = new EmployerProfile();
                break;

            default:
                chipNavigationBar.setItemSelected(R.id.nav_employer_main, true);
                selectedFragment = new EmployerHome();
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_employer, selectedFragment).commit();
    }
}