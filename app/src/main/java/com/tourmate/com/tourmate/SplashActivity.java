package com.tourmate.com.tourmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressbar);
        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }

    private void doWork() {
        for (int progress=0; progress<100; progress+=10) {
            try {
                Thread.sleep(500);
                progressBar.setProgress(progress);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    private void startApp() {
        Intent intent;
        if (DBHelper.FIREBASE_USER != null){
            intent = new Intent(SplashActivity.this,MainActivity.class);
        }
        else {
            intent = new Intent(SplashActivity.this, WelcomeLogInActivity.class);
        }
        startActivity(intent);
    }
}

