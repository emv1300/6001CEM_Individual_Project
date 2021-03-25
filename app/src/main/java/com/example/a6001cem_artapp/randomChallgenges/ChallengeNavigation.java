package com.example.a6001cem_artapp.randomChallgenges;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.a6001cem_artapp.HomePage;
import com.example.a6001cem_artapp.R;
import com.example.a6001cem_artapp.UserGuides;

public class ChallengeNavigation extends AppCompatActivity {
    private Button landscapeChallenge, goBack, charDChallenge, geomChallenge, userGuideBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_navigation);
        goBack = findViewById(R.id.goBackChallengeSelectionBT);
        landscapeChallenge = findViewById(R.id.challenge1BT);
        charDChallenge = findViewById(R.id.challenge2BT);
        geomChallenge = findViewById(R.id.challenge3BT);

        userGuideBT = findViewById(R.id.RandomChallengeUserGuideBT);
        userGuideBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ChallengeNavigation.this, UserGuides.class);
                intent.putExtra("UserGuide", "show random guide");
                startActivity(intent);
            }
        });

        landscapeChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ChallengeNavigation.this, ChallengeLandscape.class);
                startActivity(intent);
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ChallengeNavigation.this, HomePage.class);
                startActivity(intent);
            }
        });

        charDChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeNavigation.this, ChallengeCharacterDesign.class);
                startActivity(intent);
            }
        });
        geomChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeNavigation.this, Challenge3DGeometry.class);
                startActivity(intent);
            }
        });
    }
}