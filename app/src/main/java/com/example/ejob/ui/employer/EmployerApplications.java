//package com.example.ejob.ui.employer;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.FragmentManager;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.example.ejob.R;
//import com.example.ejob.ui.user.UserHomeFragment;
//import com.example.ejob.ui.user.userjob.AllJobAdapter;
//import com.example.ejob.ui.user.userjob.JobPostingforUser;
//import com.example.ejob.ui.user.userjob.UserAllJobViewModel;
//
//import java.util.List;
//
//public class EmployerApplications extends androidx.fragment.app.Fragment {
//
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private RecyclerView applicationRecyclerView;
//    private AllApplicationViewModel allApplications;
//    private AllJobAdapter allJobAdapter;
//
//    private ViewGroup viewgroupContainer;
//    private LayoutInflater layoutInflater;
//    private SwipeRefreshLayout swipeRefreshLayout;
//
//    View v;
//
//    private static JobPostingforUser jobPostingU;
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public EmployerApplications() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment UserHomeFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static EmployerApplications newInstance(String param1, String param2) {
//        EmployerApplications fragment = new EmployerApplications();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        this.v = view;
//        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeUserJob);
//
//
//        applicationRecyclerView = (RecyclerView) v.findViewById(R.id.rcvApplication);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
//        applicationRecyclerView.setLayoutManager(linearLayoutManager);
//        allApplications = new ViewModelProvider(this).get(AllApplicationViewModel.class);
//
//        allApplications.getmListJobLivedata().observe(getViewLifecycleOwner(), new Observer<List<JobPostingforUser>>() {
//            @Override
//            public void onChanged(List<JobPostingforUser> jobPostings) {
//                allJobAdapter = new AllJobAdapter(jobPostings, new AllJobAdapter.ItemClickListener() {
//
//                    @Override
//                    public void onItemClick(JobPostingforUser jobPost) {
//
//                        FragmentManager fragmentManager = getChildFragmentManager();
//                        layoutInflater.inflate(R.layout.fragment_application_job,viewgroupContainer, false);
//                        Toast.makeText(EmployerApplications.this.getContext(),jobPost.getJobTitle(),Toast.LENGTH_LONG).show();
//                        Log.d("TAG_UHFragment", jobPost.getJobTitle());
//                    }
//
//                });
//                applicationRecyclerView.setAdapter(allJobAdapter);
//
//            }
//
//
//        });
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                allJobAdapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//
//    }
//}
