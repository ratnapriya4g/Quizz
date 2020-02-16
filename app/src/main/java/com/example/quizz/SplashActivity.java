package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=2000;
  private   Animation topAnim,bottomAnim;
  private   ImageView image;
   private TextView logo,slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        topAnim=AnimationUtils.loadAnimation(this, R.anim.top_animation);
       bottomAnim= AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

       image=findViewById(R.id.splashImage);
       logo=findViewById(R.id.splashLogo);
       slogan=findViewById(R.id.splashSlogan);

       image.setAnimation(topAnim);
       logo.setAnimation(bottomAnim);
       slogan.setAnimation(bottomAnim);

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent splashintent=new Intent(SplashActivity.this,Login.class);

               //Attach all the elements those you want to animate in design
               Pair[]  pairs = new Pair[2];
               pairs[0] = new Pair<View,String>(image, "logo_image");
               pairs[1] = new Pair<View,String>(logo, "logo_text");

               //wrap the call in API level 21 or higher
               if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                   ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pairs);
                   startActivity(splashintent,options.toBundle());
               }
               finish();

           }
       }, SPLASH_SCREEN);
    }
}
