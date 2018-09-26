package com.tourmate.com.tourmate;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "";
    public double lat,log;
    private WeatherClient weatherClient;
    private String units = "metric";
    private int temMin, temMax;
    private String mainweather;

    private TextView maxTemp, mWeather;

    private static String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!= null){
            lat = location.getLatitude();
            log = location.getLongitude();
        }
        maxTemp = findViewById(R.id.maxtemp);
        mWeather = findViewById(R.id.weather);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        weatherClient = retrofit.create(WeatherClient.class);
        String url = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s",lat,log,units,getString(R.string.weather_api_key));
        Call<WeatherResponse> weatherResponseCall = weatherClient.getWeathe(url);
        weatherResponseCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse( Call<WeatherResponse> call, Response<WeatherResponse> response ) {
                if (response.code() == 200) {

                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        temMax = weatherResponse.getMain().getTempMax();
                        Toast.makeText(WeatherActivity.this, String.valueOf(temMax), Toast.LENGTH_SHORT).show();
                        maxTemp.setText(String.valueOf(temMax));
                        mainweather = weatherResponse.getWeather().get(0).getDescription();
                        mWeather.setText(mainweather);
                    }


                }
            }

            @Override
            public void onFailure( Call<WeatherResponse> call, Throwable t ) {

            }
        });

    }

    private void init() {

    }


}
