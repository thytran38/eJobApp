package com.example.ejob.ui.employer.applications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.ui.employer.job.JobPosting;
import com.example.ejob.ui.user.userjob.JobPostingforUser;

import java.util.List;

public class MyJobsAdapter extends RecyclerView.Adapter<MyJobsAdapter.JobInfoViewHolder>{

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<JobPostingforUser> jobList;

    public MyJobsAdapter(List<JobPostingforUser> itemList){
        this.jobList = itemList;
    }

    @NonNull
    @Override
    public JobInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_job_application_emp, parent, false);
        return new JobInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobInfoViewHolder holder, int position) {
        JobPostingforUser job = jobList.get(position);
        holder.jobTitle.setText(job.getJobTitle());


        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rvApplications.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(job.getNumberApplied());

        // Create sub item view adapter
        SubApplicationAdapter subItemAdapter = new SubApplicationAdapter(job.getNumberApplied());

        holder.rvApplications.setLayoutManager(layoutManager);
        holder.rvApplications.setAdapter(subItemAdapter);
        holder.rvApplications.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class JobInfoViewHolder extends RecyclerView.ViewHolder{

        private TextView jobTitle;
        private TextView jobID;
        private TextView jobType;
        private RecyclerView rvApplications;

        public JobInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.tvJobTitleA);
            jobID = itemView.findViewById(R.id.tvjobid);
            jobType = itemView.findViewById(R.id.jobTypeEmp);
            rvApplications = itemView.findViewById(R.id.rcvJobEmp);

        }
    }
}
