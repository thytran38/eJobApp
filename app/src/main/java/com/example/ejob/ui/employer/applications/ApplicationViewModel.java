package com.example.ejob.ui.employer.applications;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ejob.data.model.ApplicationStatus;
import com.example.ejob.ui.employer.RecruiteType;
import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ApplicationViewModel extends ViewModel {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    private MutableLiveData<List<JobApplication>> mApplicationLiveData;
    private List<JobApplication> mApplicationList;

    public MutableLiveData<List<JobApplication>> getmListApplicationsLivedata() {
        return mApplicationLiveData;
    }

    public ApplicationViewModel(String jobId){
        mApplicationLiveData = new MutableLiveData<>();
        initData(jobId);
    }

    private void initData(String jobID) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid().toString();
        mApplicationList = new ArrayList<>();
        mApplicationList = getApplicationsFromFirestore(jobID);
        mApplicationLiveData.setValue(mApplicationList);
    }

    private List<JobApplication> getApplicationsFromFirestore(String jobId) {
        ArrayList<JobApplication> applicationList = new ArrayList<>();
        String employername;
        DocumentSnapshot snapshot;

        firebaseFirestore.collection("Applications")
                .whereEqualTo("jobId", jobId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                JobApplication application = new JobApplication();
                                application.setApplicationId(document.getReference().getPath());
                                String selfDescript = document.get("selfDescription").toString();
                                application.setSelfDescription(selfDescript);
                                String statusStr = document.get("applicationStatus").toString();
                                ApplicationStatus status = ApplicationStatus.valueOf(statusStr);
                                application.setApplicationStatus(status);
                                application.setApplicationDate(document.get("applicationDate").toString());
                                application.setCvitaeLink(document.get("cvitaeLink").toString());
                                application.setPosition(document.get("position").toString());


                                applicationList.add(application);
                            }
                        }
                    }
                });


        return applicationList;

    }


}
