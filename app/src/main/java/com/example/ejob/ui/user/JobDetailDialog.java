package com.example.ejob.ui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ejob.R;

public class JobDetailDialog extends DialogFragment {
    private EditText etPositionApplyfor, etFullname, etAddress1, etAddress2, etPhone, etEmail, etMessage;
    private Button chooseFileResume, chooseFileCoverletter;
    private TextView employerName, employerEmail;

    private View v;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_view_job_detail, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;

        etFullname = v.findViewById(R.id.etApplicantName);
        etPhone = v.findViewById(R.id.etApplicantPhone);
        etAddress1 = v.findViewById(R.id.etApplicantAddr);
        etAddress2 = v.findViewById(R.id.etApplicantAddr2);
        etEmail = v.findViewById(R.id.etApplicantEmail);
        etMessage = v.findViewById(R.id.etApplicantMessage);

    }
}
