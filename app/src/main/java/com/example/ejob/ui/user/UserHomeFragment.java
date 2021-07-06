
package com.example.ejob.ui.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.ejob.R;
import com.example.ejob.ui.employer.job.JobAdapter;
import com.example.ejob.ui.employer.job.JobPosting;
import com.example.ejob.ui.employer.job.JobViewModel;
import com.example.ejob.ui.user.userjob.AllJobAdapter;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.ui.user.userjob.UserAllJobViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView jobRecyclerView;
    private UserAllJobViewModel userAllJobView;
    private AllJobAdapter allJobAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHomeFragment newInstance(String param1, String param2) {
        UserHomeFragment fragment = new UserHomeFragment();
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
        super.onViewCreated(view, savedInstanceState);
        jobRecyclerView = (RecyclerView) view.findViewById(R.id.rcvJobUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        jobRecyclerView.setLayoutManager(linearLayoutManager);
//        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(this.getContext(), R.anim.down_to_up);
//        jobRecyclerView.setLayoutAnimation(layoutAnimationController);
        userAllJobView = new ViewModelProvider(this).get(UserAllJobViewModel.class);

        userAllJobView.getmListJobLivedata().observe(getViewLifecycleOwner(), new Observer<List<JobPostingforUser>>() {
            @Override
            public void onChanged(List<JobPostingforUser> jobPostings) {
                allJobAdapter = new AllJobAdapter(jobPostings, new AllJobAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(JobPostingforUser jobPost) {
                        JobDetailDialog jobDetailDialog = new JobDetailDialog();
                        Toast.makeText(UserHomeFragment.this.getContext(),jobPost.getJobTitle(),Toast.LENGTH_LONG).show();
                        Log.d("TAG_UHFragment", jobPost.getJobTitle());
                    }

                });
                jobRecyclerView.setAdapter(allJobAdapter);

            }
        });


    }

    private void showToast(String message){
        Toast.makeText(UserHomeFragment.this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_detail, container, false);


    }

    private void setAnimation(int animate, List<JobPostingforUser> jobPostings){
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(this.getContext(), animate);
        jobRecyclerView.setLayoutAnimation(layoutAnimationController);

        allJobAdapter = new AllJobAdapter(jobPostings, new AllJobAdapter.ItemClickListener() {
            @Override
            public void onItemClick(JobPostingforUser jobPost) {
                Toast.makeText(UserHomeFragment.this.getContext(),jobPost.getJobTitle(),Toast.LENGTH_LONG).show();
                Log.d("TAG_UHFragment", jobPost.getJobTitle());
            }

        });
        jobRecyclerView.setAdapter(allJobAdapter);

    }
}