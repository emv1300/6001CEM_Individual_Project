package com.example.a6001cem_artapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a6001cem_artapp.adapters_and_classes.User;
import com.example.a6001cem_artapp.blog.DailyChallengeMain;
import com.example.a6001cem_artapp.randomChallgenges.ChallengeNavigation;
import com.example.a6001cem_artapp.user_account_settings.ProfileSettings;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button logOut, profileSettingsBT, challengeSectionBT, dailyBT;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String  userID;
    private CircleImageView profilePic;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);dailyBT = findViewById(R.id.dailyChallengeMainBT);
        challengeSectionBT = findViewById(R.id.ChallengeSelectionBT);
        logOut = findViewById(R.id.logOutBT);
        progressBar = findViewById(R.id.progressBarHomePage);
        profileSettingsBT = findViewById(R.id.profileSettingBT);
        mAuth = FirebaseAuth.getInstance();
        profilePic = findViewById(R.id.homePagePFP);
        dailyBT = findViewById(R.id.dailyChallengeMainBT);
        final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("Users/"+mAuth.getCurrentUser().getUid()+"profile.jpg");
        try {
            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Picasso.get().load(uri).placeholder(R.drawable.ic_launcher_foreground).into(profilePic);
                }
            });
        }catch (Exception e){

        }

        dailyBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, DailyChallengeMain.class);
                startActivity(intent);
                finish();
            }
        });

        challengeSectionBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ChallengeNavigation.class);
                startActivity(intent);
                finish();
            }
        });
        profileSettingsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ProfileSettings.class);
                startActivity(intent);
                finish();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signOut();
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                progressBar.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }


        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView username = (TextView) findViewById(R.id.TVUsername);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String userName = userProfile.userName;
                    username.setText("Welcome, "+userName+"!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePage.this, "something went wrong :(!", Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("If you press it again you will be logged out")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.signOut();
                        Intent intent = new Intent(HomePage.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        progressBar.setVisibility(View.GONE);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}