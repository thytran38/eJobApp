package com.example.ejob.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejob.R;
import com.example.ejob.ui.admin.AdminActivity;
import com.example.ejob.ui.employer.EmployerActivity;
import com.example.ejob.ui.passwordrecover.ForgetPassActivity;
import com.example.ejob.ui.register.Register;
import com.example.ejob.ui.user.UserActivity;
import com.example.ejob.ui.main.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

public class LoginActivity extends AppCompatActivity implements LoginNavigator {
    EditText emailText, passwordText;
    Button login, register, forget;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    boolean valid = true;
    boolean locked = false;

    private final String TAG = "LoginActivity_Log";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        emailText = findViewById(R.id.etEmail);
        passwordText = findViewById(R.id.etPassword);
        login = findViewById(R.id.btLogin);
        register = findViewById(R.id.btRegis);
        forget = findViewById(R.id.btnForget);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields(emailText);
                checkFields(passwordText);
                if(checkFields(emailText) && checkFields(passwordText)){
                    if (valid) {
                        fAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                checkAccessAvailability(fAuth.getCurrentUser().getUid());

                                if(locked==true){
                                    startActivity(new Intent(getApplicationContext(), LockedActivity.class));

                                }else{
                                    Toast.makeText(LoginActivity.this, "Signed in successfully.", Toast.LENGTH_SHORT).show();
                                    checkUserAccessLevel(fAuth.getCurrentUser().getUid());
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Failed to sign in.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }



            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPassActivity.class));
            }
        });



    }

    private void checkAccessAvailability(String uid){
        DocumentReference df = fStore.collection("Blocked").document(uid);
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        locked =true;
                        Log.d(TAG, "Document existed");
                    }else{
                        Log.d(TAG, "Document not existed");
                    }
                }else{
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });

    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess" + documentSnapshot.getData());

                // Identify user access level
                if (documentSnapshot.getString("isAdmin") != null) {
                    // User is Employer
                    openAdminActivity();
                }

                if (documentSnapshot.getString("isEmployer") != null) {
                    // User is Employer
                    openEmployerActivity();
                }

                if (documentSnapshot.getString("isUser") != null) {
                    // User is Employer
                    openUserActivity();
                }
            }
        });
    }

    public boolean checkFields(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    @Override
    public void handleError(Throwable throwable) {

    }

    @Override
    public void login() {

    }

    @Override
    public void openAdminActivity() {
        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
        finish();
    }

    @Override
    public void openEmployerActivity() {
        startActivity(new Intent(getApplicationContext(), EmployerActivity.class));
        finish();
    }

    @Override
    public void openUserActivity() {
        startActivity(new Intent(getApplicationContext(), UserActivity.class));
        finish();
    }

}
