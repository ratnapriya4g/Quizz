package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    private TextView scored,total;
    private ImageView imageView;
    private Button doneBtn;
    private int marks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        getSupportActionBar().setTitle("Score Board");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView=findViewById(R.id.scored_image);
        scored=findViewById(R.id.scored);
        //total=findViewById(R.id.total);
        doneBtn=findViewById(R.id.done_btn);
        marks= Integer.parseInt(String.valueOf(getIntent().getIntExtra("score",0)));
        if(marks<1){
            imageView.setImageResource(R.drawable.bad);
            scored.setText("You Should Try Hard..\n"+"You Scored"+" "+String.valueOf(getIntent().getIntExtra("score",0)+" "+"marks."));
        }else if(marks==2){
            imageView.setImageResource(R.drawable.success);
            scored.setText("You Did Very Well..\n"+"You Scored"+" "+String.valueOf(getIntent().getIntExtra("score",0)+" "+"marks."));

        }else{
            imageView.setImageResource(R.drawable.good);
            scored.setText("You Can Do Better..\n"+"You Scored"+" "+String.valueOf(getIntent().getIntExtra("score",0)+" "+"marks."));

        }

       // scored.setText("You Scored"+" "+String.valueOf(getIntent().getIntExtra("score",0)+" "+"marks."));
      //  total.setText("Out Of"+""+String.valueOf(getIntent().getIntExtra("total",0)));

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
