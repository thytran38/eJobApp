package com.example.ejob.ui.admin.employer_accounts;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ejob.data.model.EmployerModel;
import com.example.ejob.ui.employer.job.JobPosting;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmployerViewModel extends ViewModel {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db1, db2;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    private MutableLiveData<List<EmployerModel>> mListEmployerLivedata;
    private List<EmployerModel> mListEmployer;

    private ArrayList<JobPosting> jobPostingArrayList;

    public EmployerViewModel() {
        mListEmployerLivedata = new MutableLiveData<>();
        initData();
    }

    public MutableLiveData<List<EmployerModel>> getmListJobLivedata() {
        return mListEmployerLivedata;
    }

    private void initData() {
        firebaseAuth = FirebaseAuth.getInstance();
        db1 = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mListEmployer = new ArrayList<>();
        mListEmployer = getUsersFromFirestor();
        mListEmployerLivedata.setValue(mListEmployer);
    }

    private ArrayList<EmployerModel> getUsersFromFirestor() {
        ArrayList<EmployerModel> employerArrayList = new ArrayList<>();
        String employername, empID;
        DocumentSnapshot snapshot;
        EmployerModel emp1;
        HashMap<String, String> hashMap = new HashMap<>();

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
                                String empId = document.getId();

                            }
                        }
                    }
                });




        return employerArrayList;
    }

    private void getJobListOfEmployer(String empID) {
        db2.collection("Jobs")
                .whereEqualTo("empId", empID)
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
