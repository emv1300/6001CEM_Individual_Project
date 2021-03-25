package com.example.a6001cem_artapp.user_account_settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a6001cem_artapp.MainActivity;
import com.example.a6001cem_artapp.R;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account_Deletion extends AppCompatActivity {

    private Button goBackBt, deleteAccBt;
    private EditText email,password;
    private ProgressBar progressBar;
    private CheckBox captchaBox;
    private FirebaseAuth mAuth;
    private boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__deletion);

        goBackBt = findViewById(R.id.goBackAccDelBT);
        deleteAccBt = findViewById(R.id.deleteAccountBT);
        email = findViewById(R.id.emailAccDel);
        password = findViewById(R.id.passAccDel);
        progressBar = findViewById(R.id.progressBarAccDel);
        captchaBox = findViewById(R.id.checkBoxAccDel);
        mAuth = FirebaseAuth.getInstance();

        goBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account_Deletion.this, ProfileSettings.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        deleteAccBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
    }
    public void checkForDeletion(View view) {
        SafetyNet.getClient(this).verifyWithRecaptcha("6LfvMEMaAAAAAJMKQ70Ye2FbHQw17tYUP5hfAoKm")
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        Toast.makeText(Account_Deletion.this, "Success!", Toast.LENGTH_LONG).show();
                        checked = true;
                        captchaBox.setChecked(true);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Account_Deletion.this, "Failed! Try again please", Toast.LENGTH_LONG).show();
                        checked = false;
                    }
                });
        if (captchaBox.isChecked() == true && checked == false){
            captchaBox.setChecked(false);
        }
    }
    private void deleteAccount(){
        String emailString = email.getText().toString().trim();
        String passString = password.getText().toString().trim();

        if (emailString.isEmpty()) {
            email.setError("E-mail Required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Invalid E-mail");
            email.requestFocus();
            return;
        }
        if (passString.isEmpty()) {
            password.setError("Password required");
            password.requestFocus();
            return;
        }
        if(!captchaBox.isChecked()){
            captchaBox.setError("Are you human?");
            captchaBox.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("email");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String emailFromDb = snapshot.getValue().toString().trim();
                if (!emailFromDb.equals(emailString)){
                    email.setError("Input your E-mail");
                    email.requestFocus();
                    return;
                }
                else{
                    AuthCredential authCredentials = EmailAuthProvider.getCredential(user.getEmail(), passString);
                    user.reauthenticate(authCredentials)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Toast.makeText(accountDeletion.this, "Authentication worked", Toast.LENGTH_LONG).show();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
                                    reference.removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    user.delete().
                                                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    mAuth.signOut();
                                                                    Toast.makeText(Account_Deletion.this, "Account deleted successfully!", Toast.LENGTH_LONG).show();
                                                                    Intent intent = new Intent(Account_Deletion.this, MainActivity.class);
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
                                                                    Toast.makeText(Account_Deletion.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                                                                    return;
                                                                }
                                                            });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Account_Deletion.this, "Data could not be deleted from the online DB!", Toast.LENGTH_LONG).show();
                                                    return;
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Account_Deletion.this, "Invalid Password!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            });
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Account_Deletion.this, "Data could not be retrieved for verification!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
        });
    }

}