package myapplication.example.deltatask1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Layout;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class deltataskactivity2 extends AppCompatActivity {

    ConstraintLayout layout;
    TextView question;
    TextView answerdisplay;
    RadioGroup option_list;
    RadioButton option1;
    RadioButton option2;
    RadioButton option3;
    Button submit;
    long time_left;
    CountDownTimer time;
    int num;
    Vibrator wrong;
    int size = 0;
    int r;
    int correct;
    int score;
    int streak;
    TextView score_2;
    TextView streak_2;
    TextView for_time;
    long hold_answer;
    ArrayList<Integer> factors_adder = new ArrayList<>();
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deltataskactivity2);


       wrong = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        score_2 = (TextView)findViewById(R.id.highscore_1);
        streak_2 = (TextView)findViewById(R.id.longstreak_2);
        layout = (ConstraintLayout)findViewById(R.id.layout_2);
        num = getIntent().getIntExtra("number",0);
        for_time = (TextView)findViewById(R.id.timer);
        score = getIntent().getIntExtra("score",0);
        streak = getIntent().getIntExtra("streak",0);
        score_2.setText("SCORE: " + score);
        streak_2.setText("STREAK: " + streak);
        factors();
        answerdisplay = (TextView)findViewById(R.id.answerdisplay);
        question = (TextView)findViewById(R.id.question);
        option_list = (RadioGroup)findViewById(R.id.optionlist);
        option1 = (RadioButton)findViewById(R.id.option1);
        option2 = (RadioButton)findViewById(R.id.option2);
        option3 = (RadioButton)findViewById(R.id.option3);
        submit = (Button)findViewById(R.id.submit);
        question.setText("Tick the correct factor of " + num +":");

        score_2.setText("SCORE: " + score);
        streak_2.setText("STREAK: " + streak);
        set_option();
        time_left = 10000;
        start_timer();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if(option1.isChecked() || option2.isChecked() || option3.isChecked()){
                    checkAnswer();
                    time_left = 5000;
                    start_timer();
                    nextpart();

                }
                else
                    Toast.makeText(getApplicationContext(),"Please select an option",Toast.LENGTH_LONG).show();

            }
        });
    }

    public int correct_op(){
        correct = (int)(Math.random()*size);
        return correct;
    }


    public boolean factorChecker(int n){
        for(int i =0; i<size; i++){
            if(factors_adder.get(i) == n) return true;
        }
        return false;
    }

    public void factors(){
       for(int i = 2; i <= num; i++){
           if(num % i == 0){
               factors_adder.add(i);
               size++;
           }
       }


    }

    public int option_finder(){
        int rnd = 0;
        boolean condition = false;
        while (condition == false){
            rnd = (int)(Math.random()*num) + 2;
            if((!factorChecker(rnd))) condition = true;
        }
        return rnd;
    }

    public void set_option(){
        r = (int) (Math.random()*3);
        if(r>2) r = 2;
        switch(r){
            case 0:
                option1.setText(factors_adder.get(correct_op()) + "");
                option2.setText(option_finder() + "");
                option3.setText(option_finder() + "");
                break;

            case 1:
                option2.setText(factors_adder.get(correct_op()) + "");
                option3.setText(option_finder() + "");
                option1.setText(option_finder() + "");
                break;

            case 2:
                option3.setText(factors_adder.get(correct_op()) + "");
                option1.setText(option_finder() + "");
                option2.setText(option_finder() + "");
                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    public void checkAnswer() {
        RadioButton option_select = findViewById(option_list.getCheckedRadioButtonId());
        int op = option_list.indexOfChild(option_select);
        if(r == op){
            layout.setBackgroundResource(R.color.green);
            answerdisplay.setText("CORRECT ANSWER");
            score++;
            streak++;
            score_2.setText("SCORE:" + score);
            streak_2.setText("STREAK: " + streak);
        }
        else{
            if (Build.VERSION.SDK_INT >= 26) {
               wrong.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                wrong.vibrate(200);
            }
            layout.setBackgroundResource(R.color.red);
            score = 0;
            streak = 0;
            score_2.setText("SCORE:" + score);
            streak_2.setText("STREAK: " + streak);
            answerdisplay.setText("CORRECT ANSWER: " + factors_adder.get(correct));
        }


    }

    public void nextpart(){
        time.cancel();
        Intent back = new Intent();
        back.putExtra("score",score);
        back.putExtra("streak",streak);
        setResult(2,back);
        finish();
    }

    public void start_timer(){
        time = new CountDownTimer(time_left,1000) {
            @Override
            public void onTick( long millisUntilFinished ) {
                time_left = millisUntilFinished;
                update_time();
            }

            @Override
            public void onFinish() {
                time_left = 0;
                update_time();
                checkAnswer();
                nextpart();
            }
        }.start();
    }

    public void update_time(){
        int min = (int)(time_left/1000)/60;
        int sec = (int)(time_left/1000)%60;
        for_time.setText(min + ":0" + sec);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(time != null){
            time.cancel();
        }
    }
}
