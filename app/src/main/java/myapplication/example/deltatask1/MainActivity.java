package myapplication.example.deltatask1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText number1;
    Button start1;
    int score ;
    int streak ;
    TextView highscore;
    TextView longeststreak;
    int highest_score;
    int longest_streak;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        highscore = (TextView) findViewById(R.id.highscore);
        longeststreak = (TextView)findViewById(R.id.longeststreak);
        number1 = (EditText)findViewById(R.id.number1);
        load();
        update();
        start1 = (Button)findViewById(R.id.start1);
        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                int num = Integer.parseInt(number1.getText().toString());
                Intent si1 = new Intent(getApplicationContext(),deltataskactivity2.class);
                si1.putExtra("number",num);
                si1.putExtra("score",score);
                si1.putExtra("streak",streak);
                startActivityForResult(si1,2);
            }
        });
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == resultCode){
            int score_back = data.getIntExtra("score",0);
            int streak_back = data.getIntExtra("streak",0);
            score = score_back;
            streak = streak_back;
            if(highest_score < score_back || longest_streak < streak_back) {
                highest_score = score_back;
                longest_streak = streak_back;
                update();
                save();
            }
        }
    }

    public void save(){
        SharedPreferences s1 = getSharedPreferences("task1",MODE_PRIVATE);
        SharedPreferences.Editor ed = s1.edit();
        ed.putInt("score",highest_score);
        ed.putInt("streak",longest_streak);
        ed.apply();
    }

    public void load(){
        SharedPreferences s2 = getSharedPreferences("task1",MODE_PRIVATE);
        highest_score = s2.getInt("score",0);
        longest_streak = s2.getInt("streak",0);
    }

    public void update(){
        highscore.setText("HIGHEST SCORE: " + highest_score);
        longeststreak.setText("LONGEST STREAK: " + longest_streak);
    }
}
