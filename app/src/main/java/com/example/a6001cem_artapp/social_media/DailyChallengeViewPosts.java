package com.example.a6001cem_artapp.social_media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.a6001cem_artapp.R;

public class DailyChallengeViewPosts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge_view_posts);

        Intent intent = getIntent();
        String whatFragment = intent.getStringExtra("viewPosts");

        if(whatFragment.equals("viewChallengePosts")){
            DailyChallengeViewPostsFragment fragment = new DailyChallengeViewPostsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment, "");
            fragmentTransaction.commit();
        }
        else if(whatFragment.equals("viewAllPosts")){
            SharedAllArtworkViewAllPostsFragment fragment = new SharedAllArtworkViewAllPostsFragment();;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment, "");
            fragmentTransaction.commit();
        }
    }
}