package com.example.ejob.ui.admin.user_accounts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.ui.admin.employer_accounts.Employer;
import com.example.ejob.utils.Date;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.EmployerViewHolder> {

    public List<Employer> mEmployerList;
    public Context context;
    Boolean testClick = false;
    private ItemClickListener itemClickListener;
    private DatabaseReference appliedReference;


    public UserAdapter(List<Employer> mEmployerList, ItemClickListener itemClickListener1) {
        this.mEmployerList = mEmployerList;
        this.itemClickListener = itemClickListener1;
    }

    @NonNull
    @Override
    public EmployerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employer_account_item, parent, false);
        return new EmployerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployerViewHolder holder, int position) {
        Employer employer = mEmployerList.get(position);
        if (employer == null) {
            return;
        }
//        holder.employerAvatar.setImageResource(jobPosting.getImageUrl());
        holder.employerName.setText(employer.getEmployerName());

        holder.unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Date datecurrent = Date.getInstance();
        long dateCurrent = datecurrent.getEpochSecond();
//        Date datePosted = Date.getInstance(Long.parseLong(employer.getJobDateCreated()));
        long daysDiffInEpoch;
        Date dateDiff;

//        try {
////            Long dateCreated = Long.parseLong(employer.getJobDateCreated());
//            String dateShown = datePosted.toString();
//            Log.d("TAG1", String.valueOf(dateCurrent));
//            Log.d("TAG2", dateShown);
//            daysDiffInEpoch = dateCurrent - dateCreated;
//            int daysTogo = (int) daysDiffInEpoch / 86400;
//            Log.d("TAG3", String.valueOf(daysDiffInEpoch));
//            if (daysTogo < 1) {
//                holder.tvDaysago.setText("Today");
//            } else if (daysTogo < 2) {
//                holder.tvDaysago.setText(daysTogo + " day ago");
//            } else {
//                holder.tvDaysago.setText(daysTogo + " days ago");
//            }
//        } catch (NumberFormatException e) {
//            Log.d("NBE", e.getMessage());
//        }



//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), ViewAccountDetail.class);
////            intent.putExtra("employerName", mJobList.get(position).getEmployerName());
////            intent.putExtra("positionHiring", mJobList.get(position).getJobTitle());
////            intent.putExtra("dateCreated", mJobList.get(position).getJobDateCreated());
////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                v.getContext().startActivity(intent);
//            }
//        });

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
        private TextView jobPosition, employerName, empLocation, tvDaysago, jobsNumber, accountStatus;

        public EmployerViewHolder(@NonNull View itemView) {
            super(itemView);

            mapping();

        }

        private void mapping() {
            unlock = itemView.findViewById(R.id.lockIcon);
            employerName = itemView.findViewById(R.id.tvEmpName);
            empLocation = itemView.findViewById(R.id.address);
            tvDaysago = itemView.findViewById(R.id.accountCreationDate);
            jobsNumber = itemView.findViewById(R.id.tvJobsNumber);
            accountStatus = itemView.findViewById(R.id.accStatus);
        }

        public void getAppliedNumber(String postId) {
            appliedReference = FirebaseDatabase.getInstance().getReference("userapplications");
            appliedReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(postId.replaceAll(".*/", "")).exists()) {
                        int appCount = (int) snapshot.child(postId.replaceAll(".*/", "")).getChildrenCount();
                        jobsNumber.setText(String.valueOf(appCount));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


}
