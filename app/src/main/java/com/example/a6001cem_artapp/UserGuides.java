package com.example.a6001cem_artapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class UserGuides extends AppCompatActivity {

    private String userGuide;
    private TextView dailyGuideTV;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guides);
        dailyGuideTV = findViewById(R.id.dailyChallengeUserGuideTv);
        scrollView = findViewById(R.id.scrollViewRandom);

        Intent intent = getIntent();
        userGuide = ""+ intent.getStringExtra("UserGuide");


        if (userGuide.equals("show daily guide")){
            dailyGuideTV.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
        }else if (userGuide.equals("show random guide")){

            dailyGuideTV.setVisibility(View.INVISIBLE);

            scrollView.setVisibility(View.VISIBLE);
        }

    }
}