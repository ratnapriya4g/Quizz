package com.example.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorStateListDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.quizz.R.drawable.btn_activity;

public class QuestionsActivity extends AppCompatActivity {

    public static final String FILE_NAME ="QUIZZ";
    public static final String KEY_NAME ="QUESTIONS";


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("");

    private TextView question,noindicator;
    private FloatingActionButton bookmarkBtn;
    private LinearLayout optionsContainer;
    private Button submitbtn, nextbtn;
    private int count=0;
    private List<QuestionModel> list;
    private int position=0;
    private int score=0;
    private String category;
    private int setNo;
    private Dialog loadingDialog;
    private List<QuestionModel> bookmarksList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private TextView timer;
    private int matchedQuestionPosition;
    boolean TestFinish =false;
    CountDownTimer countDownTimer;
    long oneminutmicrosec=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        getSupportActionBar().setTitle("Quizz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        question=findViewById(R.id.ques);
        noindicator=findViewById(R.id.no_indicator);
        bookmarkBtn=findViewById(R.id.border_btn);
        optionsContainer=findViewById(R.id.option_container);
        submitbtn=findViewById(R.id.submit_btn);
        nextbtn=findViewById(R.id.next_btn);
        timer=findViewById(R.id.timer);

        preferences=getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor=preferences.edit();
        gson=new Gson();

        getBookmarks();

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modelMatch()){
                    bookmarksList.remove(matchedQuestionPosition);
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                }else{
                    bookmarksList.add(list.get(position));
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                }
            }
        });

        category=getIntent().getStringExtra("category");
        setNo=getIntent().getIntExtra("setNo",1);

        loadingDialog= new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        list =new ArrayList<>();

        loadingDialog.show();
        for (int i=0;i<4;i++){
            optionsContainer.getChildAt(i).setVisibility(View.GONE);

        }

        myRef.child("SETS").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    list.add(snapshot.getValue(QuestionModel.class));
                }
                if(list.size()> 0 ){
                    for(int i=0;i<4;i++){
                        optionsContainer.getChildAt(i).setVisibility(View.VISIBLE);
                        optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkAnswer((Button)v);
                            }
                        });
                    }
                    playAnim(question,0,list.get(position).getQuestion());

                    nextbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextbtn.setEnabled(false);
                            nextbtn.setAlpha(0.7f);
                            enableOption(true);
                            position++;
                            if(position==list.size()){
                                //score activity
                               // submitbtn.setEnabled(true);
                                Intent scoreintent=new Intent(QuestionsActivity.this,ScoreActivity.class);
                                scoreintent.putExtra("score",score);
                                scoreintent.putExtra("total",list.size());
                                startActivity(scoreintent);
                                finish();
                                return;
                            }
                            count=0;
                            playAnim(question,0,list.get(position).getQuestion());
                        }
                    });
                    submitbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String body=list.get(position).getQuestion() + "\n" +
                                    list.get(position).getOptionA()+ "\n" +
                                    list.get(position).getOptionB()+  "\n" +
                                    list.get(position).getOptionC()+  "\n" +
                                    list.get(position).getOptionD();

                            Intent shareIntent=new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Quizz Challenge");
                            shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                            startActivity(Intent.createChooser(shareIntent,"Share via"));


                        }
                    });
                }
                else {
                    finish();
                    Toast.makeText(QuestionsActivity.this, "No Questions", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }

        });
        countDownTimer = new CountDownTimer(25 * 60000, 1000) {

            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                timer.setText("Time :- " +minutes+" m : "+oneminutmicrosec/1000+"sec left");
                if(oneminutmicrosec == 0){
                    secondcounter();
                }
            }
            public void onFinish() {
                TestFinish=true;
                Toast.makeText(QuestionsActivity.this, "Test Finished", Toast.LENGTH_SHORT).show();

            }
        }.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    private void playAnim(final View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
         .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(value == 0 && count<4){
                    String option="";
                    if(count == 0){
                        option= list.get(position).getOptionA();
                    }
                    else if(count ==1){
                        option= list.get(position).getOptionB();
                    }
                    else if(count ==2){
                        option= list.get(position).getOptionC();
                    }
                    else if(count ==3){
                        option= list.get(position).getOptionD();
                    }
                    playAnim(optionsContainer.getChildAt(count),0,option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if(value== 0){
                    try{
                        ((TextView)view).setText(data);
                        noindicator.setText("Questions: "+(position+1)+"/"+""+list.size());
                        if(modelMatch()){
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                        }else{
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                        }

                    }
                    catch (ClassCastException ex){
                        ((Button)view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view,1,data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button selectOption){


       enableOption(false);
        nextbtn.setEnabled(true);
        nextbtn.setAlpha(1);
        String btnstr =selectOption.getText().toString();
        String crtstr  =  list.get(position).getCorrectANS();

       if(btnstr.equals(crtstr)){
            //correctanswer
            score++;
            selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF87F506")));
        } else{
            //incorrect
            selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF81807")));
           Button correctoption =(Button) optionsContainer.findViewWithTag(list.get(position).getCorrectANS());
            correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF87F506")));
        }

    }

    private void enableOption(boolean enable){

        for(int i=0;i<4;i++) {
            optionsContainer.getChildAt(i).setEnabled(enable);
            if (enable) {
        //        optionsContainer.getChildAt(i).setBackgroundTintList(newsplashicon ColorStateListDrawable(Color.parseColor(newsplashicon ColorStateListDrawable(R.drawable.btn_activity))));
            optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9a75f7")));

            }
        }
    }
    private void getBookmarks(){
     String json =   preferences.getString(KEY_NAME,"");

        Type type=new TypeToken<List<QuestionModel>>(){}.getType();
        bookmarksList=gson.fromJson(json,type);

        if(bookmarksList==null){
            bookmarksList=new ArrayList<>();
        }
    }
    private boolean modelMatch(){
        boolean matched=false;
        int i=0;
        for (QuestionModel model: bookmarksList){
            if(model.getQuestion().equals(list.get(position).getQuestion()) &&
                    model.getCorrectANS().equals(list.get(position).getCorrectANS())&&
                    model.getSetNo()== list.get(position).getSetNo()) {
                matched = true;
                matchedQuestionPosition=i;

            }
            i++;
        }
        return matched;
    }

    private void storeBookmarks(){
        String json=gson.toJson(bookmarksList);

        editor.putString(KEY_NAME,json);
        editor.commit();
    }

    private void secondcounter(){

        new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                oneminutmicrosec=millisUntilFinished;
            }
            @Override
            public void onFinish() {
                oneminutmicrosec=0;
            }
        }.start();
    }
}
