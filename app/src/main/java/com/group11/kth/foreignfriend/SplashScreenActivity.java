package com.group11.kth.foreignfriend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // create a thread for the splash screen
        Thread splashScreen = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(4000);
                    Intent intent = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        splashScreen.start();

    }
}
