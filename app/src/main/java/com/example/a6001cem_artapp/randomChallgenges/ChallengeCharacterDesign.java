package com.example.a6001cem_artapp.randomChallgenges;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a6001cem_artapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChallengeCharacterDesign extends AppCompatActivity {

    private Button goBack, challenge;
    private TextView speciesTV, occupationTV, poseTV, ageTV;
    private List<String> listSpecies = new ArrayList<String>(), listOccupation = new ArrayList<String>(), listPoses = new ArrayList<String>(), listAgeAvg = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_character_design);
        listSpecies.addAll(Arrays.asList("human","dwarf","elf","fairy","goblin","halfling","lizard-people","centaur","minotaur","werewolf","vampire","undead","cyborg","robot","android","orc","ogre","troll","satyr","naga","giant","godling","demigod","dragonling","harpie","golem","titan","draconian","gnoll","leprechaun","gorgon","fury","fae","djhin","mermen/maids","beast-man","fishmen","naiad","nymph","dryad","treant","werecat","wererat","wereboar","Man-Bear-Pig","oni","elemental"));
        listOccupation.addAll(Arrays.asList("peasant","monk","warrior","artist","builder","student","homeless","noble","spy","cook","shopkeeper","craftsman","artisan","doctor","teacher","philosopher","wild","criminal","scientist","king","sailor","knight","guard","police","singer","painter","surgeon","witch-doctor","priest","athlete","comedian","actor","alchemist","witch/wizard","knight","samurai","ninja","viking","mercenary","pirate","bandit","thief","crook","healer","explorer","scout","warrior"));
        listPoses.addAll(Arrays.asList("neutral","sitting","sleeping","resting","eating","training","working","building","fighting","swinging","drinking","smoking","eating","reading","walking","running","flying","mediating","crouching","dancing","falling","martial arts practice","writing","drawing","painting","singing","crafting","playing","praying","kneeling","punching","leg kicking","hovering","t-pose","roaring","climbing"));
        listAgeAvg.addAll(Arrays.asList("adult","middle-aged","senior","old","ancient","young"));


        goBack = findViewById(R.id.goBackChallengeCharDBT);
        challenge = findViewById(R.id.charDChallengeBT);

        speciesTV = findViewById(R.id.tvWord1CharD);
        occupationTV = findViewById(R.id.tvWord4CharD);
        poseTV = findViewById(R.id.tvWord3CharD);
        ageTV = findViewById(R.id.tvWord2CharD);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ChallengeCharacterDesign.this, ChallengeNavigation.class);
                startActivity(intent);
                finish();
            }
        });
        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> numList = new ArrayList<Integer>();
                numList.add(new Random().nextInt(listSpecies.size()));
                numList.add(new Random().nextInt(listOccupation.size()));
                numList.add(new Random().nextInt(listPoses.size()));
                numList.add(new Random().nextInt(listAgeAvg.size()));
                for (int i = 0; i < 4; i++) {
                    if (i == 0) {
                        for (int j = 0; j < listSpecies.size(); j++) {
                            if (numList.get(i) == j) {
                                speciesTV.setText(listSpecies.get(j));
                            }
                        }
                    } else if (i == 1) {
                        for (int j = 0; j < listOccupation.size(); j++) {
                            if (numList.get(i) == j) {
                                occupationTV.setText(listOccupation.get(j));
                            }
                        }
                    } else if (i == 2) {
                        for (int j = 0; j < listPoses.size(); j++) {
                            if (numList.get(i) == j) {
                                poseTV.setText(listPoses.get(j));
                            }
                        }
                    } else {
                        for (int j = 0; j < listAgeAvg.size(); j++) {
                            if (numList.get(i) == j) {
                                ageTV.setText(listAgeAvg.get(j));
                            }
                        }
                    }
                }
            }
        });
    }
}