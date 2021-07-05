package com.example.ejob.ui.employer.job;

import android.content.IntentFilter;
import android.util.Log;
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
