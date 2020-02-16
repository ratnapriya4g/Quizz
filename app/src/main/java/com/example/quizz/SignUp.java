package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity {

    Button signup,already_login;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout username, password,email,fullname,phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        signup=findViewById(R.id.signup);
        already_login=findViewById(R.id.already_login);

        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogan_name);
        fullname = findViewById(R.id.name);
        username = findViewById(R.id.username);
        email= findViewById(R.id.email);
        phoneNo = findViewById(R.id.phoneNo);
        password = findViewById(R.id.password);

        already_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent already_loginIntent=new Intent(SignUp.this,Login.class);
                Pair[] pairs = new Pair[10];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logoText, "logo_text");
                pairs[2] = new Pair<View, String>(sloganText, "logo_desc");
                pairs[3] = new Pair<View, String>(fullname, "fullname_tran");
                pairs[4] = new Pair<View, String>(username, "username_tran");
                pairs[5] = new Pair<View, String>(email, "email_tran");
                pairs[6] = new Pair<View, String>(phoneNo, "phoneNo_tran");
                pairs[7] = new Pair<View, String>(password, "password_tran");
                pairs[8] = new Pair<View, String>(signup, "button_tran");
                pairs[9] = new Pair<View, String>(already_login, "login_signup_tran");

                //wrap the call in API level 21 or higher
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);
                    startActivity(already_loginIntent,options.toBundle());
                    finish();
                }else {
                    already_loginIntent.putExtra("DATA",pairs);
                    startActivity(already_loginIntent);
                    finish();
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent=new Intent(SignUp.this,MainActivity.class);
                startActivity(signupIntent);
                finish();

            }
        });

    }

}
