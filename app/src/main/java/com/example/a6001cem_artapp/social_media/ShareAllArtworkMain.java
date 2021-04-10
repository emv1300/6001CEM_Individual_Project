package com.example.a6001cem_artapp.social_media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a6001cem_artapp.HomePage;
import com.example.a6001cem_artapp.R;
import com.example.a6001cem_artapp.UserGuides;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShareAllArtworkMain extends AppCompatActivity {

    private Button goBack, makePostBT, viewPostBT, userGuideBT;
    private TextView challengeWordTV;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_all_artwork_main);

        goBack = findViewById(R.id.goBackArtMainBT);
        makePostBT = findViewById(R.id.makeArtPostBT);
        viewPostBT = findViewById(R.id.viewAllPostsBT);
        userGuideBT = findViewById(R.id.dailyChallengeUserGuideBT);
        userGuideBT.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent= new Intent(ShareAllArtworkMain.this, UserGuides.class);
            intent.putExtra("UserGuide", "show all posts guide");
            startActivity(intent);
        }
        });


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareAllArtworkMain.this, HomePage.class);
                startActivity(intent);
            }
        });
        makePostBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareAllArtworkMain.this, DailyChallengeUploadPost.class);
                intent.putExtra("is_challenge","not_a_challenge");
                startActivity(intent);
            }
        });
        viewPostBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareAllArtworkMain.this, DailyChallengeViewPosts.class);
                intent.putExtra("viewPosts", "viewAllPosts");
                startActivity(intent);
            }
        });
    }

}