package com.example.pooja.toddlertiles;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Scoreboard extends AppCompatActivity {

    private Button play_again_button;
    private TextView scorebox, timebox, messagebox, time_message, score_message;

    private int score;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_scoreboard);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#049da0"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        scorebox = (TextView) findViewById(R.id.scorebox);
        timebox = (TextView) findViewById(R.id.timebox);
        messagebox = (TextView) findViewById(R.id.message);
        time_message = (TextView) findViewById(R.id.time_message);
        score_message = (TextView) findViewById(R.id.score_message);

        score = getIntent().getIntExtra("score", 0);
        time = getIntent().getStringExtra("time");
        scorebox.setText(String.valueOf(score));
        timebox.setText(String.valueOf(time));
        if(score == 3){
            messagebox.setText("You won!");
        }else{
            messagebox.setText("Try again.");
        }

        play_again_button = findViewById(R.id.play_again_button);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/MouseMemoirs-Regular.ttf");

        messagebox.setTypeface(face);
        scorebox.setTypeface(face);
        timebox.setTypeface(face);
        time_message.setTypeface(face);
        score_message.setTypeface(face);
        play_again_button.setTypeface(face);

        play_again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TileGame.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
