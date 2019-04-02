package com.vit.gff.codebusters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Splash extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    Handler handler;
    SharedPreferences prefs;

    int flag=0;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();

        setContentView(R.layout.activity_splash);

        prefs = this.getSharedPreferences("setting",MODE_PRIVATE);

        if (prefs.getBoolean("login",false))
        {
            flag=1;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag==0)
                    startActivity(new Intent(Splash.this,LoginActivity.class ));
                else
                    startActivity(new Intent(Splash.this, MainActivity.class));

                finish();

            }
        },3000);

    }


}
