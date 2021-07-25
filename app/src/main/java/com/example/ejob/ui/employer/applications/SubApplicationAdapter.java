package com.example.ejob.ui.employer.applications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.data.model.ApplicationStatus;
import com.example.ejob.ui.employer.job.JobAdapter;
import com.example.ejob.ui.employer.job.JobPosting;
import com.example.ejob.ui.user.JobApplying;
import com.example.ejob.ui.user.UserActivity;
import com.example.ejob.ui.user.application.JobApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SubApplicationAdapter extends RecyclerView.Adapter<SubApplicationAdapter.ApplicationItemViewHolder> {

    public List<JobApplication> mApplicationList;
    private JobAdapter.ItemClickListener onItemClickListener;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userapplicationref;
    private StorageReference storage;
    View v;
    JobApplication subItem;
    String jobId;


    public SubApplicationAdapter(List<JobApplication> appList, JobAdapter.ItemClickListener itemClickListener) {
        this.mApplicationList = appList;
        onItemClickListener = itemClickListener;
    }

    public SubApplicationAdapter(int numberApplied) {
    }

    @NonNull
    @Override
    public ApplicationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);
        this.v = view;
        return new ApplicationItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationItemViewHolder holder, int position) {
        firestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userapplicationref = firebaseDatabase.getReference("userapplications");
//        storage = firebaseDatabase.

        subItem = mApplicationList.get(position);

        holder.applicantName.setText(subItem.getApplicantFullname());
        holder.address.setText(subItem.getApplicantAddress());
        holder.school.setText(subItem.getApplicantUniversity());
        holder.cv.setText(subItem.getCvitaeLink());
        holder.email.setText(subItem.getApplicantEmail());
        holder.phone.setText(subItem.getApplicantPhone());
        holder.des.setText(subItem.getSelfDescription());
        holder.socialmedia.setText(subItem.getApplicantSocialmedia());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Job Applying alert");
                builder.setMessage("You cannot edit the application after this submission. \nAre you sure you want to continue?");
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                shortList(subItem.getApplicantID());
                                Toast.makeText(v.getContext(), "Application Submitted!", Toast.LENGTH_LONG).show();
                                v.getContext().startActivity(new Intent(v.getContext(), UserActivity.class));
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

    private void shortList(String applicantID) {
        firestore.collection("Applications")
                .document(jobId)
                .collection("applied")
                .document(applicantID)
                .update("applicationStatus", String.valueOf(ApplicationStatus.SHORTLISTED))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(v.getContext(), "Shortlisted this applicant!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Failed to add this person.", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void viewPdf() {
        Intent intent = new Intent(v.getContext(), ViewPdf.class);
        intent.putExtra("cvUrl", subItem.getCvitaeLink());
        v.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ApplicationItemViewHolder extends RecyclerView.ViewHolder {
        TextView applicantName, school, phone, address, email, cv, socialmedia, des;
        Button button;


        public ApplicationItemViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantName = itemView.findViewById(R.id.apName);
            school = itemView.findViewById(R.id.lamp);
            phone = itemView.findViewById(R.id.apPhone);
            address = itemView.findViewById(R.id.address);
            email = itemView.findViewById(R.id.apEmail);
            cv = itemView.findViewById(R.id.cvView);
            button = itemView.findViewById(R.id.btnChooseProfile);
            socialmedia = itemView.findViewById(R.id.apSocial);
            des = itemView.findViewById(R.id.apSelfdescription);

        }
    }
}
