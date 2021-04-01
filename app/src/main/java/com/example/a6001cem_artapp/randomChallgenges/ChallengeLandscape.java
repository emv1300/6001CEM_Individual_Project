package com.example.a6001cem_artapp.randomChallgenges;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a6001cem_artapp.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChallengeLandscape extends AppCompatActivity {

    private Button goBack, challenge;
    private TextView weatherTV, terrainTV, vegetationTV, timeTV, structuresTV;
    private List<String> listWeather = new ArrayList<String>(), listTerrains  = new ArrayList<String>(), listVegetation = new ArrayList<String>(), listTimeOfDay  = new ArrayList<String>(), listStructures = new ArrayList<>();
    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_landscape);
        listWeather.addAll(Arrays.asList("sunny/no clouds","snowy","rainy","windy","cloudy","stormy","foggy","freezing","blizzard","hurricane","dust storm","flood"));
        listTerrains.addAll(Arrays.asList("crater","underground cave","volcano","canyon","desert","mountains","hills","marsh","oasis","ocean","islands","coastal beach","swamp","tundra","valley","delta","glaciers","savanna"));
        listVegetation.addAll(Arrays.asList("forest","pine forest","forest","pine forest","arid","desolate","mountain forrest","arctic forest","tropical forest", "mushroom forest", "laurel forests", "rain forest", "jungle", "open","grass land","mountain forest","barren","steppe","no vegetation","cactus forest","a shrubbery, Ni!"));
        listTimeOfDay.addAll(Arrays.asList("morning","dawn","sunrise","midday","afternoon","sunset","dusk","night","full moon","half moon","eclipse"));
        listStructures.addAll(Arrays.asList("--","--","--","--","--","--","castle","manor","villa","village","cottage","cabin","camp","arena","temple","shrine","altar"));
        goBack = findViewById(R.id.goBackChallengeLandscapeBT);
        challenge = findViewById(R.id.LChallengeBT);
        structuresTV = findViewById(R.id.structuresTV);
        weatherTV = findViewById(R.id.tvWord1Landscape);
        terrainTV = findViewById(R.id.tvWord4Landscape);
        vegetationTV = findViewById(R.id.tvWord3Landscape);
        timeTV = findViewById(R.id.tvWord2Landscape);


        Bundle params = new Bundle();
        analytics = FirebaseAnalytics.getInstance(this);
        params.putString("user_id", FirebaseAuth.getInstance().getUid());

        params.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "landscapeChallenge");
        analytics.logEvent("landscapeChallenge_users", params);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ChallengeLandscape.this, ChallengeNavigation.class);
                startActivity(intent);
                finish();
            }
        });

        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Integer> numList = new ArrayList<Integer>();
                numList.add(new Random().nextInt(listWeather.size()));
                numList.add(new Random().nextInt(listTerrains.size()));
                numList.add(new Random().nextInt(listVegetation.size()));
                numList.add(new Random().nextInt(listTimeOfDay.size()));
                numList.add(new Random().nextInt(listStructures.size()));

                for(int i = 0; i < 5; i++){
                    if(i == 0 ){
                        for(int j = 0; j < listWeather.size(); j++){
                            if (numList.get(i) == j){
                                weatherTV.setText(listWeather.get(j));
                            }
                        }
                    }else if(i==1){
                        for(int j = 0; j < listTerrains.size(); j++){
                            if (numList.get(i) == j){
                                terrainTV.setText(listTerrains.get(j));
                            }
                        }
                    }else if (i==2){
                        for(int j = 0; j < listVegetation.size(); j++){
                            if (numList.get(i) == j){
                                vegetationTV.setText(listVegetation.get(j));
                            }
                        }
                    }else if (i==3){
                        for(int j = 0; j < listTimeOfDay.size(); j++){
                            if (numList.get(i) == j){
                                timeTV.setText(listTimeOfDay.get(j));
                            }
                        }
                    }else {
                        for(int j = 0; j < listStructures.size(); j++){
                            if (numList.get(i) == j){
                                structuresTV.setText(listStructures.get(j));
                            }
                        }
                    }
                }
            }
        });
    }
}