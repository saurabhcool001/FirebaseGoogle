package com.example.saurabh.firebasegoogle;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    private Button mLogOut, mVarifyEmail;
    private TextView mUserName, mUserEmail;
    private ImageView mUserImage;
    public static final String TAG = "ACCOUNT_ACTIVITY";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mLogOut = (Button) findViewById(R.id.logOut);
        mUserName = (TextView) findViewById(R.id.userName);
        mUserEmail = (TextView) findViewById(R.id.userEmail);
        mUserImage = (ImageView) findViewById(R.id.userImage);
        mVarifyEmail = (Button) findViewById(R.id.varifiedEmail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            Log.i(TAG, "onCreate: " + photoUrl);

            mUserName.setText(name);
            mUserEmail.setText(email);
            mUserImage.setImageURI(photoUrl);

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();


            if (emailVerified) {
                Toast.makeText(this, "Email is not Varified", Toast.LENGTH_SHORT).show();
            }

            mVarifyEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                    }
                                }
                            });
                }
            });
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }


        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
            }
        });
    }
}
