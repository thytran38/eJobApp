package com.example.ejob.ui.user.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.ejob.R;
import com.example.ejob.ui.user.JobDetail;
import com.example.ejob.ui.user.UserHomeFragment;
import com.facebook.shimmer.ShimmerFrameLayout;

import static android.view.View.VISIBLE;

public class ViewJobDetail extends AppCompatActivity {

    private ShimmerFrameLayout container;
    private RelativeLayout buttonApply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobdetail2);

        buttonApply = findViewById(R.id.btnApply);

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), JobDetail.class);
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