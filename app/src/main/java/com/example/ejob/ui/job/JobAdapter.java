package com.example.ejob.ui.job;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.utils.Date;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder>{

    public List<JobPosting> mJobList;

    public JobAdapter(List<JobPosting> mJobList) {
        this.mJobList = mJobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobPosting jobPosting = mJobList.get(position);
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

//    public Date getDateDifference(Long jobDateCreated){
//        long diff =  - jobDateCreated;
//    }

    public class JobViewHolder extends RecyclerView.ViewHolder{

        private ImageView employerAvatar;
        private TextView jobPosition, employerName, jobLocation, tvDaysago;


        public JobViewHolder(@NonNull View itemView) {
            super(itemView);

            employerAvatar = itemView.findViewById(R.id.job_avatar);
            jobPosition = itemView.findViewById(R.id.tvJobPosition);
            employerName = itemView.findViewById(R.id.tvEmployer);
            jobLocation = itemView.findViewById(R.id.tvJobLocation);
            tvDaysago = itemView.findViewById(R.id.tvDaysAgo);

        }
    }
}
