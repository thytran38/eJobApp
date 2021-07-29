package com.example.ejob.ui.user.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ejob.R;
import com.example.ejob.ui.user.JobApplying;
import com.example.ejob.ui.user.userjob.AllJobAdapter;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.logging.Logger;

import static android.view.View.VISIBLE;

public class ViewJobDetail extends AppCompatActivity {

    private ShimmerFrameLayout container;
    private RelativeLayout buttonApply;
    private TextView jobTitle, employerName, jobType, jd, email;
    private JobPostingforUser jobPosting;
    private TextView applyTv, numberneed, salary, location;
    private ImageView appIcon;

    private FirebaseFirestore db1, db2;
    private DatabaseReference db3;
    private FirebaseAuth fAuth;
    String empId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobdetail2);
        mapping();

        initDb();


        jobPosting = getIntent().getExtras().getParcelable("myJobposting");

        try{
            email.setText(jobPosting.getEmpEmail());
            numberneed.setText(jobPosting.getNumberneed() + " slots");

        }catch (NullPointerException npe){
            email.setText("No email");
            npe.getMessage();
        }
        empId = jobPosting.getEmployerFbID();

        salary.setText(jobPosting.getSalary());
        location.setText(jobPosting.getJobLocation());
        jobTitle.setText(jobPosting.getJobTitle());
        employerName.setText(jobPosting.getEmployerName());
        jd.setText(jobPosting.getJobDescription());
        jobType.setText(jobPosting.getJobType());

        getAppliedStatus(jobPosting.getJobId(),fAuth.getCurrentUser().getUid());


        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), JobApplying.class);
                intent.putExtra("myJobposting1",jobPosting);
                v.getContext().startActivity(intent);

            }
        });

        container = (ShimmerFrameLayout) findViewById(R.id.imageProgress);
        container.startShimmerAnimation();
        container.setVisibility(VISIBLE);
        container.setAutoStart(true);
        container.setDuration(800);

    }

    private void initDb() {
        db1 = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

    }

    private void mapping() {
        jobTitle = findViewById(R.id.tvJobTitle);
        employerName = findViewById(R.id.tvEmpname);
        applyTv = findViewById(R.id.tvApply);
        buttonApply = findViewById(R.id.btnApply);
        jd = findViewById(R.id.description);
        jobType = findViewById(R.id.typeJob);
        appIcon = findViewById(R.id.applyIcon);
        numberneed = findViewById(R.id.tvVacancy);
        salary = findViewById(R.id.tvSalary);
        location = findViewById(R.id.tvLocationDetail);
        email = findViewById(R.id.tvEmpEmail);
    }

    public void getAppliedStatus(String postId, String uid){
        db3 =  FirebaseDatabase.getInstance().getReference("userapplications");
        db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postId.replaceAll(".*/", "")).hasChild(uid)){
                    applyTv.setText("Already Applied");
                    buttonApply.setEnabled(false);
                    appIcon.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onResume() {
        container.startShimmerAnimation();
        super.onResume();
        Task task = db2.collection("Applications")
                .document(jobPosting.getJobId().replaceAll(".*/", ""))
                .collection("pending")
                .document(fAuth.getCurrentUser().getUid())
                .get();

        if(task.isSuccessful() && task.isComplete()){
            Log.d("TAG5", task.getResult().toString());
            applyTv.setText("Applied");
            buttonApply.setEnabled(false);
        }
    }
}