package com.example.ejob.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Supplier;

import com.example.ejob.R;
import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.ui.admin.AdminActivity;
import com.example.ejob.ui.employer.EmployerActivity;
import com.example.ejob.ui.passwordrecover.ForgetPassActivity;
import com.example.ejob.ui.register.TransitRegister;
import com.example.ejob.ui.user.UserActivity;
import com.example.ejob.ui.main.MainFragment;
import com.example.ejob.ui.user.UserProfileFragment;
import com.example.ejob.ui.user.application.ViewJobDetail;
import com.example.ejob.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.MetadataChanges;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class LoginActivity extends AppCompatActivity implements LoginNavigator {
    private final String TAG = "LoginActivity_Log";
    EditText emailText, passwordText;
    Button login, register, forget;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    boolean valid = true;
    boolean locked = false;
    SharedPreferences sharedPreferences1, sharedPreferences2, sharedPreferences3;
    Role role;

    private static boolean containUpperCase(String str) {
        for (char c : str.toCharArray())
            if (Character.isUpperCase(c))
                return true;
        return false;
    }

    private static boolean containLowerCase(String str) {
        for (char c : str.toCharArray())
            if (Character.isLowerCase(c))
                return true;
        return false;
    }

    private static boolean containSpecialChar(String str) {
        String specialChars = ",./!@#$%^&*()-_+=~[]\\|{}[]";
        Supplier<Stream<String>> supplier = () -> toStream(str);
        for (char c : specialChars.toCharArray())
            if (supplier.get().anyMatch(item -> item.equals(String.valueOf(c))))
                return true;
        return false;
    }

    private static Stream<String> toStream(String text) {
        String[] res = new String[text.length()];
        for (int i = 0; i < text.length(); i++) {
            res[i] = String.valueOf(text.charAt(i));
        }
        return Arrays.stream(res);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        FirebaseApp.initializeApp(this);
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
                startActivity(new Intent(getApplicationContext(), TransitRegister.class));
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
                if (!(valEmail() && valPass())) {
                    Toast.makeText(LoginActivity.this, "Email & Pass can not be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (valid) {
                    fAuth.signInWithEmailAndPassword(emailText.getText().toString().trim(), passwordText.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            checkAccessAvailability(fAuth.getCurrentUser().getUid());

                            if (locked == true) {
                                startActivity(new Intent(getApplicationContext(), LockedActivity.class));
                            } else {
                                fetchData();

                                Toast.makeText(LoginActivity.this, "Signed in successfully.", Toast.LENGTH_SHORT).show();
                                saveData(emailText.getText().toString().trim(), fAuth.getCurrentUser());
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
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPassActivity.class));
            }
        });
    }

    private void fetchData() {


        fStore.collection("Applicants")
                .document(fAuth.getCurrentUser().getUid())
                .addSnapshotListener(MetadataChanges.INCLUDE, (data, error) ->{
                    if(error == null){
                        if(data != null && data.exists()){
                            UserProfileFragment userProfileFragment = new UserProfileFragment();
                            ApplicantModel applicantModel = new ApplicantModel();
                            applicantModel = data.toObject(ApplicantModel.class);
                            Bundle bundle = new Bundle();
                            assert applicantModel != null;
                            if(applicantModel!=null){
                                bundle.putParcelable("applicant", applicantModel);
                                userProfileFragment.setArguments(bundle);
                                Log.d("applicant_not_null","170");
                            }
                            Log.d("applicant_not_null","172");

                        }
                    }

                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionManager sessionManager = new SessionManager(LoginActivity.this);
    }

    void saveData(String email, FirebaseUser user){
        SharedPreferences sharedPreferences=getSharedPreferences("logindata",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("logincounter",true);
        editor.putString("useremail",email);
        editor.apply();
        checkUserAccessLevel(user.getUid());
    }


    private void checkAccessAvailability(String uid) {
        DocumentReference df = fStore.collection("Blocked").document(uid);
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        locked = true;
                        Log.d(TAG, "Document existed");
                    } else {
                        Log.d(TAG, "Document not existed");
                    }
                } else {
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
                    role = Role.Admin;
                    openAdminActivity();
                }

                if (documentSnapshot.getString("isEmployer") != null) {
                    // User is Employer
                    role = Role.Employer;
                    openEmployerActivity();
                }

                if (documentSnapshot.getString("isUser") != null) {
                    // User is User
                    role = Role.User;
                    openUserActivity();
                }
            }
        });
    }

    private boolean valEmail() {
        String em = emailText.getText().toString().trim();
        String emailCond = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (em.isEmpty()) {
            emailText.setError("Please enter your Email");
            return false;
        } else if (!em.matches(emailCond)) {
            emailText.setError("Invalid Email");
            return false;
        } else {
            emailText.setError(null);
            return true;
        }
    }

    private boolean valPass() {
        String pass = passwordText.getText().toString().trim();
        if (pass.isEmpty()) {
            passwordText.setError("Please enter password");
            return false;
        } else if (pass.length() < 6) {
            passwordText.setError("Password is too short!");
            return false;
        } else if (!containLowerCase(pass) || !containUpperCase(pass) || !containSpecialChar(pass) || toStream(pass).anyMatch(c -> c.equals(" "))) {
            passwordText.setError("Password must contain at least 1 upper case, lower case, special character and don't contain \" \"");
            return false;
        }
        passwordText.setError(null);
        return true;
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
        sharedPreferences2 = getSharedPreferences("LoginEmployer", Context.MODE_PRIVATE);
        startActivity(new Intent(getApplicationContext(), EmployerActivity.class));
        finish();
    }

    @Override
    public void openUserActivity() {
        startActivity(new Intent(getApplicationContext(), UserActivity.class));
        finish();
    }

}
