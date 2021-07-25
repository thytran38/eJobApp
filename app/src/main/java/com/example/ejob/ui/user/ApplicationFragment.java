package com.example.ejob.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ejob.R;
import com.example.ejob.ui.user.userjob.AllJobAdapter;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.ui.user.userjob.UserAllJobViewModel;

import java.util.List;

public class ApplicationFragment extends androidx.fragment.app.Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView jobRecyclerView;
    private UserAllJobViewModel userAllJobView;
    private AllJobAdapter allJobAdapter;

    private ViewGroup viewgroupContainer;
    private LayoutInflater layoutInflater;

    private SwipeRefreshLayout swipeRefreshLayout;

    View v;

    private String mParam1;
    private String mParam2;

    public ApplicationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ApplicationFragment newInstance(String param1, String param2) {
        ApplicationFragment fragment = new ApplicationFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.v = view;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeUserJob);


        jobRecyclerView = (RecyclerView) v.findViewById(R.id.rcvJobUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        jobRecyclerView.setLayoutManager(linearLayoutManager);
        userAllJobView = new ViewModelProvider(this).get(UserAllJobViewModel.class);

        userAllJobView.getmListJobLivedata().observe(getViewLifecycleOwner(), new Observer<List<JobPostingforUser>>() {
            @Override
            public void onChanged(List<JobPostingforUser> jobPostings) {
                allJobAdapter = new AllJobAdapter(jobPostings, new AllJobAdapter.ItemClickListener() {

                    @Override
                    public void onItemClick(JobPostingforUser jobPost) {

                        FragmentManager fragmentManager = getChildFragmentManager();
                        layoutInflater.inflate(R.layout.fragment_application_job,viewgroupContainer, false);
                        Toast.makeText(ApplicationFragment.this.getContext(),jobPost.getJobTitle(),Toast.LENGTH_LONG).show();
                    }

                });
                jobRecyclerView.setAdapter(allJobAdapter);

            }


        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allJobAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewgroupContainer = container;
        layoutInflater = inflater;
        return inflater.inflate(R.layout.fragment_job_detail, container, false);    }
}
