package com.example.a6001cem_artapp.user_account_settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a6001cem_artapp.MainActivity;
import com.example.a6001cem_artapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Password_Update extends AppCompatActivity {

    private EditText oldPass, newPass, confirmPass;
    private Button goBackBT, resetPw;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password__update);

        oldPass = findViewById(R.id.oldPassTV);
        newPass = findViewById(R.id.passResetTV);
        confirmPass = findViewById(R.id.pwResetTVConfirm);

        goBackBT = findViewById(R.id.goBackPassChangeBT);
        resetPw = findViewById(R.id.resetPwBT);

        progressBar = findViewById(R.id.progressBarPassChange);

        mAuth = FirebaseAuth.getInstance();

        goBackBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Password_Update.this, ProfileSettings.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        resetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldPw = oldPass.getText().toString().trim();
                String newPw = newPass.getText().toString().trim();
                String confirmPw = confirmPass.getText().toString().trim();

                if(oldPw.isEmpty()) {
                    oldPass.setError("Old Password Required");
                    oldPass.requestFocus();
                    return;
                }
                if(newPw.isEmpty()) {
                    oldPass.setError("New Password Required");
                    oldPass.requestFocus();
                    return;
                }
                if(newPw.length()<8){
                    newPass.setError("Invalid New Password");
                    newPass.requestFocus();
                    return;
                }
                if(!newPw.matches("(.*[A-Z].*)")){
                    newPass.setError("Need at least one Uppercase character");
                    newPass.requestFocus();
                    return;
                }
                if(!newPw.matches("(.*[a-z].*)")) {
                    newPass.setError("Need at least one Lowercase character");
                    newPass.requestFocus();
                    return;
                }
                if(!newPw.matches("(.*[0-9].*)")) {
                    newPass.setError("Need at least one numeric character");
                    newPass.requestFocus();
                    return;
                }
                if(!newPw.matches("^(?=.*[!@#$%^&*()+=`?]).*$")) {
                    newPass.setError("Need at least one special character");
                    newPass.requestFocus();
                    return;
                }
                if (!newPw.equals(confirmPw)) {
                    confirmPass.setError("Password doesn't match!");
                    confirmPass.requestFocus();
                    return;
                }
                /*if (new Registration().checkString(newPw)) {
                    newPass.setError("You cannot repeat the same character, in a row, more than 3 times");
                    newPass.requestFocus();
                    return;
                }*/
                updatePassword(oldPw, newPw);
            }
        });
    }
    private void updatePassword(String oldPassword, String newPassword){
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredentials = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(authCredentials)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Password_Update.this, "Password Changed Successfully! Returning to login screen", Toast.LENGTH_LONG).show();
                                        mAuth.signOut();
                                        Intent intent = new Intent(Password_Update.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Password_Update.this, "Something went wrong! please try again...", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Password_Update.this, "Update Failed!", Toast.LENGTH_LONG).show();
                        return;
                    }
                });
        progressBar.setVisibility(View.GONE);
    }
}