package com.example.ejob.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejob.MainActivity;
import com.example.ejob.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText fullName, email, password, phone;
    Button registerButton, loginButton;
    RadioGroup radioGroup;
    RadioButton employerCheck, userCheck;
    boolean valid = true, validRoleCheck = false, userRole = false, employerRole = false;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        firebaseAuth = firebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.etFullname);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        phone = findViewById(R.id.etPhone);
        registerButton = findViewById(R.id.btLogin);
        loginButton = findViewById(R.id.btnRegister3);
        employerCheck = findViewById(R.id.radioEmployer);
        userCheck = findViewById(R.id.radioUser);
        radioGroup = findViewById(R.id.radioGroup);
        userCheck.setChecked(true);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields(fullName);
                checkFields(email);
                checkFields(password);
                checkFields(phone);

                checkRole(radioGroup);

                if(valid && validRoleCheck){
                    // Start registration process
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("FullName",fullName.getText().toString());
                            userInfo.put("UserEmail",email.getText().toString());
                            userInfo.put("PhoneNumber", phone.getText().toString());
                            // Specify if the user is an Employer
                            if(employerRole==true){
                                userInfo.put("isEmployer", "1");
                            }else if(userRole ==true){
                                userInfo.put("isUser", "1");
                            }
                            df.set(userInfo);

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Failed to Create", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public boolean checkBox(RadioButton checkBox1, RadioButton checkBox2){
        if(checkBox1.getText().toString().equals("") && checkBox2.getText().toString().equals("")){
            Toast.makeText(Register.this, "Cannot leave this box empty.", Toast.LENGTH_SHORT).show();
            validRoleCheck = false;
        }
        else if(checkBox1.isChecked()&&checkBox2.isChecked()){
            Toast.makeText(Register.this, "Cannot register as two roles.", Toast.LENGTH_SHORT).show();
            validRoleCheck = false;
        }else if(checkBox1.isChecked()){
            employerRole = true;
            validRoleCheck = true;
        }else{
            userRole = true;
            validRoleCheck = true;
        }
        return validRoleCheck;
    }

    public boolean checkRole(RadioGroup group){
        if(group.getCheckedRadioButtonId()==employerCheck.getId()){
            employerRole = true;
            validRoleCheck=true;
        }else{
            userRole = true;
            validRoleCheck = true;
        }
        return validRoleCheck;
    }

    public boolean checkFields(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }
        else{
            valid = true;
        }
        return valid;
    }


}
