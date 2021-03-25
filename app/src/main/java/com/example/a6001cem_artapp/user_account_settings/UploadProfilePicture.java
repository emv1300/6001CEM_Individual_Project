package com.example.a6001cem_artapp.user_account_settings;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a6001cem_artapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadProfilePicture extends AppCompatActivity {

    private Button goBack, setImg, uploadImg;
    private CircleImageView profilePic;
    private ProgressBar progressBar;

    private static final int GALLERY_INTENT_CODE = 4269;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private FirebaseUser user;

    private Bitmap bitmap;
    private Uri imageUri;
    private StorageReference storageProfilePicsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);

        goBack = findViewById(R.id.goBackUploadPicBT);
        setImg = findViewById(R.id.selectImgBT);
        uploadImg = findViewById(R.id.uploadPicBT);

        profilePic = findViewById(R.id.ProfilePicSelectIV);
        progressBar = findViewById(R.id.progressBarUploadProfilePic);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference();

        uploadImg.setEnabled(false);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadProfilePicture.this, ProfileSettings.class);
                startActivity(intent);
            }
        });

        StorageReference profileRef = storageProfilePicsRef.child("Users/"+mAuth.getCurrentUser().getUid()+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });

        setImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                try{
                    if (ActivityCompat.checkSelfPermission(UploadProfilePicture.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(UploadProfilePicture.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_INTENT_CODE);
                    }else{
                        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(openGalleryIntent, GALLERY_INTENT_CODE);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                uploadImgFirebase(imageUri);
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
            Toast.makeText(UploadProfilePicture.this, "Grant permission in order to access gallery!", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
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
                profilePic.setImageURI(imageUri);
                uploadImg.setEnabled(true);
            }
        }
        progressBar.setVisibility(View.GONE);
    }
    private void uploadImgFirebase(Uri uri){

        final StorageReference fileRef = storageProfilePicsRef.child("Users/"+mAuth.getCurrentUser().getUid()+"profile.jpg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(UploadProfilePicture.this.getContentResolver(), bitmap,mAuth.getCurrentUser().getUid()+"profile.jpg" ,null);
        Uri uriBitmap = Uri.parse(path);
        fileRef.putFile(uriBitmap).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        final String downloadUrl = task.getResult().toString();
                        if (task.isSuccessful()){
                            Map<String, Object> updated_hashMap = new HashMap<>();
                            updated_hashMap.put("Image", downloadUrl);
                            FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid())
                                    .updateChildren(updated_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(UploadProfilePicture.this,"Image uploaded Successfully!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadProfilePicture.this, "Error! Something went wrong or perhaps image size might be too big!",Toast.LENGTH_LONG).show();
                uploadImg.setEnabled(false);
            }

        });
        uploadImg.setEnabled(false);
        progressBar.setVisibility(View.GONE);
    }

}
