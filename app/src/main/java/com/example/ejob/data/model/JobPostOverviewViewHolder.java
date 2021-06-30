package com.example.ejob.data.model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.utils.Date;

public class JobPostOverviewViewHolder extends RecyclerView.ViewHolder {
    private TextView tvShortName, tvTransactionId, tvTransactionPerformTime, tvTransactionValue;


    public JobPostOverviewViewHolder(@NonNull View itemView) {
        super(itemView);
        tvShortName = itemView.findViewById(R.id.textView);
    }


    public void setShortName(String fullName) {
        String[] words = fullName.split("\\s+");
        String shortName = (String.valueOf(words[words.length - 1].charAt(0)) + String.valueOf(words[0].charAt(0))).toUpperCase();
        tvShortName.setText(shortName);
    }

    public void setTransactionId(String transactionId) {
        tvTransactionId.setText(transactionId);
    }

    public void setTransactionPerformTime(long epochTime) {
        tvTransactionPerformTime.setText(Date.getInstance(epochTime).toString(true));
    }

}
