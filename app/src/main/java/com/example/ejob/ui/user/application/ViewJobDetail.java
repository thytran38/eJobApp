package com.example.ejob.ui.user.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ejob.R;
import com.example.ejob.ui.user.JobApplying;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.facebook.shimmer.ShimmerFrameLayout;

import static android.view.View.VISIBLE;

public class ViewJobDetail extends AppCompatActivity {

    private ShimmerFrameLayout container;
    private RelativeLayout buttonApply;
    private TextView jobTitle, employerName, jobType;
    private JobPostingforUser jobPosting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobdetail2);

        jobTitle = findViewById(R.id.tvJobTitle);
        employerName = findViewById(R.id.tvEmpname);


        buttonApply = findViewById(R.id.btnApply);
        JobPostingforUser jobPosting = getIntent().getExtras().getParcelable("myJobposting");

        jobTitle.setText(jobPosting.getJobTitle());
        employerName.setText(jobPosting.getEmployerName());


        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), JobApplying.class);
                intent.putExtra("myJobposting",jobPosting);
                v.getContext().startActivity(intent);

            }
        });


        container = (ShimmerFrameLayout) findViewById(R.id.imageProgress);

        container.startShimmerAnimation();
        container.setVisibility(VISIBLE);
        container.setAutoStart(true);
        container.setDuration(800);

    }

    @Override
    protected void onResume() {
        container.startShimmerAnimation();
        super.onResume();
    }
}