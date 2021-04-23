package com.example.a6001cem_artapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button loginBT;
    private TextView registerTV, credRecTV, numOfTries;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refresh;
    private int numTries;
    private long timeOfLastFailedLogin;
    private long lastClickTime = 0;
    public static final String sharedPrefs = "PrefsFile";
    public static final String stored_numTries = "numTries";
    public static final String stored_timeOfLastFailedLogin = "timeOfLastFailedLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoadData();

        registerTV = findViewById(R.id.regTVlogin);
        registerTV.setOnClickListener(this);

        loginBT = findViewById(R.id.loginBT);
        loginBT.setOnClickListener(this);

        credRecTV = findViewById(R.id.passRecTV);
        credRecTV.setOnClickListener(this);

        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.loginPassword);

        progressBar = findViewById(R.id.progressBarLogin);
        mAuth = FirebaseAuth.getInstance();
        LoadData();
        numOfTries =findViewById(R.id.numOfTries);
        numOfTries.setText("Number of login attempts: " + numTries);

        progressBar.setVisibility(View.INVISIBLE);

        refresh = findViewById(R.id.swiperefresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                long timeCurrent = new Date().getTime();
                LoadData();
                if (timeCurrent >= 40000+timeOfLastFailedLogin){
                    numTries = 3;
                    loginBT.setEnabled(true);
                    numOfTries.setText("Number of login attempts: " + numTries);
                    SaveData();
                }
                final Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(refresh.isRefreshing()) {
                            refresh.setRefreshing(false);
                        }
                    }
                }, 1000);

            }
        });


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regTVlogin:
                startActivity(new Intent(this, Registration.class));
                break;
            case R.id.loginBT:
                if (SystemClock.elapsedRealtime() - lastClickTime < 1800){
                    return;
                }else{
                    lastClickTime = SystemClock.elapsedRealtime();//records the last time the login button was clicked
                    loginUser();//calls loginUser method
                    break;
                }
        }
    }

    private void SaveData() {
        SharedPreferences sharedPreferences= getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(stored_numTries, numTries);
        editor.putLong(stored_timeOfLastFailedLogin,timeOfLastFailedLogin);
        editor.apply();
    }
    private void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        numTries = sharedPreferences.getInt(stored_numTries, 3);
        timeOfLastFailedLogin = sharedPreferences.getLong(stored_timeOfLastFailedLogin, new Date().getTime());
    }

    /*
    method for checking email format
     */
    public static boolean emailValid(String emailString){
        if (emailString == null || emailString.length()<=0){//checks if the email string is null or has length lower or equal to 0
            return false;//if it does than return false
        }
        //email pattern object that matches standard email account format
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" + "[a-zA-Z0-9\\-]{0,64}" +
                "(" + "\\."+
                "[a-zA-Z0-9\\-]{0,25}"+
                ")+");
        return  emailPattern.matcher(emailString).matches();//returns true if email matches format and false if not
    }

    private void loginUser(){
        Date dt = new Date();
        String login_email = email.getText().toString().trim();
        String login_pass = password.getText().toString().trim();

        if (login_email.isEmpty()){
            email.setError("Invalid Email");
            email.requestFocus();
            return;
        }
        if(!emailValid(login_email)){
            email.setError("Invalid Email");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(login_email).matches()){
            email.setError("Invalid Email");
            email.requestFocus();
            return;
        }
        if (login_pass.isEmpty()){
            email.setError("Invalid Password");
            email.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        if (numTries>=1){
            mAuth.signInWithEmailAndPassword(login_email, login_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user.isEmailVerified()){
                            Toast.makeText(MainActivity.this, "Successful login!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, HomePage.class));
                            finish();
                        }else{
                            Toast.makeText(MainActivity.this, "failed to login! Account must be verified, check your e-mail please :)", Toast.LENGTH_SHORT).show();
                            FirebaseUser userEmailVerification = FirebaseAuth.getInstance().getCurrentUser();
                            userEmailVerification.sendEmailVerification();
                            FirebaseAuth.getInstance().signOut();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "failed to login!", Toast.LENGTH_SHORT).show();
                        numTries--;
                        if(numTries<=0){
                            loginBT.setEnabled(false);
                            Toast.makeText(MainActivity.this, "Exceeded number of tries! Refresh your page after 5 minutes and your button should be enabled!", Toast.LENGTH_SHORT).show();
                        }
                        SharedPreferences.Editor editor = getSharedPreferences("timeForLoginBT", MODE_PRIVATE).edit();

                        timeOfLastFailedLogin = new Date().getTime();
                        SaveData();
                        numOfTries.setText("Number of login attempts: " + numTries);
                    }

                    progressBar.setVisibility(View.GONE);

                }
            });
        }else{
            numOfTries.setText("Number of login attempts: 0" );
            progressBar.setVisibility(View.GONE);
        }

    }

}