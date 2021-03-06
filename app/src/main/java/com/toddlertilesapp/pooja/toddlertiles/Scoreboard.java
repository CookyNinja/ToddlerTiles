package com.toddlertilesapp.pooja.toddlertiles;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class Scoreboard extends AppCompatActivity {

    private Button play_again_button, backto_menu_button;
    private TextView scorebox, timebox, messagebox, time_message, score_message;

    private MediaPlayer mediaPlayer = null;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private int score, wrongTaps, promptTechnique;
    private float rate;
    private String time;
    private String agecat, gender;
    private boolean isLovingIt = false, isNotAbleToUnderstand = false;

    // Firebase Database Variables
    private DatabaseReference mRoot;
    private DatabaseReference mRecords;
    private DatabaseReference mId;
    private DatabaseReference mPromptTechnique, mScore,  mTimeTaken, mWrongTaps, mGender, mAgeCategory, mRating;
    public int numRecords;

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
        wrongTaps = getIntent().getIntExtra("wrongTaps", 0);
        promptTechnique = getIntent().getIntExtra("promptTechnique", 2);
        rate = getIntent().getFloatExtra("rating", 0);
        scorebox.setText(String.valueOf(score));
        timebox.setText(String.valueOf(time));
        if(score == 3){
            messagebox.setText("You won!");
        }else{
            messagebox.setText("Try again.");
        }

        Toast.makeText(getApplicationContext(), "Wrong Taps: " + Integer.toString(wrongTaps), Toast.LENGTH_SHORT).show();

        mRecords = FirebaseDatabase.getInstance().getReference().child("Records");

        mRecords.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numRecords = dataSnapshot.getValue(Integer.class);
                mRecords.setValue(numRecords+1);
                mRoot = FirebaseDatabase.getInstance().getReference();
                mId = mRoot.child(Integer.toString(numRecords));
                mPromptTechnique = mId.child("PromptTechnique");
                mPromptTechnique.setValue(promptTechnique);
                mScore = mId.child("Score");
                mScore.setValue(score);
                mTimeTaken = mId.child("TimeTaken");
                mTimeTaken.setValue(time);
                mWrongTaps = mId.child("WrongTaps");
                mWrongTaps.setValue(wrongTaps);
                mAgeCategory = mId.child("AgeCategory");
                agecat = sharedPreferences.getString("AgeCategory", null);
                mAgeCategory.setValue(agecat);
                mGender = mId.child("Gender");
                gender = sharedPreferences.getString("Gender" , null);
                mGender.setValue(gender);
                mRating = mId.child("Rating");
                mRating.setValue(rate);

                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.d("The read failed: ", "FAILED");
            }
        });


        /*mRoot = FirebaseDatabase.getInstance().getReference();
        mRecords = mRoot.child("Records");
        int numRecords = mRecords.;
        numRecords++;

        mRecords.setValue(numRecords);

        Toast.makeText(getApplicationContext(), Integer.toString(mRecords.hashCode()), Toast.LENGTH_SHORT).show();*/

        /*mUserPhoneNumber.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    mCount = mUserPhoneNumber.child("count");
                    mCount.setValue(0);
                }
            }*/

        play_again_button = findViewById(R.id.play_again_button);
        backto_menu_button = findViewById(R.id.backto_menu_button);


        //setting font here
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
                    mediaPlayer.reset();
                }

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
                editor.commit(); //commiting to save changes

                Intent intent = new Intent(getApplicationContext(), PromptTechnique.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //to check if app is on foreground
    public boolean foregrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND  ||  appProcessInfo.importance == IMPORTANCE_VISIBLE );
    }

    @Override
    protected void onPause() {
        if (!this.isFinishing()){
            //stops the background music and service
            stopService(new Intent(Scoreboard.this, SoundService.class));
            if (mediaPlayer!= null) mediaPlayer.release();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        //starts bg music when app returns to foreground
        startService(new Intent(Scoreboard.this, SoundService.class));
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        //stop service and stop music
        if(!foregrounded()){
            stopService(new Intent(Scoreboard.this, SoundService.class));
            if (mediaPlayer!= null) mediaPlayer.release();

        }
        super.onDestroy();
        if (mediaPlayer!= null) mediaPlayer.release();
    }

}
