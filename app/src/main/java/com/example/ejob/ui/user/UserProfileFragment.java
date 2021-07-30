package com.example.ejob.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ejob.R;
import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.ui.user.pdf.UploadPdf;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int CHOOSE_PDF_FROM_DEVICE = 1001;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    ApplicantModel applicantModel;
    ApplicantModel parsedModel;
    ImageView userAvatar, upload, change;
    boolean cvExist = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private Button pdfBrowse, uploadButton;
    private TextView filepath, cvTitle, cv;
    private EditText email, dob, school, phone, address, fullname;
    Pair<ApplicantModel, String> pair;

    public UserProfileFragment() {
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
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
//        userAvatar = (ImageView) v.findViewById(R.id.userAvatar);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.v = view;

        mapping();
        init();
        fetchData();

//        Log.d("TAG_333", applicantModel1.getApplicantFullname());
        fullname.setText(applicantModel.getApplicantFullname());
        email.setText(applicantModel.getApplicantEmail());
        dob.setText("30/12/2000");
        phone.setText(applicantModel.getApplicantPhone());
        address.setText(applicantModel.getApplicantAddress());
        school.setText(applicantModel.getApplicantUniversity());


    }

    private void init() {

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("pdf");
        firestore = FirebaseFirestore.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        applicantModel = new ApplicantModel();

    }

    private void mapping() {

        fullname = v.findViewById(R.id.etFullnameUP1);
        email = v.findViewById(R.id.etEmailUP1);
        school = v.findViewById(R.id.etSchoolUP1);
        phone = v.findViewById(R.id.etPersonalPhoneUP1);
        address = v.findViewById(R.id.etPersonalAddressUP1);
        cv = v.findViewById(R.id.pdfLinks);
        dob  = v.findViewById(R.id.etDateofBirthUP1);

        upload = v.findViewById(R.id.cvUpload);
        filepath = v.findViewById(R.id.pdfLinks);
    }

    ApplicantModel fetchData() {
        firestore.collection("Applicants")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            Log.d("TAG_query", document.getData().toString());
                            applicantModel.setApplicantAddress(document.get("applicantAddress").toString());
                            Log.d("TAG_result", applicantModel.getApplicantAddress().toString());
                            applicantModel.setApplicantEmail(document.get("applicantEmail").toString());
                            applicantModel.setApplicantFullname(document.get("applicantFullname").toString());
                            applicantModel.setApplicantID(document.get("applicantID").toString());
                            applicantModel.setApplicantPhone(document.get("applicantPhone").toString());
                            applicantModel.setApplicantSocialmedia(document.get("applicantSocialmedia").toString());
                            applicantModel.setApplicantUniversity(document.get("applicantUniversity").toString());
                        }
                    }
                });


        DatabaseReference cvRef = firebaseDatabase.getReference("cvUploads");

        cvRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseAuth.getCurrentUser().getUid()).exists()) {
                    cvExist = true;
                    for (DataSnapshot child : snapshot.getChildren()) {
                        filepath.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("fileName").getValue().toString());

                        String cvLink = snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("cvURL").getValue().toString();
                        filepath.setText(cvLink);
                    }
                } else {
                    filepath.setText("Click icon on the right to upload CV");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return applicantModel;
    }

}