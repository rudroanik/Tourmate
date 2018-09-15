package com.tourmate.com.tourmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeLogInActivity extends AppCompatActivity {

    Button btnphonenumber,btnregistration;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_);

        btnphonenumber = findViewById(R.id.btn_signin);
        btnregistration = findViewById(R.id.btn_signup);


        btnphonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                Intent intent = new Intent(WelcomeLogInActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnregistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                Intent intent = new Intent(WelcomeLogInActivity.this,InformationActivity.class);
                startActivity(intent);

            }
        });

    }
}
