package com.example.ejob.ui.employer.applications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.data.model.ApplicationStatus;
import com.example.ejob.ui.admin.employer_accounts.Employer;
import com.example.ejob.ui.employer.EmployerActivity;
import com.example.ejob.ui.user.application.JobApplication;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationItemViewHolder> {

    public List<JobApplication> mApplicationList;
    private ApplicationAdapter.ItemClickListener itemClickListener;
    JobApplication jobApplication;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userapplicationref;
    private StorageReference storage;
    View v;
    JobApplication subItem;
    String jobId;


    public ApplicationAdapter(List<JobApplication> appList, ApplicationAdapter.ItemClickListener itemClickListener1) {
        this.mApplicationList = appList;
        this.itemClickListener = itemClickListener1;
    }

    @NonNull
    @Override
    public ApplicationAdapter.ApplicationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);
        this.v = view;
        return new ApplicationAdapter.ApplicationItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ApplicationItemViewHolder holder, int position) {
        firestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        jobApplication = mApplicationList.get(position);
        userapplicationref = firebaseDatabase.getReference("userapplications");

//        storage = firebaseDatabase.
        if(jobApplication == null){
            return;
        }

        holder.applicantName.setText(jobApplication.getApplicantFullname());
        holder.address.setText(jobApplication.getApplicantAddress());
        holder.school.setText(jobApplication.getApplicantUniversity());
        holder.cv.setText(jobApplication.getCvitaeLink());
        holder.email.setText(jobApplication.getApplicantEmail());
        holder.phone.setText(jobApplication.getApplicantPhone());
        holder.des.setText(jobApplication.getSelfDescription());
        holder.socialmedia.setText(jobApplication.getApplicantSocialmedia());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Job Applying alert");
                builder.setMessage("You cannot  after this submission. \nAre you sure you want to continue?");
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                shortList(jobApplication.getApplicantID());
                                Toast.makeText(v.getContext(), "Application Submitted!", Toast.LENGTH_LONG).show();
                                v.getContext().startActivity(new Intent(v.getContext(), EmployerActivity.class));
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
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

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPdf();
            }
        });

    }

    private void shortList(String applicationID) {
        DatabaseReference usrappRef = FirebaseDatabase.getInstance().getReference("userapplications");
        jobApplication.setApplicationStatus(ApplicationStatus.SHORTLISTED);
        usrappRef.child(jobApplication.getApplicationId().substring(11,18))
                .child(jobApplication.getApplicantID())
                .setValue("applicationStatus",String.valueOf(ApplicationStatus.SHORTLISTED))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(v.getContext(), "Approved successfully!", Toast.LENGTH_SHORT).show();

                    }
                });

//        firestore.collection("Applications")
//                .document(jobApplication.getApplicationId().substring(11,18))
//                .update("applicationStatus", String.valueOf(ApplicationStatus.SHORTLISTED))
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                    }
//                });


    }

    private void viewPdf() {
        Intent intent = new Intent(v.getContext(), ViewPdf.class);
        intent.putExtra("cvUrl", jobApplication.getCvitaeLink());
        v.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {

        if(mApplicationList != null) {
            return mApplicationList.size();
        }
        return 0;
    }

    public interface ItemClickListener{
        void onItemClick(JobApplication application);
    }

    public class ApplicationItemViewHolder extends RecyclerView.ViewHolder {
        TextView applicantName, school, phone, address, email, cv, socialmedia, des;
        Button button;
        ImageView photo;


        public ApplicationItemViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantName = itemView.findViewById(R.id.applicantName);
            school = itemView.findViewById(R.id.lamp);
            phone = itemView.findViewById(R.id.apPhone);
            address = itemView.findViewById(R.id.address);
            email = itemView.findViewById(R.id.apEmail);
            cv = itemView.findViewById(R.id.cvView);
            button = itemView.findViewById(R.id.btnChooseProfile);
            socialmedia = itemView.findViewById(R.id.apSocial);
            des = itemView.findViewById(R.id.apSelfdescription);
            button = itemView.findViewById(R.id.btnChooseProfile);
            photo = itemView.findViewById(R.id.photoPreview);

        }
    }
}
