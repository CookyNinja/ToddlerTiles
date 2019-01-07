package com.example.pooja.toddlertiles;


import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.service.quicksettings.Tile;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;


public class TileGame extends AppCompatActivity {

    private Button start_button, end_game_button;
    private TextView score_textview, instruction;
    private Switch toggle;
    private LinearLayout instruction_switch;
    private ImageButton tile1, tile2, tile3, tile4, tile5, tile6, tile_medium1, tile_medium2, tile_medium3, tile_medium4, tile_medium5, tile_medium6;
    //private ImageButton[] all;
    private ImageView handpointer, handpointer2, handpointerend;
    int drawableResourceId1, drawableResourceId2, drawableResourceId3;
    int onclick_previous = 0, onclick_current = 0, current_id = 0, previous_id = 0;
    int scoreCount = 0;
    private Chronometer timer;
    private boolean isTimerRunning = false;
    long elapsedTime = 0;
    private long minutes;
    private long seconds;
    private int clickNumber = 0;
    private int wrongTaps = 0;

    ValueAnimator valueAnimator;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    private boolean isToggleChecked;
    private MediaPlayer mediaPlayer = null;
    private int promptTechnique;

    Handler mHandler;

    String tile_names[] = {"apple", "banana", "grapes", "mango", "kiwi", "pineapple", "strawberry", "pomegranate", "orange", "melon"};

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        //savedInstanceState.putInt("promptTechnique", promptTechnique);
        super.onSaveInstanceState(savedInstanceState);
        //Toast.makeText(getApplicationContext(), "OnSaveInstanceState called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_tile_game);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#f4b71c"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taponstart);
        mediaPlayer.start();

        //sharedPreference for promptTechnique variable and toggle button
        sharedPreferences = getApplicationContext().getSharedPreferences("SharedPreference1", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //if nothing stored, then returns default value 2, i.e. text instructions at the start
        promptTechnique = sharedPreferences.getInt("promptTechnique", 2);

        start_button = findViewById(R.id.start_button);
        instruction = findViewById(R.id.instruction);
        toggle = findViewById(R.id.toggle);
        instruction_switch = findViewById(R.id.instruction_switch);
        score_textview = findViewById(R.id.score_textview);
        //time_taken_textview = findViewById(R.id.time_taken_textview);
        end_game_button = findViewById(R.id.end_game_button);
        timer = findViewById(R.id.timer);

        //setting font here
        final Typeface face = Typeface.createFromAsset(getAssets(),
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
                    editor.putBoolean("toggleButton", true);
                    editor.commit();
                    isToggleChecked = sharedPreferences.getBoolean("toggleButton", true);
                    //Toast.makeText(getApplicationContext(), "Instructions ON", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putBoolean("toggleButton", false);
                    editor.commit();
                    isToggleChecked = sharedPreferences.getBoolean("toggleButton", false);
                    //Toast.makeText(getApplicationContext(), "Instructions OFF", Toast.LENGTH_SHORT).show();
                }
            }


        });


        //setting toggle button based on the value present in sharedpreference
        //by default it will be ON
        toggle.setChecked(sharedPreferences.getBoolean("toggleButton", true));


        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                Log.d("Prompt_technique = ", promptTechnique + " ");

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

                if (isToggleChecked) {
                    switch (promptTechnique) {
                        case 4: {
                            //audio instructions on the fly
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taponbabytile);
                            mediaPlayer.start();

                            //timer starts now
                            timer.setBase(SystemClock.elapsedRealtime());
                            timer.start();
                            isTimerRunning = true;
                            break;
                        }
                        case 2: {

                            //text instructions on start
                            //displays the dialog box of instructions
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                            View mView = getLayoutInflater().inflate(R.layout.dialog_instructions, null);

                            Button close_button = mView.findViewById(R.id.close_button);
                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dialog.show();

                            close_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                  //close the dialog box
                                    dialog.dismiss();

                                    //timer starts now
                                    timer.setBase(SystemClock.elapsedRealtime());
                                    timer.start();
                                    isTimerRunning = true;

                                }
                            });

                            break;
                        }

                        case 3: {

                            //text instructions on the fly
                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                            View mView = getLayoutInflater().inflate(R.layout.instruction_one, null);

                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dialog.show();

                            //waiting for 3 seconds and then closing dialog box
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    //timer starts now
                                    timer.setBase(SystemClock.elapsedRealtime());
                                    timer.start();
                                    isTimerRunning = true;
                                }
                            }, 3000);

                            break;
                        }

                        case 1: {

                            //displays video in a dialog box
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                            View mView = getLayoutInflater().inflate(R.layout.dialog_video_tutorial, null);

                            final VideoView tutorial_videoview = mView.findViewById(R.id.tutorial_videoview);
                            final Button close_tutorial_button = mView.findViewById(R.id.close_tutorial_button);
                            final Button play_again_button = mView.findViewById(R.id.play_again_button);
                            final TextView display_after_tutorial = mView.findViewById(R.id.display_after_tutorial);
                            final LinearLayout linear_layout_for_message = mView.findViewById(R.id.linear_layout_for_message);
                            display_after_tutorial.setTypeface(face);

                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dialog.show();

                            //creating the videopath from raw folder
                            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.tutorial_video;
                            final Uri uri = Uri.parse(videoPath);

                            //setting the path to the video view
                            tutorial_videoview.setVideoURI(uri);
                            //tutorial_videoview.setBackgroundColor(Color.WHITE);

                            //starting video automatically
                            tutorial_videoview.start();

                            tutorial_videoview.setZOrderOnTop(true);

                            play_again_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (tutorial_videoview.isPlaying()) {

                                        //stop if already playing
                                        tutorial_videoview.stopPlayback();
                                    }

                                    //replay it by setting its path again
                                    tutorial_videoview.setVideoURI(uri);
                                    tutorial_videoview.start();
                                }
                            });

                            close_tutorial_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    //when closing the video dialog box
                                    tutorial_videoview.setVisibility(View.GONE);
                                    play_again_button.setVisibility(View.GONE);
                                    close_tutorial_button.setVisibility(View.GONE);
                                    display_after_tutorial.setVisibility(View.VISIBLE);
                                    linear_layout_for_message.setVisibility(View.VISIBLE);

                                    //we dismiss the message after 2 seconds
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            //timer starts now
                                            timer.setBase(SystemClock.elapsedRealtime());
                                            timer.start();
                                            isTimerRunning = true;

                                        }
                                    }, 2000);

                                }
                            });


                            break;
                        }

                        case 5: {

                            int location[] = new int[2];
                            tile2.getLocationInWindow(location);

                            FrameLayout root = (FrameLayout)findViewById(R.id.my_frame_layout);
                            handpointer = new ImageView(getApplicationContext());
                            handpointer.setBackgroundResource(R.drawable.handpointer);

                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, 500);
                            params.leftMargin = location[0]+100;
                            params.topMargin  = location[1]-50;
                            root.addView(handpointer, params);


                            valueAnimator = ValueAnimator.ofFloat(50f, -50f);

                            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
                            valueAnimator.setDuration(800);
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float progress = (float) animation.getAnimatedValue();
                                    handpointer.setTranslationY(progress);
                                    handpointer.setTranslationX(progress);
                                    // no need to use invalidate() as it is already present in             //the text view.
                                }
                            });
                            valueAnimator.setRepeatCount(1);
                            valueAnimator.start();

                            //Toast.makeText(getApplicationContext(), Integer.toString(location[0]), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(), "COORDINATES", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(), Integer.toString(location[1]), Toast.LENGTH_SHORT).show();

                            //timer starts now
                            timer.setBase(SystemClock.elapsedRealtime());
                            timer.start();
                            isTimerRunning = true;
                            break;
                        }


                    }

                } else {
                    //the timer starts incase the instruction toggle was off
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.start();
                    isTimerRunning = true;
                }


            }
        });


        end_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                mediaPlayer.release();

                if (isTimerRunning) {
                    timer.stop();
                    elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();
                    ;
                    minutes = (elapsedTime / 1000) / 60;
                    seconds = (elapsedTime / 1000) % 60;
                }

                String time = String.valueOf(minutes) + " : " + String.valueOf(seconds);

                Intent intent = new Intent(getApplicationContext(), Scoreboard.class);
                intent.putExtra("score", scoreCount);
                intent.putExtra("time", time);
                intent.putExtra("wrongTaps", wrongTaps);
                startActivity(intent);
                finish();
            }
        });


        List<Integer> randlist = new ArrayList<Integer>();
        for (int i = 0; i < tile_names.length; i++) {
            randlist.add(new Integer(i));
        }
        Collections.shuffle(randlist);

        int ar[] = new int[3];
        for (int i = 0; i < 3; i++) {
            ar[i] = randlist.get(i);
        }

        String random_value_1 = tile_names[ar[0]]; // generates a random tile
        drawableResourceId1 = this.getResources().getIdentifier(random_value_1, "drawable", this.getPackageName());
        //Log.d("drawableResourceId1" , Integer.toString(drawableResourceId1) );

        //tile_medium = all[random.nextInt(all.length)];

        final ArrayList<ImageButton> list = new ArrayList<>();
        list.add(tile1);
        list.add(tile2);
        list.add(tile3);
        list.add(tile4);
        list.add(tile5);
        list.add(tile6);
        Collections.shuffle(list);

        Log.d("list elements", "onCreate: " + list.get(0) + " " + list.get(1) + " " + list.get(2) + " " + list.get(3) + " " + list.get(4) + " " + list.get(5));

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

                if (isToggleChecked) {
                    switch (promptTechnique) {
                        case 4: {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                            }

                            if (clickNumber == 0) {
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                        case 3: {

                            if (clickNumber == 0) {
                                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                View mView = getLayoutInflater().inflate(R.layout.instruction_two, null);

                                mBuilder.setView(mView);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.show();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();

                                    }
                                }, 3000);

                                clickNumber++;
                            }
                            break;
                        }
                        case 5: {
                            handpointer.setVisibility(View.GONE);
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

                                        if (onclick_previous == 0) {
                                            //dont change the image on the button
                                        } else if (onclick_current == onclick_previous && previous_id != current_id) {
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if (scoreCount == 1 && clickNumber == 1) {
                                                switch (promptTechnique) {
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                    case 3: {

                                                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                        View mView = getLayoutInflater().inflate(R.layout.instruction_three, null);

                                                        mBuilder.setView(mView);
                                                        final AlertDialog dialog = mBuilder.create();
                                                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                        dialog.show();

                                                        final Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dialog.dismiss();

                                                            }
                                                        }, 3000);

                                                        clickNumber++;
                                                        break;

                                                    }
                                                }
                                            } else if (scoreCount == 3) {
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();
                                                ;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if (isToggleChecked) {
                                                    switch (promptTechnique) {
                                                        case 4: {
                                                            if (mediaPlayer.isPlaying()) {
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }
                                                        case 3: {

                                                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                            View mView = getLayoutInflater().inflate(R.layout.instruction_four, null);

                                                            mBuilder.setView(mView);
                                                            final AlertDialog dialog = mBuilder.create();
                                                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                            dialog.show();

                                                            final Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    dialog.dismiss();

                                                                }
                                                            }, 3000);

                                                            break;

                                                        }
                                                        case 5: {
                                                            int location[] = new int[2];
                                                            end_game_button.getLocationInWindow(location);

                                                            FrameLayout root = (FrameLayout)findViewById(R.id.my_frame_layout);
                                                            handpointerend = new ImageView(getApplicationContext());
                                                            handpointerend.setBackgroundResource(R.drawable.handpointer);

                                                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, 500);
                                                            params.leftMargin = location[0]+100;
                                                            params.topMargin  = location[1]-50;
                                                            root.addView(handpointerend, params);


                                                            valueAnimator = ValueAnimator.ofFloat(50f, -50f);

                                                            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
                                                            valueAnimator.setDuration(800);
                                                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                                @Override
                                                                public void onAnimationUpdate(ValueAnimator animation) {
                                                                    float progress = (float) animation.getAnimatedValue();
                                                                    handpointerend.setTranslationY(progress);
                                                                    handpointerend.setTranslationX(progress);
                                                                    // no need to use invalidate() as it is already present in             //the text view.
                                                                }
                                                            });
                                                            valueAnimator.setRepeatCount(1);
                                                            valueAnimator.start();
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
                                        } else {
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1 = findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;
                                            wrongTaps++;

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

                if (isToggleChecked) {
                    switch (promptTechnique) {
                        case 4: {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                            }

                            if (clickNumber == 0) {
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                        case 3: {

                            if (clickNumber == 0) {
                                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                View mView = getLayoutInflater().inflate(R.layout.instruction_two, null);

                                mBuilder.setView(mView);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.show();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();

                                    }
                                }, 3000);

                                clickNumber++;
                            }
                            break;
                        }
                        case 5: {
                            handpointer.setVisibility(View.INVISIBLE);




                                /*if(((BitmapDrawable)tile_medium2.getDrawable()).getBitmap() == ((BitmapDrawable)tile_medium1.getDrawable()).getBitmap()){tile2
                                    tileToPoint = tile_medium1;
                                    Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                                }else if(((BitmapDrawable)tile_medium2.getDrawable()).getBitmap() == ((BitmapDrawable)tile_medium3.getDrawable()).getBitmap()){
                                    tileToPoint = tile_medium3;
                                    Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
                                }else if(((BitmapDrawable)tile_medium2.getDrawable()).getBitmap() == ((BitmapDrawable)tile_medium4.getDrawable()).getBitmap()){
                                    tileToPoint = tile_medium4;
                                    Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_SHORT).show();
                                }else if(((BitmapDrawable)tile_medium2.getDrawable()).getBitmap() == ((BitmapDrawable)tile_medium5.getDrawable()).getBitmap()){
                                    tileToPoint = tile_medium5;
                                    Toast.makeText(getApplicationContext(), "5", Toast.LENGTH_SHORT).show();
                                }else if(((BitmapDrawable)tile_medium2.getDrawable()).getBitmap() == ((BitmapDrawable)tile_medium6.getDrawable()).getBitmap()){
                                    tileToPoint = tile_medium6;
                                    Toast.makeText(getApplicationContext(), "6", Toast.LENGTH_SHORT).show();
                                }

                                int location[] = new int[2];
                                tileToPoint.getLocationInWindow(location);

                                FrameLayout root = (FrameLayout)findViewById(R.id.my_frame_layout);
                                handpointer2 = new ImageView(getApplicationContext());
                                handpointer2.setBackgroundResource(R.drawable.handpointer);

                                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(400, 400);
                                params.leftMargin = location[0]+100;
                                params.topMargin  = location[1]-50;
                                root.addView(handpointer2, params);


                                valueAnimator = ValueAnimator.ofFloat(50f, -50f);

                                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
                                valueAnimator.setDuration(1000);
                                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        float progress = (float) animation.getAnimatedValue();
                                        handpointer2.setTranslationY(progress);
                                        handpointer2.setTranslationX(progress);
                                        // no need to use invalidate() as it is already present in             //the text view.
                                    }
                                });
                                valueAnimator.setRepeatCount(1);
                                valueAnimator.start();*/
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
                                        if (onclick_previous == 0) {
                                            //dont change the image on the button
                                        } else if (onclick_current == onclick_previous && previous_id != current_id) {
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if (scoreCount == 1 && clickNumber == 1) {
                                                switch (promptTechnique) {
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                    case 3: {

                                                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                        View mView = getLayoutInflater().inflate(R.layout.instruction_three, null);

                                                        mBuilder.setView(mView);
                                                        final AlertDialog dialog = mBuilder.create();
                                                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                        dialog.show();

                                                        final Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dialog.dismiss();

                                                            }
                                                        }, 3000);

                                                        clickNumber++;

                                                        break;

                                                    }
                                                }
                                            } else if (scoreCount == 3) {
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();
                                                ;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if (isToggleChecked) {
                                                    switch (promptTechnique) {
                                                        case 4: {
                                                            if (mediaPlayer.isPlaying()) {
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }

                                                        case 3: {

                                                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                            View mView = getLayoutInflater().inflate(R.layout.instruction_four, null);

                                                            mBuilder.setView(mView);
                                                            final AlertDialog dialog = mBuilder.create();
                                                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                            dialog.show();

                                                            final Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    dialog.dismiss();

                                                                }
                                                            }, 3000);

                                                            break;

                                                        }
                                                        case 5: {
                                                            int location[] = new int[2];
                                                            end_game_button.getLocationInWindow(location);

                                                            FrameLayout root = (FrameLayout)findViewById(R.id.my_frame_layout);
                                                            handpointerend = new ImageView(getApplicationContext());
                                                            handpointerend.setBackgroundResource(R.drawable.handpointer);

                                                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, 500);
                                                            params.leftMargin = location[0]+100;
                                                            params.topMargin  = location[1]-50;
                                                            root.addView(handpointerend, params);


                                                            valueAnimator = ValueAnimator.ofFloat(50f, -50f);

                                                            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
                                                            valueAnimator.setDuration(800);
                                                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                                @Override
                                                                public void onAnimationUpdate(ValueAnimator animation) {
                                                                    float progress = (float) animation.getAnimatedValue();
                                                                    handpointerend.setTranslationY(progress);
                                                                    handpointerend.setTranslationX(progress);
                                                                    // no need to use invalidate() as it is already present in             //the text view.
                                                                }
                                                            });
                                                            valueAnimator.setRepeatCount(1);
                                                            valueAnimator.start();
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
                                        } else {
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1 = findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;
                                            wrongTaps++;

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

                if (isToggleChecked) {
                    switch (promptTechnique) {
                        case 4: {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                            }

                            if (clickNumber == 0) {
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                        case 3: {

                            if (clickNumber == 0) {
                                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                View mView = getLayoutInflater().inflate(R.layout.instruction_two, null);

                                mBuilder.setView(mView);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.show();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();

                                    }
                                }, 3000);

                                clickNumber++;
                            }
                            break;
                        }
                        case 5: {
                           handpointer.setVisibility(View.GONE);
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
                                        if (onclick_previous == 0) {
                                            //dont change the image on the button
                                        } else if (onclick_current == onclick_previous && previous_id != current_id) {
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if (scoreCount == 1 && clickNumber == 1) {
                                                switch (promptTechnique) {
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                    case 3: {

                                                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                        View mView = getLayoutInflater().inflate(R.layout.instruction_three, null);

                                                        mBuilder.setView(mView);
                                                        final AlertDialog dialog = mBuilder.create();
                                                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                        dialog.show();

                                                        final Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dialog.dismiss();

                                                            }
                                                        }, 3000);

                                                        clickNumber++;
                                                        break;

                                                    }
                                                }
                                            } else if (scoreCount == 3) {
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();
                                                ;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if (isToggleChecked) {
                                                    switch (promptTechnique) {
                                                        case 4: {
                                                            if (mediaPlayer.isPlaying()) {
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }
                                                        case 3: {

                                                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                            View mView = getLayoutInflater().inflate(R.layout.instruction_four, null);

                                                            mBuilder.setView(mView);
                                                            final AlertDialog dialog = mBuilder.create();
                                                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                            dialog.show();

                                                            final Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    dialog.dismiss();

                                                                }
                                                            }, 3000);

                                                            break;

                                                        }
                                                        case 5: {
                                                            int location[] = new int[2];
                                                            end_game_button.getLocationInWindow(location);

                                                            FrameLayout root = (FrameLayout)findViewById(R.id.my_frame_layout);
                                                            handpointerend = new ImageView(getApplicationContext());
                                                            handpointerend.setBackgroundResource(R.drawable.handpointer);

                                                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, 500);
                                                            params.leftMargin = location[0]+100;
                                                            params.topMargin  = location[1]-50;
                                                            root.addView(handpointerend, params);


                                                            valueAnimator = ValueAnimator.ofFloat(50f, -50f);

                                                            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
                                                            valueAnimator.setDuration(800);
                                                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                                @Override
                                                                public void onAnimationUpdate(ValueAnimator animation) {
                                                                    float progress = (float) animation.getAnimatedValue();
                                                                    handpointerend.setTranslationY(progress);
                                                                    handpointerend.setTranslationX(progress);
                                                                    // no need to use invalidate() as it is already present in             //the text view.
                                                                }
                                                            });
                                                            valueAnimator.setRepeatCount(1);
                                                            valueAnimator.start();
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
                                        } else {
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1 = findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;
                                            wrongTaps++;

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

                if (isToggleChecked) {
                    switch (promptTechnique) {
                        case 4: {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                            }

                            if (clickNumber == 0) {
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                        case 3: {

                            if (clickNumber == 0) {
                                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                View mView = getLayoutInflater().inflate(R.layout.instruction_two, null);

                                mBuilder.setView(mView);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.show();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();

                                    }
                                }, 3000);

                                clickNumber++;
                            }
                            break;
                        }
                        case 5: {
                            handpointer.setVisibility(View.GONE);
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
                                        if (onclick_previous == 0) {
                                            //dont change the image on the button
                                        } else if (onclick_current == onclick_previous && previous_id != current_id) {
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if (scoreCount == 1 && clickNumber == 1) {
                                                switch (promptTechnique) {
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                    case 3: {

                                                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                        View mView = getLayoutInflater().inflate(R.layout.instruction_three, null);

                                                        mBuilder.setView(mView);
                                                        final AlertDialog dialog = mBuilder.create();
                                                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                        dialog.show();

                                                        final Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dialog.dismiss();

                                                            }
                                                        }, 3000);

                                                        clickNumber++;
                                                        break;

                                                    }
                                                }
                                            } else if (scoreCount == 3) {
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();
                                                ;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if (isToggleChecked) {
                                                    switch (promptTechnique) {
                                                        case 4: {
                                                            if (mediaPlayer.isPlaying()) {
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }

                                                        case 3: {

                                                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                            View mView = getLayoutInflater().inflate(R.layout.instruction_four, null);

                                                            mBuilder.setView(mView);
                                                            final AlertDialog dialog = mBuilder.create();
                                                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                            dialog.show();

                                                            final Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    dialog.dismiss();

                                                                }
                                                            }, 3000);

                                                            break;

                                                        }
                                                        case 5: {
                                                            int location[] = new int[2];
                                                            end_game_button.getLocationInWindow(location);

                                                            FrameLayout root = (FrameLayout)findViewById(R.id.my_frame_layout);
                                                            handpointerend = new ImageView(getApplicationContext());
                                                            handpointerend.setBackgroundResource(R.drawable.handpointer);

                                                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, 500);
                                                            params.leftMargin = location[0]+100;
                                                            params.topMargin  = location[1]-50;
                                                            root.addView(handpointerend, params);


                                                            valueAnimator = ValueAnimator.ofFloat(50f, -50f);

                                                            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
                                                            valueAnimator.setDuration(800);
                                                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                                @Override
                                                                public void onAnimationUpdate(ValueAnimator animation) {
                                                                    float progress = (float) animation.getAnimatedValue();
                                                                    handpointerend.setTranslationY(progress);
                                                                    handpointerend.setTranslationX(progress);
                                                                    // no need to use invalidate() as it is already present in             //the text view.
                                                                }
                                                            });
                                                            valueAnimator.setRepeatCount(1);
                                                            valueAnimator.start();
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
                                        } else {
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1 = findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;
                                            wrongTaps++;

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
        Log.d("drawableResourceId3", Integer.toString(drawableResourceId3));


        tile_medium5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isToggleChecked) {
                    switch (promptTechnique) {
                        case 4: {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                            }

                            if (clickNumber == 0) {
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                        case 3: {

                            if (clickNumber == 0) {
                                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                View mView = getLayoutInflater().inflate(R.layout.instruction_two, null);

                                mBuilder.setView(mView);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.show();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();

                                    }
                                }, 3000);

                                clickNumber++;
                            }
                            break;
                        }
                        case 5: {
                            handpointer.setVisibility(View.GONE);
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
                                        if (onclick_previous == 0) {
                                            //dont change the image on the button
                                        } else if (onclick_current == onclick_previous && previous_id != current_id) {
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if (scoreCount == 1 && clickNumber == 1) {
                                                switch (promptTechnique) {
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                    case 3: {

                                                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                        View mView = getLayoutInflater().inflate(R.layout.instruction_three, null);

                                                        mBuilder.setView(mView);
                                                        final AlertDialog dialog = mBuilder.create();
                                                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                        dialog.show();

                                                        final Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dialog.dismiss();

                                                            }
                                                        }, 3000);

                                                        clickNumber++;
                                                        break;

                                                    }
                                                }
                                            } else if (scoreCount == 3) {
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();
                                                ;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if (isToggleChecked) {
                                                    switch (promptTechnique) {
                                                        case 4: {
                                                            if (mediaPlayer.isPlaying()) {
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }

                                                        case 3: {

                                                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                            View mView = getLayoutInflater().inflate(R.layout.instruction_four, null);

                                                            mBuilder.setView(mView);
                                                            final AlertDialog dialog = mBuilder.create();
                                                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                            dialog.show();

                                                            final Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    dialog.dismiss();

                                                                }
                                                            }, 3000);

                                                            break;

                                                        }
                                                        case 5: {
                                                            int location[] = new int[2];
                                                            end_game_button.getLocationInWindow(location);

                                                            FrameLayout root = (FrameLayout)findViewById(R.id.my_frame_layout);
                                                            handpointerend = new ImageView(getApplicationContext());
                                                            handpointerend.setBackgroundResource(R.drawable.handpointer);

                                                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, 500);
                                                            params.leftMargin = location[0]+100;
                                                            params.topMargin  = location[1]-50;
                                                            root.addView(handpointerend, params);


                                                            valueAnimator = ValueAnimator.ofFloat(50f, -50f);

                                                            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
                                                            valueAnimator.setDuration(800);
                                                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                                @Override
                                                                public void onAnimationUpdate(ValueAnimator animation) {
                                                                    float progress = (float) animation.getAnimatedValue();
                                                                    handpointerend.setTranslationY(progress);
                                                                    handpointerend.setTranslationX(progress);
                                                                    // no need to use invalidate() as it is already present in             //the text view.
                                                                }
                                                            });
                                                            valueAnimator.setRepeatCount(1);
                                                            valueAnimator.start();
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
                                        } else {

                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1 = findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;
                                            wrongTaps++;

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

                if (isToggleChecked) {
                    switch (promptTechnique) {
                        case 4: {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                            }

                            if (clickNumber == 0) {
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taptofindsametile);
                                mediaPlayer.start();
                                clickNumber++;
                            }

                            break;
                        }
                        case 3: {

                            if (clickNumber == 0) {
                                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                View mView = getLayoutInflater().inflate(R.layout.instruction_two, null);

                                mBuilder.setView(mView);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.show();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();

                                    }
                                }, 3000);

                                clickNumber++;
                            }
                            break;
                        }
                        case 5: {
                            handpointer.setVisibility(View.GONE);
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
                                        if (onclick_previous == 0) {
                                            //dont change the image on the button
                                        } else if (onclick_current == onclick_previous && previous_id != current_id) {
                                            //dont change image but increment score count
                                            scoreCount++;
                                            if (scoreCount == 1 && clickNumber == 1) {
                                                switch (promptTechnique) {
                                                    case 4: {
                                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findothermatchingtiles);
                                                        mediaPlayer.start();
                                                        clickNumber++;
                                                        break;
                                                    }
                                                    case 3: {

                                                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                        View mView = getLayoutInflater().inflate(R.layout.instruction_three, null);

                                                        mBuilder.setView(mView);
                                                        final AlertDialog dialog = mBuilder.create();
                                                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                        dialog.show();

                                                        final Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dialog.dismiss();

                                                            }
                                                        }, 3000);

                                                        clickNumber++;
                                                        break;

                                                    }
                                                }
                                            } else if (scoreCount == 3) {
                                                timer.stop();
                                                isTimerRunning = false;
                                                elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();
                                                ;
                                                minutes = (elapsedTime / 1000) / 60;
                                                seconds = (elapsedTime / 1000) % 60;

                                                if (isToggleChecked) {
                                                    switch (promptTechnique) {
                                                        case 4: {
                                                            if (mediaPlayer.isPlaying()) {
                                                                mediaPlayer.stop();
                                                            }
                                                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapendgamebutton);
                                                            mediaPlayer.start();
                                                            break;
                                                        }
                                                        case 3: {

                                                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TileGame.this);
                                                            View mView = getLayoutInflater().inflate(R.layout.instruction_four, null);

                                                            mBuilder.setView(mView);
                                                            final AlertDialog dialog = mBuilder.create();
                                                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                            dialog.show();

                                                            final Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    dialog.dismiss();

                                                                }
                                                            }, 3000);

                                                            break;

                                                        }
                                                        case 5: {
                                                            int location[] = new int[2];
                                                            end_game_button.getLocationInWindow(location);

                                                            FrameLayout root = (FrameLayout)findViewById(R.id.my_frame_layout);
                                                            handpointerend = new ImageView(getApplicationContext());
                                                            handpointerend.setBackgroundResource(R.drawable.handpointer);

                                                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, 500);
                                                            params.leftMargin = location[0]+100;
                                                            params.topMargin  = location[1]-50;
                                                            root.addView(handpointerend, params);


                                                            valueAnimator = ValueAnimator.ofFloat(50f, -50f);

                                                            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
                                                            valueAnimator.setDuration(800);
                                                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                                @Override
                                                                public void onAnimationUpdate(ValueAnimator animation) {
                                                                    float progress = (float) animation.getAnimatedValue();
                                                                    handpointerend.setTranslationY(progress);
                                                                    handpointerend.setTranslationX(progress);
                                                                    // no need to use invalidate() as it is already present in             //the text view.
                                                                }
                                                            });
                                                            valueAnimator.setRepeatCount(1);
                                                            valueAnimator.start();
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

                                        } else {
                                            onclick_current = 0;
                                            onclick_previous = 0;
                                            ImageButton justlike1 = findViewById(previous_id);
                                            justlike1.setImageResource(R.drawable.baby_tile);
                                            ImageButton justlike2 = findViewById(current_id);
                                            justlike2.setImageResource(R.drawable.baby_tile);
                                            current_id = 0;
                                            previous_id = 0;
                                            wrongTaps++;

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

    //to check if app is on foreground
    public boolean foregrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE);
    }

    @Override
    protected void onResume() {
        //starts music when app returns to foreground
        startService(new Intent(TileGame.this, SoundService.class));
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (!this.isFinishing()) {
            //stops service and bg music
            stopService(new Intent(TileGame.this, SoundService.class));
            if (mediaPlayer != null) mediaPlayer.release();

        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //stop service and stop music
        if (!foregrounded()) {
            stopService(new Intent(TileGame.this, SoundService.class));
            if (mediaPlayer != null) mediaPlayer.release();
        }
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();

    }


}
