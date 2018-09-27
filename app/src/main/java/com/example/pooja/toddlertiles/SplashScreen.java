package com.example.pooja.toddlertiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //to remove status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView Image_toddler_tiles = (ImageView) findViewById(R.id.splash_screen_image);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        Image_toddler_tiles.startAnimation(myanim);


        final Intent intent = new Intent(this, TileGame.class);
        Thread timer = new Thread(){

            public void run(){
                try {
                    sleep(2000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {

                    startActivity(intent);
                    finish();

                }
            }
        };

        timer.start();

    }
}
