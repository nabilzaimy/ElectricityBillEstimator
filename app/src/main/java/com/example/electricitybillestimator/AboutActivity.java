package com.example.electricitybillestimator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // You might want to dynamically set the developer info if it comes from string resources
        // For now, it's hardcoded in activity_about.xml
        // Remember to replace placeholders in activity_about.xml with your actual details.
    }
}