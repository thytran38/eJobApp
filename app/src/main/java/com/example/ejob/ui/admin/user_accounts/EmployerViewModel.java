package com.example.ejob.ui.admin.user_accounts;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

public class EmployerViewModel extends ViewModel {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db1, db2;
    FirebaseUser firebaseUser;

    private MutableLiveData<List<Employer>> mListEmployerLivedata;
    private List<Employer> mListEmployer;

    private ArrayList<JobPosting> jobPostingArrayList;

    public EmployerViewModel() {
        mListEmployerLivedata = new MutableLiveData<>();
        initData();
    }

    public MutableLiveData<List<Employer>> getmListJobLivedata() {
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

    private ArrayList<Employer> getUsersFromFirestor() {
        ArrayList<Employer> employerArrayList = new ArrayList<>();
        String employername;
        DocumentSnapshot snapshot;
//        DocumentReference df = firebaseFirestore.collection("Users").document(uid);

        db1.collection("Users")
                .whereEqualTo("isEmployer", "1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                Employer emp = new Employer();
                                emp.setEmployerName(document.getString("FullName").toString());

                                try {
                                    emp.setPhoneNumber(document.getString("PhoneNumber"));
                                } catch (NullPointerException npe) {
                                    npe.getMessage();
                                }

                                emp.setEmployerEmail(document.get("UserEmail").toString());
                                employerArrayList.add(emp);

                                Log.d("TAG", document.getId() + " => " + emp.getEmployerName());
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
