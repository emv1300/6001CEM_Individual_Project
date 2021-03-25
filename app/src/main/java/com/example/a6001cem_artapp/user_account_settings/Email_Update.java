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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Email_Update extends AppCompatActivity {

    private EditText newEmail, password, oldEmail;
    private Button updateEmailBT,returnBT;
    private CheckBox captchaCheck;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email__update);

        oldEmail = findViewById(R.id.oldEmailUpdate);
        newEmail = findViewById(R.id.emailUpdate);
        password = findViewById(R.id.passwordEmailUpdate);
        captchaCheck = findViewById(R.id.CaptchaEmailUpdate);
        progressBar = findViewById(R.id.progressBarEmailUpdate);
        returnBT = findViewById(R.id.goBackEmailUpdateBT);
        updateEmailBT = findViewById(R.id.emailUpdateBT);

        mAuth = FirebaseAuth.getInstance();
        returnBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Email_Update.this, ProfileSettings.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        updateEmailBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });
    }
    public void checkEmailUpdate(View view){
        SafetyNet.getClient(this).verifyWithRecaptcha("6LeVoIAaAAAAAAbaJpHDfn6XYcw1wzs9VT95WcYJ")
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        Toast.makeText(Email_Update.this, "Success!", Toast.LENGTH_LONG).show();
                        checked = true;
                        captchaCheck.setChecked(true);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Email_Update.this, "Failed! Try again please", Toast.LENGTH_LONG).show();
                        checked = false;
                    }
                });
        if (captchaCheck.isChecked() == true && checked == false){
            captchaCheck.setChecked(false);
        }
    }
    private void updateEmail() {
        String old = oldEmail.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String email = newEmail.getText().toString().trim();

        if (old.isEmpty()) {
            oldEmail.setError("E-mail Required");
            oldEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(old).matches()) {
            oldEmail.setError("Invalid E-mail");
            oldEmail.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            newEmail.setError("E-mail Required");
            newEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            newEmail.setError("Invalid E-mail");
            newEmail.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            password.setError("Password required");
            password.requestFocus();
            return;
        }
        if (!captchaCheck.isChecked()){
            captchaCheck.setError("must be ticked");
            captchaCheck.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference comparisonReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("email");
        comparisonReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String emailFromDb = snapshot.getValue().toString().trim();
                if (!emailFromDb.equals(old)){
                    oldEmail.setError("Input your old email!");
                    oldEmail.requestFocus();
                    return;
                }
                else{
                    AuthCredential authCredentials = EmailAuthProvider.getCredential(user.getEmail(), pass);
                    user.reauthenticate(authCredentials).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("email");
                                reference.setValue(email)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                user.updateEmail(email)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                progressBar.setVisibility(View.GONE);
                                                                FirebaseUser userEmailVerification = mAuth.getCurrentUser();
                                                                userEmailVerification.sendEmailVerification();
                                                                Toast.makeText(Email_Update.this, "Email Changed Successfully! Returning to login screen", Toast.LENGTH_LONG).show();

                                                                Intent intent = new Intent(Email_Update.this, MainActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                mAuth.signOut();
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(Email_Update.this, "email update failed!", Toast.LENGTH_LONG).show();
                                                                progressBar.setVisibility(View.GONE);
                                                                return;
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Email_Update.this, "database update failed!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                return;
                                            }
                                        });

                            }
                            else{
                                Toast.makeText(Email_Update.this, "email update failed! credentials entered are wrong", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                return;
                            }

                        }

                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Email_Update.this, "Data could not be retrieved for verification!", Toast.LENGTH_LONG).show();
            }
        });



    }
}