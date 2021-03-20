package com.example.a6001cem_artapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChallengeNavigation extends AppCompatActivity {
    private Button landscapeChallenge, goBack, charDChallenge, geomChallenge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goBack = findViewById(R.id.goBackChallengeSelectionBT);
        landscapeChallenge = findViewById(R.id.challenge1BT);
        charDChallenge = findViewById(R.id.challenge2BT);
        geomChallenge = findViewById(R.id.challenge3BT);

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