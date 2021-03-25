package com.example.a6001cem_artapp.user_account_settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a6001cem_artapp.HomePage;
import com.example.a6001cem_artapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileSettings extends AppCompatActivity {
    private Button passReset, usernameReset, emailReset, goBack, profilePicChange, deleteAcc;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        passReset = findViewById(R.id.pwChangeBT);
        usernameReset = findViewById(R.id.userChangeBT);
        emailReset = findViewById(R.id.emailChangeBT);
        goBack = findViewById(R.id.goBackSettingsBT);
        progressBar = findViewById(R.id.progressBarProfileSettings);
        deleteAcc = findViewById(R.id.accountDeletionBT);
        profilePicChange = findViewById(R.id.profileImgBT);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettings.this, HomePage.class);
                startActivity(intent);
            }
        });

        profilePicChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettings.this, UploadProfilePicture.class);
                startActivity(intent);
            }
        });

        passReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettings.this, Password_Update.class);
                startActivity(intent);
            }
        });

        emailReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettings.this, Email_Update.class);
                startActivity(intent);
            }
        });

        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettings.this, Account_Deletion.class);
                startActivity(intent);
            }
        });
        usernameReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettings.this);
                builder.setTitle("Pick a new username");
                progressBar.setVisibility(View.VISIBLE);
                LinearLayout linearLayout = new LinearLayout(ProfileSettings.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);
                EditText updateUsername = new EditText(ProfileSettings.this);
                updateUsername.setHint("Input the new username");
                linearLayout.addView(updateUsername);

                builder.setView(linearLayout);
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newUsername = updateUsername.getText().toString().trim();
                        if (TextUtils.isEmpty(newUsername) || newUsername.length()<6){
                            Toast.makeText(ProfileSettings.this, "Please enter a valid username", Toast.LENGTH_LONG).show();


                        }else{
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userName");
                            reference.setValue(newUsername);
                            Toast.makeText(ProfileSettings.this, "Succesfully updated username!", Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }

                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.GONE);
                        return;

                    }
                });
                progressBar.setVisibility(View.GONE);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
}