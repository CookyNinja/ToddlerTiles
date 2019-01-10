package com.example.pooja.toddlertiles;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class PromptTechnique extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //private int promptTechnique;
    private Button b1, b2, b3, b4, b5;
    private String AgeCategory = "", Gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_prompt_technique);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#f47485"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Chandelle Display Demo.ttf");

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);

        b1.setTypeface(face);
        b2.setTypeface(face);
        b3.setTypeface(face);
        b4.setTypeface(face);
        b5.setTypeface(face);


// Male Female dialog

        final String[] choice = {"Male", "Female"};

        AlertDialog.Builder builder = new AlertDialog.Builder(PromptTechnique.this);
        builder.setTitle("Male or Female?");
        builder.setSingleChoiceItems(choice, -1, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on choice[which]
                if("Male".equals(choice[which])){
                    Gender = "Male";
                    editor.putString("Gender" , Gender);
                    editor.commit();
                    dialog.dismiss();

                }
                else if("Female".equals(choice[which])){
                    Gender = "Female";
                    editor.putString("Gender" , Gender);
                    editor.commit();
                    dialog.dismiss();

                }
            }

        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();


        //Age category dialog

        final String[] choice2 = {"4 to less than 7", "7 to less than 9"};

        AlertDialog.Builder builder2 = new AlertDialog.Builder(PromptTechnique.this);
        builder2.setTitle("Age Category: ");
        builder2.setSingleChoiceItems(choice2, -1, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on choice[which]
                if("4 to less than 7".equals(choice2[which])){
                    AgeCategory = "4 to <7";
                    editor.putString("AgeCategory" , AgeCategory);
                    editor.commit();
                    dialog.dismiss();

                }
                else if("7 to less than 9".equals(choice2[which])){
                    AgeCategory = "7 to <9";
                    editor.putString("AgeCategory" , AgeCategory);
                    editor.commit();
                    dialog.dismiss();

                }
            }

        });

        builder2.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog2 = builder2.create();
        mDialog2.show();



        //creating sharedpreference for PromptTechnique Variable
        sharedPreferences = getApplicationContext().getSharedPreferences("SharedPreference1" , MODE_PRIVATE);
        editor = sharedPreferences.edit();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("promptTechnique", 1); //saving 1 in promptTechnique
                editor.commit(); //saving changes to sharedPreference
                //promptTechnique = 1;
                pressButton(b1);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //promptTechnique = 2;
                editor.putInt("promptTechnique" , 2);
                editor.commit();
                pressButton(b2);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //promptTechnique = 3;
                editor.putInt("promptTechnique" , 3);
                editor.commit();
                pressButton(b3);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //promptTechnique = 4;
                editor.putInt("promptTechnique" , 4);
                editor.commit();
                pressButton(b4);
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //promptTechnique = 5;
                editor.putInt("promptTechnique" , 5);
                editor.commit();
                pressButton(b5);
            }
        });

    }

    public void pressButton(View view){
        //view.setBackgroundColor(Color.rgb(255, 201, 216));
        Intent intent = new Intent(this, TileGame.class);
        //if nothing stored, then returns default value 2, i.e. text instructions at the start
        //intent.putExtra("promptTechnique", sharedPreferences.getInt("promptTechnique" , 2));
        startActivity(intent);
        finish();
    }

    //to check if app is on foreground
    public boolean foregrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE);
    }

    @Override
    protected void onResume() {
        //starts the background music
        startService(new Intent(PromptTechnique.this, SoundService.class));
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (!this.isFinishing()){
            //stops the background music
            stopService(new Intent(PromptTechnique.this, SoundService.class));
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //stop service and stop music
        if(!foregrounded()){
            stopService(new Intent(PromptTechnique.this, SoundService.class));
        }
        super.onDestroy();
    }


}
