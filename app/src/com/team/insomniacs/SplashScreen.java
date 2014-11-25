package com.team.insomniacs;

import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.splash_sound);
        //mediaPlayer.start();

        new Handler().postDelayed(new Runnable() {

	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, NListView.class);
                startActivity(i);
                //mediaPlayer.release();

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}
