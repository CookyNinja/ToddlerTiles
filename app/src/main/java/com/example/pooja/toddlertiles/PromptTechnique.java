package com.example.pooja.toddlertiles;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class PromptTechnique extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_prompt_technique);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#f47485"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

    }

    public void pressButton(View view){
        view.setBackgroundColor(Color.rgb(255, 201, 216));
        Intent intent = new Intent(this, TileGame.class);
        startActivity(intent);
    }
}
