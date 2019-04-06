package com.project.mobility.view.activities.splash;

import android.content.Intent;
import android.os.Bundle;

import com.project.mobility.view.activities.OnboardingActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
        finish();
    }
}
