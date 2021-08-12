package com.example.ejob.ui.admin.employer_accounts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.data.model.EmployerModel;
import com.example.ejob.ui.user.application.ViewJobDetail;
import com.example.ejob.ui.user.userjob.AllJobAdapter;
import com.example.ejob.utils.Date;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.EmployerViewHolder> {

    public List<EmployerModel> mEmployerList;
    public Context context;
    Boolean testClick = false;
    private ItemClickListener itemClickListener;
    private DatabaseReference lockRef;
    private FirebaseAuth fAuth;
    private FirebaseDatabase fDatabase;
    private FirebaseFirestore fStore;


    public EmployerAdapter(List<EmployerModel> mEmployerList, ItemClickListener itemClickListener1) {
        this.mEmployerList = mEmployerList;
        this.itemClickListener = itemClickListener1;
    }

    @NonNull
    @Override
    public EmployerAdapter.EmployerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employer_account_item, parent, false);
        return new EmployerAdapter.EmployerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployerAdapter.EmployerViewHolder holder, int position) {
        EmployerModel employer = mEmployerList.get(position);
        if (employer == null) {
            return;
        }

        init();
//        holder.employerAvatar.setImageResource(jobPosting.getImageUrl());
        holder.employerName.setText(employer.getEmployerFullname());
        holder.empLocation.setText(employer.getEmployerAddress());
        holder.industry.setText(employer.getEmployerIndustry());
        Date datecurrent = Date.getInstance();
        long dateCurrent = datecurrent.getEpochSecond();
        Date datePosted = Date.getInstance(Long.parseLong(employer.getDateCreationEmployer()));
        long daysDiffInEpoch;
        Date dateDiff;
        try{
            Long dateCreated = Long.parseLong(employer.getDateCreationEmployer());
            String dateShown = datePosted.toString();
            Log.d("TAG1" , String.valueOf(dateCurrent));
            Log.d("TAG2", dateShown);
            daysDiffInEpoch = dateCurrent - dateCreated;
            int daysTogo = (int) daysDiffInEpoch / 86400;
            Log.d("TAG3", String.valueOf(daysDiffInEpoch));
            if(daysTogo < 1){
                holder.tvDaysago.setText("Today");
            }
            else if(daysTogo < 2 )
            {
                holder.tvDaysago.setText(daysTogo + " day ago");
            }
            else{
                holder.tvDaysago.setText(daysTogo + " days ago");
            }
        }catch(NumberFormatException e){
            Log.d("NBE", e.getMessage());
        }
        holder.getLockStatus(employer.getEmployerId());


        holder.unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void init() {
        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        fStore = FirebaseFirestore.getInstance();
        lockRef = fDatabase.getReference("Blocked");
    }


    @Override
    public int getItemCount() {
        if (mEmployerList != null) {
            return mEmployerList.size();
        }
        return 0;
    }


    public interface ItemClickListener {
        void onItemClick(Employer employer);
    }

    public class EmployerViewHolder extends RecyclerView.ViewHolder {

        private ImageView employerAvatar, unlock;
        private TextView jobPosition, employerName, empLocation, tvDaysago, jobsNumber, accountStatus, industry;

        public EmployerViewHolder(@NonNull View itemView) {
            super(itemView);

            mapping();
        }

        private void getLockStatus(String uid) {
            lockRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(uid).exists()){
                        unlock.setImageResource(R.drawable.ic_baseline_lock_24);
                        accountStatus.setText("Locked");
                        accountStatus.setBackgroundResource(R.drawable.bg_account_type_blocked);
                    }else{
                        unlock.setImageResource(R.drawable.ic_baseline_lock_open_24);
                        accountStatus.setText("Available");
                        accountStatus.setBackgroundResource(R.drawable.bg_account_type_available);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void mapping() {
            unlock = itemView.findViewById(R.id.lockIcon);
            employerName = itemView.findViewById(R.id.tvEmpName);
            empLocation = itemView.findViewById(R.id.address);
            tvDaysago = itemView.findViewById(R.id.accountCreationDate);
            jobsNumber = itemView.findViewById(R.id.tvJobsNumber);
            accountStatus = itemView.findViewById(R.id.accStatus);
            industry = itemView.findViewById(R.id.tvIndustry);
        }

    }

}
