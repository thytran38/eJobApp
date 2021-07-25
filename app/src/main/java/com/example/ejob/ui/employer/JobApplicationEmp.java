package com.example.ejob.ui.employer;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ejob.R;
import com.example.ejob.ui.employer.applications.MyJobsAdapter;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.ui.user.userjob.UserAllJobViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobApplicationEmp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobApplicationEmp extends androidx.fragment.app.Fragment {

    private TextView jobTitle, jobId, jobType;
    private RecyclerView jobRcv;
    MyJobsAdapter mAdapter;

    UserAllJobViewModel userAllJobViewModel;


    View v;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JobApplicationEmp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobApplicationEmp.
     */
    // TODO: Rename and change types and number of parameters
    public static JobApplicationEmp newInstance(String param1, String param2) {
        JobApplicationEmp fragment = new JobApplicationEmp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_application_emp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;

        jobTitle = v.findViewById(R.id.tvJobTitleA);
        jobId = v.findViewById(R.id.tvjobid);
        jobType = v.findViewById(R.id.jobTypeEmp);
        jobRcv = v.findViewById(R.id.rcvJobEmp);

        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        jobRcv.setLayoutManager(layoutManager);

        userAllJobViewModel = new ViewModelProvider(this).get(UserAllJobViewModel.class);
        userAllJobViewModel.getmListJobLivedata().observe(getViewLifecycleOwner(), new Observer<List<JobPostingforUser>>() {
            @Override
            public void onChanged(List<JobPostingforUser> jobPostingforUsers) {
                mAdapter = new MyJobsAdapter(jobPostingforUsers);
            }
        });


        jobRcv.setAdapter(mAdapter);

    }

}