package com.example.pooja.toddlertiles;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class TileGame extends AppCompatActivity {

    private Button start_button, end_game_button;
    private TextView score_textview, instruction;
    private Switch toggle;
    private LinearLayout instruction_switch;
    private ImageButton tile1, tile2, tile3, tile4, tile5, tile6, tile_medium1, tile_medium2, tile_medium3, tile_medium4, tile_medium5, tile_medium6;
    //private ImageButton[] all;
    int drawableResourceId1, drawableResourceId2, drawableResourceId3;
    int onclick_previous = 0, onclick_current = 0, current_id = 0, previous_id = 0;
    int scoreCount = 0;
    private Chronometer timer;
    private boolean isTimerRunning = false;
    long elapsedTime = 0;
    private long minutes;
    private long seconds;
    private int clickNumber = 0;

    private static int promptTechnique;
    private boolean isToggleChecked = false;
    private MediaPlayer mediaPlayer = null;

    Handler mHandler;

    String tile_names[] = {"apple", "banana", "grapes", "mango", "kiwi", "pineapple", "strawberry", "pomegranate", "orange", "melon"};

   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("message", "This is my message to be reloaded");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String message = savedInstanceState.getString("message");
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("promptTechnique", promptTechnique);
        super.onSaveInstanceState(savedInstanceState);
        Toast.makeText(getApplicationContext(), "OnSaveInstanceState called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_tile_game);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#f4b71c"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taponstart);
        mediaPlayer.start();

        start_button = findViewById(R.id.start_button);
        instruction = findViewById(R.id.instruction);
        toggle = findViewById(R.id.toggle);
        instruction_switch = findViewById(R.id.instruction_switch);
        score_textview = findViewById(R.id.score_textview);
        //time_taken_textview = findViewById(R.id.time_taken_textview);
        end_game_button = findViewById(R.id.end_game_button);
        timer = findViewById(R.id.timer);

        if(savedInstanceState != null) {
            promptTechnique = savedInstanceState.getInt("promptTechnique");
            Toast.makeText(getApplicationContext(), "savedInstance", Toast.LENGTH_SHORT).show();
        }else{
            promptTechnique = getIntent().getIntExtra("promptTechnique", 1);
            Toast.makeText(getApplicationContext(), "intentValue", Toast.LENGTH_SHORT).show();
        }

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/MouseMemoirs-Regular.ttf");
        start_button.setTypeface(face);
        instruction.setTypeface(face);
        end_game_button.setTypeface(face);
        timer.setTypeface(face);
        score_textview.setTypeface(face);

        tile1 = findViewById(R.id.tile1);
        tile2 = findViewById(R.id.tile2);
        tile3 = findViewById(R.id.tile3);
        tile4 = findViewById(R.id.tile4);
        tile5 = findViewById(R.id.tile5);
        tile6 = findViewById(R.id.tile6);
        //all = new ImageButton[]{tile1, tile2, tile3, tile4, tile5, tile6};

        mHandler = new Handler();

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isToggleChecked = true;
                    Toast.makeText(getApplicationContext(), "Instructions ON", Toast.LENGTH_SHORT).show();
                } else {
                    isToggleChecked = false;
                    Toast.makeText(getApplicationContext(), "Instructions OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }

                instruction_switch.setVisibility(View.GONE);
                start_button.setVisibility(View.GONE);
                instruction.setVisibility(View.GONE);
                tile1.setVisibility(View.VISIBLE);
                tile2.setVisibility(View.VISIBLE);
                tile3.setVisibility(View.VISIBLE);
                tile4.setVisibility(View.VISIBLE);
                tile5.setVisibility(View.VISIBLE);
                tile6.setVisibility(View.VISIBLE);
                score_textview.setVisibility(View.VISIBLE);

                //time_taken_textview.setVisibility(View.VISIBLE);
                end_game_button.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);

                if(isToggleChecked){
                    switch (promptTechnique){
                        case 4: {
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taponbabytile);
                            mediaPlayer.start();
                            break;
                        }
                    }
                }

                timer.setBase(SystemClock.elapsedRealtime());
                timer.start();
                isTimerRunning = true;
            }
        });


        end_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }

                mediaPlayer.release();

                if(isTimerRunning){
                    timer.stop();
                    elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();;
                    minutes = (elapsedTime / 1000) / 60;
                    seconds = (elapsedTime / 1000) % 60;
                }

                String time = String.valueOf(minutes) + " : " + String.valueOf(seconds);

                Intent intent = new Intent(getApplicationContext(), Scoreboard.class);
                intent.putExtra("score", scoreCount);
                intent.putExtra("time", time);
                startActivity(intent);
                finish();
            }
        });


        List<Integer> randlist = new ArrayList<Integer>();
        for (int i=0; i<tile_names.length; i++) {
            randlist.add(new Integer(i));
        }
        Collections.shuffle(randlist);

        int ar[] = new int[3];
        for(int i = 0; i<3; i++){
            ar[i] = randlist.get(i);
        }

        String random_value_1 = tile_names[ar[0]]; // generates a random tile
        drawableResourceId1 = this.getResources().getIdentifier(random_value_1, "drawable", this.getPackageName());
        //Log.d("drawableResourceId1" , Integer.toString(drawableResourceId1) );

        //tile_medium = all[random.nextInt(all.length)];

        ArrayList<ImageButton> list = new ArrayList<>();
        list.add(tile1);
        list.add(tile2);
        list.add(tile3);
        list.add(tile4);
        list.add(tile5);
        list.add(tile6);
        Collections.shuffle(list);

        Log.d("list elements", "onCreate: " + list.get(0) + " " + list.get(1) + " " + list.get(2) + " " + list.get(3) + " " + list.get(4) + " " + list.get(5)  );

        tile_medium1 = list.get(0);
        tile_medium2 = list.get(1);
        tile_medium3 = list.get(2);
        tile_medium4 = list.get(3);
        tile_medium5 = list.get(4);
        tile_medium6 = list.get(5);

        //Log.d("tile set 1" , tile_medium.getResources().getResourceName(tile_medium.getId()) + "  " +   tile_medium2.getResources().getResourceName(tile_medium2.getId()) );


        tile_medium1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isToggleChecked){
                    switch (promptTechnique){
                        case 4:{
                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }

                            if(clickNumber == 0){
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                    }
                }

                onclick_previous = onclick_current;
                onclick_current = drawableResourceId1;

                previous_id = current_id;
                current_id = tile_medium1.getId();

                ImageButton ib = findViewById(current_id);
                ib.setImageResource(drawableResourceId1);
                //tile_medium1.setImageResource(drawableResourceId1);


                //creates a delay of 1 second
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //noinspection InfiniteLoopStatement
                        while (true) {
                            try {
                                Thread.sleep(400);
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub

                                        if(onclick_previous == 0){
                                            //dont change the image on the button
                                        }

                                        else if(onclick_current == onclick_previous && previous_id!=current_id){
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if(scoreCount == 1 && clickNumber == 1){
                                                switch (promptTechnique){
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                }
                                            }
                                            else if(scoreCount == 3){
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if(isToggleChecked){
                                                    switch (promptTechnique){
                                                        case 4: {
                                                            if(mediaPlayer.isPlaying()){
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            score_textview.setText("Score: " + Integer.toString(scoreCount));
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton b1 = findViewById(current_id);
                                            ImageButton b2 = findViewById(previous_id);
                                            b1.setEnabled(false);
                                            b2.setEnabled(false);
                                            current_id = 0;
                                            previous_id = 0;
                                        }

                                        else {
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1  =  findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;

                                        }
                                    }
                                });
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                }).start();

            }
        });


        tile_medium2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isToggleChecked){
                    switch (promptTechnique){
                        case 4:{
                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }

                            if(clickNumber == 0){
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                    }
                }

                onclick_previous = onclick_current;
                onclick_current = drawableResourceId1;

                previous_id = current_id;
                current_id = tile_medium2.getId();

                //tile_medium2.setImageResource(drawableResourceId1);
                ImageButton ib = findViewById(current_id);
                ib.setImageResource(drawableResourceId1);


                //creates a delay of 1 second
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //noinspection InfiniteLoopStatement
                        while (true) {
                            try {
                                Thread.sleep(400);
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        if(onclick_previous == 0){
                                            //dont change the image on the button
                                        }

                                        else if(onclick_current == onclick_previous && previous_id!=current_id){
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if(scoreCount == 1 && clickNumber == 1){
                                                switch (promptTechnique){
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                }
                                            }
                                            else if(scoreCount == 3){
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if(isToggleChecked){
                                                    switch (promptTechnique){
                                                        case 4: {
                                                            if(mediaPlayer.isPlaying()){
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            score_textview.setText("Score: " + Integer.toString(scoreCount));
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton b1 = findViewById(current_id);
                                            ImageButton b2 = findViewById(previous_id);
                                            b1.setEnabled(false);
                                            b2.setEnabled(false);
                                            current_id = 0;
                                            previous_id = 0;
                                        }

                                        else {
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1  =  findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;

                                        }
                                    }
                                });
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                }).start();

            }
        });

        //tile_medium = list.get(2);
        //tile_medium2 = list.get(3);
        //Log.d("tile set 1" , tile_medium.getResources().getResourceName(tile_medium.getId()) + "  " +   tile_medium2.getResources().getResourceName(tile_medium2.getId()) );

        String random_value_2 = tile_names[ar[1]]; // generates a random tile
        drawableResourceId2 = this.getResources().getIdentifier(random_value_2, "drawable", this.getPackageName());
        //Log.d("drawableResourceId2" , Integer.toString(drawableResourceId2) );


        tile_medium3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isToggleChecked){
                    switch (promptTechnique){
                        case 4:{
                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }

                            if(clickNumber == 0){
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                    }
                }

                onclick_previous = onclick_current;
                onclick_current = drawableResourceId2;

                previous_id = current_id;
                current_id = tile_medium3.getId();

                //tile_medium3.setImageResource(drawableResourceId2);
                ImageButton ib = findViewById(current_id);
                ib.setImageResource(drawableResourceId2);


                //creates a delay of 1 second
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //noinspection InfiniteLoopStatement
                        while (true) {
                            try {
                                Thread.sleep(400);
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        if(onclick_previous == 0){
                                            //dont change the image on the button
                                        }

                                        else if(onclick_current == onclick_previous && previous_id!=current_id){
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if(scoreCount == 1 && clickNumber == 1){
                                                switch (promptTechnique){
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                }
                                            }
                                            else if(scoreCount == 3){
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if(isToggleChecked){
                                                    switch (promptTechnique){
                                                        case 4: {
                                                            if(mediaPlayer.isPlaying()){
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            score_textview.setText("Score: " + Integer.toString(scoreCount));
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton b1 = findViewById(current_id);
                                            ImageButton b2 = findViewById(previous_id);
                                            b1.setEnabled(false);
                                            b2.setEnabled(false);
                                            current_id = 0;
                                            previous_id = 0;
                                        }

                                        else {
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1  =  findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;

                                        }

                                    }
                                });
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                }).start();

            }
        });

        tile_medium4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isToggleChecked){
                    switch (promptTechnique){
                        case 4:{
                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }

                            if(clickNumber == 0){
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                    }
                }

                onclick_previous = onclick_current;
                onclick_current = drawableResourceId2;

                previous_id = current_id;
                current_id = tile_medium4.getId();

                //tile_medium4.setImageResource(drawableResourceId2);
                ImageButton ib = findViewById(current_id);
                ib.setImageResource(drawableResourceId2);


                //creates a delay of 1 second
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //noinspection InfiniteLoopStatement
                        while (true) {
                            try {
                                Thread.sleep(400);
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        if(onclick_previous == 0){
                                            //dont change the image on the button
                                        }

                                        else if(onclick_current == onclick_previous && previous_id!=current_id){
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if(scoreCount == 1 && clickNumber == 1){
                                                switch (promptTechnique){
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                }
                                            }
                                            else if(scoreCount == 3){
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if(isToggleChecked){
                                                    switch (promptTechnique){
                                                        case 4: {
                                                            if(mediaPlayer.isPlaying()){
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            score_textview.setText("Score: " + Integer.toString(scoreCount));
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton b1 = findViewById(current_id);
                                            ImageButton b2 = findViewById(previous_id);
                                            b1.setEnabled(false);
                                            b2.setEnabled(false);
                                            current_id = 0;
                                            previous_id = 0;
                                        }

                                        else {
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1  =  findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;

                                        }
                                    }
                                });
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                }).start();

            }
        });


        //tile_medium = list.get(4);
        //tile_medium2 = list.get(5);
        //Log.d("tile set 1" , tile_medium.getResources().getResourceName(tile_medium.getId()) + "  " +   tile_medium2.getResources().getResourceName(tile_medium2.getId()) );

        String random_value_3 = tile_names[ar[2]]; // generates a random tile
        drawableResourceId3 = this.getResources().getIdentifier(random_value_3, "drawable", this.getPackageName());
        Log.d("drawableResourceId3" , Integer.toString(drawableResourceId3) );


        tile_medium5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isToggleChecked){
                    switch (promptTechnique){
                        case 4:{
                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }

                            if(clickNumber == 0){
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                    }
                }

                onclick_previous = onclick_current;
                onclick_current = drawableResourceId3;

                previous_id = current_id;
                current_id = tile_medium5.getId();

                //tile_medium5.setImageResource(drawableResourceId3);
                ImageButton ib = findViewById(current_id);
                ib.setImageResource(drawableResourceId3);


                //creates a delay of 1 second
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //noinspection InfiniteLoopStatement
                        while (true) {
                            try {
                                Thread.sleep(400);
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        if(onclick_previous == 0){
                                            //dont change the image on the button
                                        }

                                        else if(onclick_current == onclick_previous && previous_id!=current_id){
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if(scoreCount == 1 && clickNumber == 1){
                                                switch (promptTechnique){
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                }
                                            }
                                            else if(scoreCount == 3){
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if(isToggleChecked){
                                                    switch (promptTechnique){
                                                        case 4: {
                                                            if(mediaPlayer.isPlaying()){
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            score_textview.setText("Score: " + Integer.toString(scoreCount));
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton b1 = findViewById(current_id);
                                            ImageButton b2 = findViewById(previous_id);
                                            b1.setEnabled(false);
                                            b2.setEnabled(false);
                                            current_id = 0;
                                            previous_id = 0;
                                        }

                                        else {

                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1  =  findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;

                                        }
                                    }
                                });
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                }).start();

            }
        });

        tile_medium6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isToggleChecked){
                    switch (promptTechnique){
                        case 4:{
                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }

                            if(clickNumber == 0){
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                    }
                }


                onclick_previous = onclick_current;
                onclick_current = drawableResourceId3;

                previous_id = current_id;
                current_id = tile_medium6.getId();

                //tile_medium6.setImageResource(drawableResourceId3);
                ImageButton ib = findViewById(current_id);
                ib.setImageResource(drawableResourceId3);


                //creates a delay of 1 second
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //noinspection InfiniteLoopStatement
                        while (true) {
                            try {
                                Thread.sleep(400);
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        if(onclick_previous == 0){
                                            //dont change the image on the button
                                        }

                                        else if(onclick_current == onclick_previous && previous_id!=current_id){
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if(scoreCount == 1 && clickNumber == 1){
                                                switch (promptTechnique){
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                }
                                            }
                                            else if(scoreCount == 3){
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if(isToggleChecked){
                                                    switch (promptTechnique){
                                                        case 4: {
                                                            if(mediaPlayer.isPlaying()){
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            score_textview.setText("Score: " + Integer.toString(scoreCount));
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton b1 = findViewById(current_id);
                                            ImageButton b2 = findViewById(previous_id);
                                            b1.setEnabled(false);
                                            b2.setEnabled(false);
                                            current_id = 0;
                                            previous_id = 0;

                                        }

                                        else {
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1  =  findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;

                                        }
                                    }
                                });
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                }).start();

            }
        });

    }

}
