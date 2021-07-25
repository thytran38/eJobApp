package com.example.ejob.ui.employer.applications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.ui.employer.job.JobAdapter;
import com.example.ejob.ui.employer.job.JobPosting;
import com.example.ejob.ui.user.application.JobApplication;

import java.util.List;

public class SubApplicationAdapter extends RecyclerView.Adapter<SubApplicationAdapter.ApplicationItemViewHolder> {

    public List<JobApplication> mApplicationList;
    private JobAdapter.ItemClickListener onItemClickListener;

    public SubApplicationAdapter(List<JobApplication> appList, JobAdapter.ItemClickListener itemClickListener){
        this.mApplicationList = appList;
        onItemClickListener = itemClickListener;
    }

    public SubApplicationAdapter(int numberApplied) {
    }

    @NonNull
    @Override
    public ApplicationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);

        return new ApplicationItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationItemViewHolder holder, int position) {
        JobApplication subItem = mApplicationList.get(position);

        //holder.applicantName  = // chua co
//        holder.address = subItem.
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ApplicationItemViewHolder extends RecyclerView.ViewHolder{
        TextView applicantName, school, phone, address, email, cv;
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

        }
    }
}
