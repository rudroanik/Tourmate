package com.tourmate.com.tourmate;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyFireApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
