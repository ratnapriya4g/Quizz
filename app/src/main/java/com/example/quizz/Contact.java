package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Contact extends AppCompatActivity {
    Button dial1,dial2,email,headoffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        dial1=findViewById(R.id.cont1);
        dial2=findViewById(R.id.cont2);
        email=findViewById(R.id.emailaddr);
        headoffice=findViewById(R.id.headofficeaddr);
        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        headoffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/Plot+No.+404,+3rd+Floor+Sentrum+Mall+New+Shristinagar,+Asansol-713305+West+Bengal/@23.7035397,86.9459432,17z/data=!3m1!4b1")));
            }
        });
        dial1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="+917979038537";
                Intent intent=new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",phone,null));
                startActivity(intent);
            }
        });
        dial2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="+917001685402";
                Intent intent=new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",phone,null));
                startActivity(intent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("Plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"contact@isera.in"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"Subject");
                intent.putExtra(Intent.EXTRA_TEXT,"Good Morning");
                startActivity(Intent.createChooser(intent,""));
            }
        });
    }
}
