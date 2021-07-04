package com.example.ejob.ui.user.userjob;

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

import java.util.List;

public class AllJobAdapter extends RecyclerView.Adapter<AllJobAdapter.JobViewHolderForUser>{

    public List<com.example.ejob.ui.user.userjob.JobPostingforUser> mJobList;

    public AllJobAdapter(List<JobPostingforUser> mJobList) {
        this.mJobList = mJobList;
    }
    @NonNull
    @Override
    public AllJobAdapter.JobViewHolderForUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_for_user, parent, false);
        return new AllJobAdapter.JobViewHolderForUser(view);
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
        holder.tvDaysago.setText(jobPosting.getJobDeadline());
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

            employerAvatar = itemView.findViewById(R.id.job_avatar);
            jobPosition = itemView.findViewById(R.id.tvJobPosition);
            employerName = itemView.findViewById(R.id.tvEmployer);
            jobLocation = itemView.findViewById(R.id.tvJobLocation);
            tvDaysago = itemView.findViewById(R.id.tvDaysAgo);

        }
    }
}
