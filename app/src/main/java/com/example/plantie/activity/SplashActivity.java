package com.example.plantie.activity;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.plantie.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        System.out.println(currentUser+"");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser != null) {
                    Intent intentWelcome = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intentWelcome);
                } else {
                    Intent intentSignIn = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intentSignIn);
                }
                finish();
            }
        }, 2000);
    }
}