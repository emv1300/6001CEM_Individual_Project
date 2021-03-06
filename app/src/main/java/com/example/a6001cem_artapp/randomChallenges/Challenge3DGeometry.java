package com.example.a6001cem_artapp.randomChallenges;

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
import java.util.Objects;
import java.util.Random;

public class Challenge3DGeometry extends AppCompatActivity {

    private Button goBack, challenge;
    private TextView shapeTV, originXYZTV, lightXYZTV;
    private final List<String> listShapes = new ArrayList<String>(), coordList = new ArrayList<>();
    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge3_d_geometry);
        coordList.addAll(Arrays.asList("0","0.1","0.2","0.25","0.3","0.4","0.5","0.6","0.7","0.75","0.8","0.9","1","-0.1","-0.2","-0.25","-0.3","-0.4","-0.5","-0.6","-0.7","-0.75","-0.8","-0.9","-1"));
        listShapes.addAll(Arrays.asList("Cube","Cone","Sphere","Cylinder","Square Pyramid","Triangular Pyramid",
                "Pentagonal Pyramid","Hexagonal Pyramid","Triangular Prism","Pentagonal Prism","Cuboid",
                "Hexagonal Prism","Ellipsoid","Dodecahedron","Octagonal Pyramid","Octagonal Prism","Torus",
                "Parallelepiped","Tetrahedron","Octahedron","Helix"));

        shapeTV = findViewById(R.id.ShapeListTV);
        originXYZTV = findViewById(R.id.OriginTV);
        lightXYZTV = findViewById(R.id.LightSourceTV);

        goBack = findViewById(R.id.goBackGeomChallengeBT);
        challenge = findViewById(R.id.challengeGeometryBT);

        Bundle params = new Bundle();
        analytics = FirebaseAnalytics.getInstance(this);
        params.putString("user_id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        params.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "geometryChallenge");
        analytics.logEvent("geometryChallenge_users", params);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Challenge3DGeometry.this, ChallengeNavigation.class);
                startActivity(intent);

            }
        });

        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> listOriginXYZ = new ArrayList<String>();
                List<String> listLightXYZ = new ArrayList<String>();
                List<Integer> verticesOriginList = new ArrayList<Integer>();
                for (int i = 0; i < 3; i++){
                    verticesOriginList.add(new Random().nextInt(coordList.size()));
                    for (int j = 0; j < coordList.size(); j++){
                        if(verticesOriginList.get(i) == j ){
                            listOriginXYZ.add(coordList.get(j));
                        }
                    }
                }
                List<Integer> verticesLightList = new ArrayList<Integer>();
                for (int i = 0; i < 3; i++){
                    verticesLightList.add(new Random().nextInt(coordList.size()));
                    for (int j = 0; j < coordList.size(); j++){
                        if(verticesLightList.get(i) == j ){
                            listLightXYZ.add(coordList.get(j));
                        }
                    }
                }

                originXYZTV.setText(listOriginXYZ.toString());
                lightXYZTV.setText(listLightXYZ.toString());
                List<Integer> numList = new ArrayList<Integer>();
                List<String> stringLists = new ArrayList<>();
                for (int i = 0; i < 5; i++)
                {
                    numList.add(new Random().nextInt(listShapes.size()));
                    for (int j = 0; j < listShapes.size(); j++){
                        if (numList.get(i) == j){
                            stringLists.add(listShapes.get(j));
                        }
                    }
                    shapeTV.setText(stringLists.toString());
                }

            }
        });
    }
}