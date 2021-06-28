package com.example.ejob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ejob.accounts.AdminActivity;
import com.example.ejob.accounts.EmployerActivity;
import com.example.ejob.accounts.Register;
import com.example.ejob.accounts.UserActivity;
import com.example.ejob.ui.main.MainFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


    }


//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }
//    }
}