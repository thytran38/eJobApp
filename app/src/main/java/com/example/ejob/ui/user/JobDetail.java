package com.example.ejob.ui.user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejob.R;

public class JobDetail extends AppCompatActivity {

    private EditText etPositionApplyfor, etFullname, etAddress1, etAddress2, etPhone, etEmail, etMessage;
    private Button chooseFileCV, submitApplication;

    Bundle bundle;
    private TextView employerName, positionHiring, dateCreated;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_apply);


//        employerName.setText(String.valueOf(getIntent().getIntExtra("employerName", 1)));
//        positionHiring.setText(String.valueOf(getIntent().getIntExtra("positionHiring",0)));
//        dateCreated.setText(String.valueOf(getIntent().getIntExtra("dateCreated",0)));


    }
}
