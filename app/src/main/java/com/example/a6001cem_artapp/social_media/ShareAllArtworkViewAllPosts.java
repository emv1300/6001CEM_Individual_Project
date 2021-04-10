package com.example.a6001cem_artapp.social_media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.a6001cem_artapp.R;

public class ShareAllArtworkViewAllPosts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_all_artwork_view_all_posts);

        SharedAllArtworkViewAllPostsFragment fragment = new SharedAllArtworkViewAllPostsFragment();;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();
    }


}