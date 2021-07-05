package com.example.ejob.data.db;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ejob.ui.admin.AdminActivity;
import com.example.ejob.ui.employer.EmployerActivity;
import com.example.ejob.ui.user.UserActivity;
import com.example.ejob.utils.Date;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.MetadataChanges;

import java.util.Objects;

public class FetchingService extends Service {

    public static final String INTENT_KEY = "Firestore.Data";
    public static final String USER_INFO_KEY = "UserInfo";

    public static final String SEND_USER_UID_KEY = "USER_UID";
    private static final String CHANNEL_ID = "ApplicationNewReviewChannel";
    private static int accessLevelNumber;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String uid = intent.getStringExtra(SEND_USER_UID_KEY);
        int accessLevel;
        FirebaseFirestore db = FirebaseFirestore.getInstance(Objects.requireNonNull(FirebaseApp.initializeApp(this)));

        final long currentTime = Date.getInstance().getEpochSecond();

        new Thread(() ->{

            db.collection("Users")
                    .document(uid)
                    .addSnapshotListener(MetadataChanges.INCLUDE,(data,error) -> {
                        if(error == null){
                            if(data != null && data.exists()){
                                Intent intentData = new Intent(INTENT_KEY + " . " + USER_INFO_KEY);

                            }
                        }


            });





        });




        return super.onStartCommand(intent, flags, startId);

    }


    private int checkUserAccessLevel(String uid) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference df = firebaseFirestore.collection("Users").document(uid);
        int accessLevel = 0;
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess" + documentSnapshot.getData());

                // Identify user access level
                if (documentSnapshot.getString("isAdmin") != null) {
                    // User is Employer
                    accessLevelNumber = 1;

                }

                if (documentSnapshot.getString("isEmployer") != null) {
                    // User is Employer
                    accessLevelNumber = 2;


                }

                if (documentSnapshot.getString("isUser") != null) {
                    // User is Employer
                    accessLevelNumber = 3;
                }
            }
        });
        return accessLevelNumber;
    }


}
