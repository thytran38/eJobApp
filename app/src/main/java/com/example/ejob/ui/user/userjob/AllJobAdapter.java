package com.example.ejob.ui.user.userjob;

import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.ui.employer.job.JobAdapter;
import com.example.ejob.ui.employer.job.JobPosting;
import com.example.ejob.utils.Date;

import java.util.List;

public class AllJobAdapter extends RecyclerView.Adapter<AllJobAdapter.JobViewHolderForUser>{

    public List<com.example.ejob.ui.user.userjob.JobPostingforUser> mJobList;
    private ItemClickListener itemClickListener;

    public AllJobAdapter(List<JobPostingforUser> mJobList, ItemClickListener itemClickListener1) {
        this.mJobList = mJobList;
        this.itemClickListener = itemClickListener1;
    }
    @NonNull
    @Override
    public AllJobAdapter.JobViewHolderForUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jobuser_cardview, parent, false);
        return new AllJobAdapter.JobViewHolderForUser(view);
    }

    public interface ItemClickListener{
        void onItemClick(JobPostingforUser jobPost);
    }


    @Override
    public void onBindViewHolder(@NonNull AllJobAdapter.JobViewHolderForUser holder, int position) {
        JobPostingforUser jobPosting = mJobList.get(position);
        if(jobPosting == null){
            return;
        }
//        holder.employerAvatar.setImageResource(jobPosting.getImageUrl());
        holder.employerName.setText(jobPosting.getEmployerName());
        holder.jobPosition.setText(jobPosting.getJobTitle());
        holder.jobLocation.setText(jobPosting.getJobLocation());

        Date datecurrent = Date.getInstance();
        long dateCurrent = datecurrent.getEpochSecond();
        Date datePosted = Date.getInstance(Long.parseLong(jobPosting.getJobDateCreated()));
        long daysDiffInEpoch;
        Date dateDiff;
        try{
            Long dateCreated = Long.parseLong(jobPosting.getJobDateCreated());
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

        holder.itemView.setOnClickListener(view -> {
            itemClickListener.onItemClick(mJobList.get(position));
        });
    }


    @Override
    public int getItemCount() {
        if(mJobList != null) {
            return mJobList.size();
        }
        return 0;
    }

    public class JobViewHolderForUser extends RecyclerView.ViewHolder{

        private ImageView employerAvatar;
        private TextView jobPosition, employerName, jobLocation, tvDaysago;


        public JobViewHolderForUser(@NonNull View itemView) {
            super(itemView);

            employerAvatar = itemView.findViewById(R.id.photoPreview_user);
            jobPosition = itemView.findViewById(R.id.tvJobPosition_user);
            employerName = itemView.findViewById(R.id.tvEmployer_user);
            jobLocation = itemView.findViewById(R.id.tvJobLocation_user);
            tvDaysago = itemView.findViewById(R.id.tvDaysAgo_user);

        }
    }
}
