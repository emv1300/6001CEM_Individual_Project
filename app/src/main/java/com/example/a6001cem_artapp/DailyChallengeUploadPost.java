package com.example.a6001cem_artapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DailyChallengeUploadPost extends AppCompatActivity {

    private EditText title, description;
    private Button post, goBack, setImg;
    private ProgressBar progressBar;
    private ImageView postImage;
    private TextView neutralTV;


    private static final int GALLERY_INTENT_CODE = 6942;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private FirebaseUser user;

    private Bitmap bitmap;
    private Uri imageUri;
    private StorageReference storagePostPicsRef;

    private String userNameString;
    private String userPfp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge_upload_post);

        neutralTV = findViewById(R.id.neutralUploadPostTV);
        title = findViewById(R.id.titlePostEditText);
        description = findViewById(R.id.descriptionEditText);
        post = findViewById(R.id.postBT);
        goBack = findViewById(R.id.goBackUploadPostBT);
        setImg = findViewById(R.id.chooseImgBT);
        progressBar = findViewById(R.id.progressBarUploadPost);
        postImage = findViewById(R.id.makePostIV);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        storagePostPicsRef = FirebaseStorage.getInstance().getReference();

        try {
            dbRef.child("Image").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String pfp = (String) snapshot.getValue().toString();
                    userPfp = pfp;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            userPfp = "";
        }

        post.setEnabled(false);

        DatabaseReference username = dbRef.child("userName");
        username.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userNameString = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyChallengeUploadPost.this, DailyChallengeMain.class);
                startActivity(intent);
            }
        });

        setImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                try {
                    if (ActivityCompat.checkSelfPermission(DailyChallengeUploadPost.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DailyChallengeUploadPost.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_INTENT_CODE);
                    } else {
                        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(openGalleryIntent, GALLERY_INTENT_CODE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titleS = title.getText().toString().trim();
                String descriptionS = description.getText().toString();

                if (titleS.isEmpty()) {
                    title.setError("Pick a title!");
                    title.requestFocus();
                    return;
                }
                if (descriptionS.isEmpty() || descriptionS.length() < 10) {
                    description.setError("Please describe how the post relates to the challenge");
                    description.requestFocus();
                    return;
                }

                uploadPostFirebase(titleS, descriptionS);

            }
        });
    }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode == GALLERY_INTENT_CODE){
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(openGalleryIntent, GALLERY_INTENT_CODE);
                }
            }
            else{
                Toast.makeText(DailyChallengeUploadPost.this, "Grant permission in order to access gallery!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == GALLERY_INTENT_CODE){
                if(resultCode == Activity.RESULT_OK){
                    imageUri = data.getData();
                    try{
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    if(imageUri!=null){
                        postImage.setImageURI(imageUri);
                        if(post.getVisibility() == View.VISIBLE){
                            post.setEnabled(true);
                        }

                    }

                }
            }
            progressBar.setVisibility(View.INVISIBLE);
        }


    private void uploadPostFirebase( String title, String description){

        progressBar.setVisibility(View.VISIBLE);

        String timestamp = String.valueOf(System.currentTimeMillis());
        String filePath = "Posts/"+timestamp+"+"+user.getUid()+".jpg";
        final StorageReference fileRef = storagePostPicsRef.child(filePath);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(DailyChallengeUploadPost.this.getContentResolver(), bitmap,timestamp+"+"+user.getUid()+".jpg",null);
        Uri uriBitmap = Uri.parse(path);
        fileRef.putFile(uriBitmap).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        final String downloadUrl = task.getResult().toString();
                        if (task.isSuccessful()){
                            Map<String, Object> db_hashMap = new HashMap<>();
                            db_hashMap.put("postImage", downloadUrl);
                            db_hashMap.put("postDescription",description);
                            db_hashMap.put("postTitle",title);
                            db_hashMap.put("postID",timestamp+"+"+user.getUid());
                            db_hashMap.put("postTimestamp",timestamp);
                            db_hashMap.put("userID", user.getUid());
                            db_hashMap.put("userName",userNameString);
                            db_hashMap.put("postCommentsNum", "0");
                            if(userPfp != ""){
                                db_hashMap.put("userPfp",userPfp);
                            }

                            db_hashMap.put("postLikes", "0");
                            FirebaseDatabase.getInstance().getReference("Posts").child(timestamp+"+"+user.getUid())
                                    .setValue(db_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(DailyChallengeUploadPost.this,"Post uploaded successfully!",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(DailyChallengeUploadPost.this, DailyChallengeMain.class);
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(DailyChallengeUploadPost.this,"could not upload to database!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        return;
                                    }

                                }
                            });
                        }else{
                            Toast.makeText(DailyChallengeUploadPost.this,"could not upload image to cloud!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                    }

                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DailyChallengeUploadPost.this, "Error! Something went wrong!",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }

        });
        postImage.setEnabled(false);
    }


}