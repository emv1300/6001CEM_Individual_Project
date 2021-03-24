package com.example.a6001cem_artapp;

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

import com.example.a6001cem_artapp.adapters_and_classes.User;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText eMail, userName, pass, confirmPass;
    private Button registerBT, regGoBack;
    private ProgressBar progressBar;
    private CheckBox checkBox;
    private boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

        regGoBack = findViewById(R.id.regBackBT);
        regGoBack.setOnClickListener(this);

        registerBT =  findViewById(R.id.regBT);
        registerBT.setOnClickListener(this);

        checkBox = findViewById(R.id.checkBox);


        userName = findViewById(R.id.regUsername);
        eMail = findViewById(R.id.regEmail);
        pass = findViewById(R.id.PassReg);
        confirmPass = findViewById(R.id.regPwConfirm);
        progressBar = findViewById(R.id.progressBar);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.regBackBT:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.regBT:
                registerNewUser();
                break;
        }
    }

    public void check(View view){


        SafetyNet.getClient(this).verifyWithRecaptcha("6LeVoIAaAAAAAAbaJpHDfn6XYcw1wzs9VT95WcYJ")
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        Toast.makeText(Registration.this, "Success!", Toast.LENGTH_LONG).show();
                        checked = true;
                        checkBox.setChecked(true);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registration.this, "Failed! Try again please", Toast.LENGTH_LONG).show();
                        checked = false;
                    }
                });
        if (checkBox.isChecked() == true && checked == false){
            checkBox.setChecked(false);
        }
    }

    private boolean checkString(String toBeChecked){
        int n = toBeChecked.length(), numAllowed = 3;
        for (int i = 0; i < n-1; i++)
        {
            if (toBeChecked.charAt(i) == toBeChecked.charAt(i+1))
            { numAllowed = numAllowed - 1;}
            else
                numAllowed = 3;
            if (numAllowed < 1){
                return true;
            }
        }
        return false;
    }
    private void registerNewUser() {
        String user_eMail = eMail.getText().toString().trim();
        String username = userName.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String passwordCF = confirmPass.getText().toString().trim();
        ///Username validation
        if (username.isEmpty()) {
            userName.setError("Username Required");
            userName.requestFocus();
            return;
        }
        if (username.length() < 6) {
            userName.setError("Invalid Username");
            userName.requestFocus();
            return;
        }
        if (checkString(username)) {
            userName.setError("You cannot repeat the same character, in a row, more than 3 times");
            userName.requestFocus();
            return;
        }
        ///Password validation
        if(password.isEmpty()) {
            pass.setError("Password Required");
            pass.requestFocus();
            return;
        }
        if(password.length()<8){
            pass.setError("Invalid Password");
            pass.requestFocus();
            return;
        }
        if(!password.matches("(.*[A-Z].*)")){
            pass.setError("Need at least one Uppercase character");
            pass.requestFocus();
            return;
        }
        if(!password.matches("(.*[a-z].*)")) {
            pass.setError("Need at least one Lowercase character");
            pass.requestFocus();
            return;
        }
        if(!password.matches("(.*[0-9].*)")) {
            pass.setError("Need at least one numeric character");
            pass.requestFocus();
            return;
        }
        if(!password.matches("^(?=.*[!@#$%^&*()+=`?]).*$")) {
            pass.setError("Need at least one special character");
            pass.requestFocus();
            return;
        }
        if (!password.equals(passwordCF)) {
            confirmPass.setError("Password doesn't match!");
            confirmPass.requestFocus();
            return;
        }
        if (checkString(password)) {
            pass.setError("You cannot repeat the same character, in a row, more than 3 times");
            pass.requestFocus();
            return;
        }
        ///Email validation
        if (user_eMail.isEmpty()) {
            eMail.setError("E-mail Required");
            eMail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(user_eMail).matches()) {
            eMail.setError("Invalid E-mail");
            eMail.requestFocus();
            return;
        }
        //Verify human
        if (!checkBox.isChecked()){
            checkBox.setError("must be ticked");
            checkBox.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);//
        mAuth.createUserWithEmailAndPassword(user_eMail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(user_eMail, username, "");
                            String uID = FirebaseAuth.getInstance().getUid();
                            Task<Void> voidTask = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(uID)
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Registration.this, "Successful registration", Toast.LENGTH_LONG).show();
                                                FirebaseUser userEmailVerification = FirebaseAuth.getInstance().getCurrentUser();
                                                userEmailVerification.sendEmailVerification();
                                                progressBar.setVisibility(View.GONE);//
                                                startActivity(new Intent(Registration.this, MainActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(Registration.this, "Registration failed", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);//
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Registration.this, "Registration failed", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);//
                        }


                    }
                });

    }
}