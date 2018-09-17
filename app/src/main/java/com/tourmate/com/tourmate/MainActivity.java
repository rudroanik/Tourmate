package com.tourmate.com.tourmate;

import android.app.Fragment;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomnavigation);


        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_contineer, homeFragment);
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem item ) {
                android.support.v4.app.Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = HomeFragment.newInstance();
                        break;
                    case R.id.travellist:
                        Intent intent = new Intent(MainActivity.this,ViewEventActivity.class);
                        startActivity(intent);

                        //selectedFragment = EventlistFragment.newInstance();
                        break;
                    case R.id.person:
                        selectedFragment = ProfileFragment.newInstance();
                        break;
                    default:

                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame_contineer, selectedFragment);
                transaction.commit();
                return false;
            }
        });

    }


}
