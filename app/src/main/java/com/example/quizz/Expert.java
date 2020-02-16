package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Expert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);

        getSupportActionBar(). setTitle("Expert's");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
