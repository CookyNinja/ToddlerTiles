package com.example.pooja.toddlertiles;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND_SERVICE;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class Scoreboard extends AppCompatActivity {

    private Button play_again_button, backto_menu_button;
    private TextView scorebox, timebox, messagebox, time_message, score_message;

    private MediaPlayer mediaPlayer = null;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private int score;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_scoreboard);

        sharedPreferences = getApplicationContext().getSharedPreferences("SharedPreference1" , MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#049da0"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taponplayagainbutton);
        mediaPlayer.start();

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
        backto_menu_button = findViewById(R.id.backto_menu_button);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/MouseMemoirs-Regular.ttf");

        messagebox.setTypeface(face);
        scorebox.setTypeface(face);
        timebox.setTypeface(face);
        time_message.setTypeface(face);
        score_message.setTypeface(face);
        play_again_button.setTypeface(face);
        backto_menu_button.setTypeface(face);

        play_again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                mediaPlayer.release();

                Intent intent = new Intent(getApplicationContext(), TileGame.class);
                startActivity(intent);
                finish();
            }
        });

        backto_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //clearing all data from sharedprefernce1 as it is not needed anymore
                editor.clear();
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), PromptTechnique.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public boolean foregrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND  ||  appProcessInfo.importance == IMPORTANCE_VISIBLE );
    }


    @Override
    protected void onDestroy() {
        //stop service and stop music
        if(!foregrounded()){
            stopService(new Intent(Scoreboard.this, SoundService.class));
        }
        super.onDestroy();
    }

}
