package com.example.a6001cem_artapp.randomChallgenges;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.a6001cem_artapp.R;

import java.util.Random;

public class ChallengeColours extends AppCompatActivity {

    private Button colour1,colour2,colour3,colour4,colour5, challengeBT, goBackBT;
    private int colourRgb1,colourRgb2,colourRgb3,colourRgb4,colourRgb5;
    private boolean b1,b2,b3,b4,b5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_colours);

        colour1 = findViewById(R.id.neutralColorPaletteBT1);
        colour2 = findViewById(R.id.neutralColorPaletteBT2);
        colour3 = findViewById(R.id.neutralColorPaletteBT3);
        colour4 = findViewById(R.id.neutralColorPaletteBT4);
        colour5 = findViewById(R.id.neutralColorPaletteBT5);

        b1 = b2 = b3 = b4 = b5 = false;

        goBackBT = findViewById(R.id.goBackChallengeColoursBT);
        challengeBT  = findViewById(R.id.challengeColourGenerateBT);

        goBackBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeColours.this, ChallengeNavigation.class);
                startActivity(intent);
            }
        });
        challengeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rgbRand = new Random();
                colourRgb1 = Color.argb(255, rgbRand.nextInt(256), rgbRand.nextInt(256), rgbRand.nextInt(256));
                colourRgb2 = Color.argb(255, rgbRand.nextInt(256), rgbRand.nextInt(256), rgbRand.nextInt(256));
                colourRgb3 = Color.argb(255, rgbRand.nextInt(256), rgbRand.nextInt(256), rgbRand.nextInt(256));
                colourRgb4 = Color.argb(255, rgbRand.nextInt(256), rgbRand.nextInt(256), rgbRand.nextInt(256));
                colourRgb5 = Color.argb(255, rgbRand.nextInt(256), rgbRand.nextInt(256), rgbRand.nextInt(256));


                colour1.setBackgroundColor(colourRgb1);
                colour1.setText(Integer.toHexString(colourRgb1));
                colour2.setBackgroundColor(colourRgb2);
                colour2.setText(Integer.toHexString(colourRgb2));
                colour3.setBackgroundColor(colourRgb3);
                colour3.setText(Integer.toHexString(colourRgb3));
                colour4.setBackgroundColor(colourRgb4);
                colour4.setText(Integer.toHexString(colourRgb4));
                colour5.setBackgroundColor(colourRgb5);
                colour5.setText(Integer.toHexString(colourRgb5));
            }
        });

        colour1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b1 == false){
                    colour1.setTextColor(Color.BLACK);
                    b1 = true;
                }else{
                    b1 = false;
                    colour1.setTextColor(Color.WHITE);
                }
            }
        });

        colour2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b2 == false){
                    colour2.setTextColor(Color.BLACK);
                    b2 = true;
                }else{
                    b2 = false;
                    colour2.setTextColor(Color.WHITE);
                }
            }
        });
        colour3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b3 == false){
                    colour3.setTextColor(Color.BLACK);
                    b3 = true;
                }else{
                    b3 = false;
                    colour3.setTextColor(Color.WHITE);
                }
            }
        });
        colour4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b4 == false){
                    colour4.setTextColor(Color.BLACK);
                    b4 = true;
                }else{
                    b4 = false;
                    colour4.setTextColor(Color.WHITE);
                }
            }
        });
        colour5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b5 == false){
                    colour5.setTextColor(Color.BLACK);
                    b5 = true;
                }else{
                    b5 = false;
                    colour5.setTextColor(Color.WHITE);
                }
            }
        });
    }
}