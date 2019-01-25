package com.toddlertilesapp.pooja.toddlertiles;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by arshi on 11-01-2019.
 */

public class Rating extends AppCompatActivity {

    private int score, wrongTaps, promptTechnique;
    private float rate;
    private String time;
    private String agecat, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rating);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#f47485"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        score = getIntent().getIntExtra("score", 0);
        time = getIntent().getStringExtra("time");
        wrongTaps = getIntent().getIntExtra("wrongTaps", 0);
        promptTechnique = getIntent().getIntExtra("promptTechnique", 2);

        final RatingBar ratingBar = findViewById(R.id.ratingbar);
        final TextView rateusbox = findViewById(R.id.rateusbox);
        Button submit_btn = findViewById(R.id.submit_btn);

        final Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/MouseMemoirs-Regular.ttf");

        rateusbox.setTypeface(face);
        submit_btn.setTypeface(face);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate = ratingBar.getRating();
                //Toast.makeText(getApplicationContext(), Float.toString(rate), Toast.LENGTH_SHORT).show();
                //Log.d("RATE: ", Float.toString(rate));
            }
        });


        /*ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate = ratingBar.getRating();
                Toast.makeText(getApplicationContext(), Float.toString(rate), Toast.LENGTH_SHORT).show();
                Log.d("RATE: ", Float.toString(rate));
            }
        });*/

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Scoreboard.class);
                intent.putExtra("score", score);
                intent.putExtra("time", time);
                intent.putExtra("wrongTaps", wrongTaps);
                intent.putExtra("promptTechnique", promptTechnique);
                intent.putExtra("rating", rate);
                startActivity(intent);
                finish();
            }
        });
    }
}
