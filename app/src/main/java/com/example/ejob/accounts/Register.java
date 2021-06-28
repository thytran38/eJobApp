package com.example.ejob.accounts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejob.R;

public class Register extends AppCompatActivity {
    EditText fullName, email, password, phone;
    Button registerButton, loginButton;
    boolean valid = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        fullName = findViewById(R.id.etFullname);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        phone = findViewById(R.id.etPhone);

        checkFields(fullName);
        checkFields(email);
        checkFields(password);
        checkFields(phone);


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
