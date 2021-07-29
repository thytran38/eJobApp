package com.example.ejob.ui.user.applications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
import com.example.ejob.ui.employer.AddJob;
import com.example.ejob.ui.employer.EmployerActivity;
import com.example.ejob.ui.user.UserActivity;
import com.example.ejob.ui.user.application.JobApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ApplicationAdapterforUser extends RecyclerView.Adapter<ApplicationAdapterforUser.ApplicationItemViewHolder> {

    public List<JobApplication> mApplicationList;
    private ApplicationAdapterforUser.ItemClickListener itemClickListener;
    JobApplication jobApplication;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userapplicationref;
    private StorageReference storage;
    View v;
    JobApplication subItem;
    String jobId;


    public ApplicationAdapterforUser(List<JobApplication> appList, ApplicationAdapterforUser.ItemClickListener itemClickListener1) {
        this.mApplicationList = appList;
        this.itemClickListener = itemClickListener1;
    }

    @NonNull
    @Override
    public ApplicationAdapterforUser.ApplicationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item_user, parent, false);
        this.v = view;
        return new ApplicationAdapterforUser.ApplicationItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapterforUser.ApplicationItemViewHolder holder, int position) {
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
        try{
            holder.cv.setText(jobApplication.getCvitaeLink());
        }catch(NullPointerException npe){
            Log.d("TAG_npe", npe.getMessage());
            holder.cv.setText("Applicant has no CV");
        }
        holder.email.setText(jobApplication.getApplicantEmail());
        holder.phone.setText(jobApplication.getApplicantPhone());
        holder.des.setText(jobApplication.getSelfDescription());
        holder.socialmedia.setText(jobApplication.getApplicantSocialmedia());

        if(jobApplication.getApplicationStatus().equals("CANCELLED")){
            holder.status.setBackgroundResource(R.drawable.application_status_cancelled);
            holder.status.setText(jobApplication.getApplicationStatus().toString());
        }else if(jobApplication.getApplicationStatus().equals("SUBMITTED")){
            holder.status.setBackgroundResource(R.drawable.application_status_submitted);
            holder.status.setText(jobApplication.getApplicationStatus().toString());
        }else{
            holder.status.setBackgroundResource(R.drawable.application_status_shortlisted);
            holder.status.setText(jobApplication.getApplicationStatus().toString());
        }



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onLongClick(v);
            }
        });

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Application Alert");
                builder.setMessage("You can not re-open this application once it's cancelled. Are you sure you want to continue?");
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                cancelApplication(jobApplication);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                v.getContext().startActivity(new Intent(v.getContext(), UserActivity.class));
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
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

    private void cancelApplication(JobApplication jobApplication) {

        jobApplication.setApplicationStatus(ApplicationStatus.CANCELLED);
        firestore.collection("Applications")
                .document(jobApplication.getApplicationId())
                .update("applicationStatus", String.valueOf(ApplicationStatus.CANCELLED))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(v.getContext(), "Cancelled successfully!", Toast.LENGTH_SHORT).show();
                    }
                });


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
        TextView applicantName, school, phone, address, email, cv, socialmedia, des, status;
        Button button;
        ImageView photo, cancelBtn;


        public ApplicationItemViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantName = itemView.findViewById(R.id.applicantName);
            school = itemView.findViewById(R.id.lamp);
            phone = itemView.findViewById(R.id.apPhone);
            address = itemView.findViewById(R.id.address);
            email = itemView.findViewById(R.id.apEmail);
            cv = itemView.findViewById(R.id.cvView);
            socialmedia = itemView.findViewById(R.id.apSocial);
            des = itemView.findViewById(R.id.apSelfdescription);
            photo = itemView.findViewById(R.id.photoPreview);
            cancelBtn = itemView.findViewById(R.id.cancel);
            status = itemView.findViewById(R.id.applicationStatus);

        }

    }
}
