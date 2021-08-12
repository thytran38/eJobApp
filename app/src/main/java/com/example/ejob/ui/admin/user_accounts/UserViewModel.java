package com.example.ejob.ui.admin.user_accounts;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.ui.admin.employer_accounts.Employer;
import com.example.ejob.ui.employer.job.JobPosting;
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

public class UserViewModel extends ViewModel {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db1, db2;
    FirebaseUser firebaseUser;
    ApplicantModel user;

    private MutableLiveData<List<ApplicantModel>> mListEmployerLivedata;
    private List<ApplicantModel> mListEmployer;

    private ArrayList<ApplicantModel> jobPostingArrayList;

    public UserViewModel() {
        mListEmployerLivedata = new MutableLiveData<>();
        initData();
    }

    public MutableLiveData<List<ApplicantModel>> getmListJobLivedata() {
        return mListEmployerLivedata;
    }

    private void initData() {
        firebaseAuth = FirebaseAuth.getInstance();
        db1 = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mListEmployer = new ArrayList<>();
        mListEmployer = getUsersFromFirestor();
        mListEmployerLivedata.setValue(mListEmployer);
    }

    private ArrayList<ApplicantModel> getUsersFromFirestor() {
        ArrayList<ApplicantModel> employerArrayList = new ArrayList<>();
        String employername;
        DocumentSnapshot snapshot;
//        DocumentReference df = firebaseFirestore.collection("Users").document(uid);

        db1.collection("Users")
                .whereEqualTo("isUser", "1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user = new ApplicantModel();
                                user.setApplicantEmail(document.get("Email").toString());

                                try {
                                } catch (NullPointerException npe) {
                                    npe.getMessage();
                                }

                                employerArrayList.add(user);

                            }
                        }
                    }
                });


        return employerArrayList;
    }

    private void getJobListOfEmployer() {
        db2.collection("Jobs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                            }
                        }
                    }
                });
    }


}
