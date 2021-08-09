package com.example.ejob.ui.user.applications;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ApplicationAdapterforUser extends RecyclerView.Adapter<ApplicationAdapterforUser.ApplicationItemViewHolder> {

    public List<JobApplication> mApplicationList;
    private ItemClickListener itemClickListener;
    JobApplication jobApplication;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userapplicationref;
    private StorageReference storage;
    private FirebaseUser firebaseUser;
    View v;
    JobApplication subItem;
    String jobId;
    boolean cancelled = false;
    ApplicationItemViewHolder holder1;
    Notification notificationManager;


    public ApplicationAdapterforUser(List<JobApplication> appList, ItemClickListener itemClickListener1) {
        this.mApplicationList = appList;
        this.itemClickListener = itemClickListener1;
    }

    @NonNull
    @Override
    public ApplicationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item_user, parent, false);
        this.v = view;
        firestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userapplicationref = firebaseDatabase.getReference("userapplications");

        return new ApplicationItemViewHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull ApplicationItemViewHolder holder) {
        super.onViewRecycled(holder);
        checkApplicationStatus(holder);

        if(jobApplication.getApplicationStatus().equals("CANCELLED") || jobApplication.getApplicationStatus().equals("SHORTLISTED")){
            holder.cancelBtn.setVisibility(View.INVISIBLE);
            holder.cancelBtn.setEnabled(false);
        }else{
            holder.cancelBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationItemViewHolder holder, int position) {
        this.holder1 = holder;
        jobApplication = mApplicationList.get(position);

//        storage = firebaseDatabase.
        if(jobApplication == null){
            return;
        }

        holder.applicantName.setText(jobApplication.getApplicantFullname());
        holder.address.setText(jobApplication.getApplicantAddress());
        holder.school.setText(jobApplication.getApplicantUniversity());
        holder.applicant.setText(jobApplication.getApplicantFullname());
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
        holder.status.setText(jobApplication.getApplicationStatus().toString());

        checkApplicationStatus(holder);

        if(holder.status.getText().toString().equals("CANCELLED") || holder.status.getText().toString().equals("SHORTLISTED")){
            holder.cancelBtn.setVisibility(View.INVISIBLE);
            holder.cancelBtn.setEnabled(false);
        }else{
            holder.cancelBtn.setVisibility(View.VISIBLE);
        }

        holder.applicantName.setText(jobApplication.getPosition());

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
//                                    try {
//                                        Thread.sleep(500);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                    v.getContext().startActivity(new Intent(v.getContext(), UserActivity.class));
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Archived this application?", Toast.LENGTH_SHORT).show();
            }
        });


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPdf();
            }
        });

    }

    private void doNothing() {
        Toast.makeText(v.getContext(), "Cannot cancel", Toast.LENGTH_SHORT).show();
    }

    private ApplicationStatus checkApplicationStatus(@NonNull ApplicationItemViewHolder holder) {
        if(jobApplication.getApplicationStatus().equals("CANCELLED")){
            holder.status.setBackgroundResource(R.drawable.application_status_cancelled);
            holder.status.setText(jobApplication.getApplicationStatus().toString());
            return ApplicationStatus.CANCELLED;
        }else if(jobApplication.getApplicationStatus().equals("SUBMITTED")){
            holder.status.setBackgroundResource(R.drawable.application_status_submitted);
            holder.status.setText(jobApplication.getApplicationStatus().toString());
            return ApplicationStatus.SUBMITTED;
        }else{
            holder.status.setBackgroundResource(R.drawable.application_status_shortlisted);
            holder.status.setText(jobApplication.getApplicationStatus().toString());
            return ApplicationStatus.SHORTLISTED;
        }
    }

    private void reduceAppliedNumber() {



    }

    private void cancelApplication(JobApplication jobApplication) {

        Runnable runnable = () -> {
            jobApplication.setApplicationStatus(ApplicationStatus.CANCELLED);
            firestore.collection("Applications")
                    .document(jobApplication.getApplicationId())
                    .update("applicationStatus", String.valueOf(ApplicationStatus.CANCELLED))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(v.getContext(), "Cancelled successfully!", Toast.LENGTH_SHORT).show();
                            cancelled = true;
                        }
                    });

            userapplicationref
                    .child(jobApplication.getJobID())
                    .child(firebaseAuth.getCurrentUser().getUid())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
        };
        Thread thread = new Thread(runnable);

        thread.start();


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
        TextView applicantName, school, phone, address, email, cv, socialmedia, des, status, applicant;
        Button button;
        ImageView photo, cancelBtn;


        public ApplicationItemViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantName = itemView.findViewById(R.id.applicationName1);
            applicant = itemView.findViewById(R.id.applicantName3);
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
