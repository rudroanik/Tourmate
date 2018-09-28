package com.tourmate.com.tourmate;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.tourmate.com.tourmate.Weather.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "";
    public double log, lat;
    private String city, cityName, country;
    private WeatherClient weatherClient;
    private String units = "metric";
    private int temMin, temMax;
    private String mainweather;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private TextView maxTemp, mWeather, mlocation, count;
    private EditText mCityName;
    private Button mSearch;

    private static String BASE_URL = "https://api.openweathermap.org";


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        maxTemp = findViewById(R.id.maxtemp);
        mWeather = findViewById(R.id.weather);
        mCityName = findViewById(R.id.cityname);
        mSearch = findViewById(R.id.search);
        mlocation = findViewById(R.id.location);
        count = findViewById(R.id.country);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        weatherClient = retrofit.create(WeatherClient.class);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                city=mCityName.getText().toString()+",bd";

                String url = String.format("data/2.5/weather?q=%s&units=%s&appid=%s",city,units,getString(R.string.weather_api_key));
                Call<WeatherResponse> weatherResponseCall = weatherClient.getWeathe(url);
                weatherResponseCall.enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse( Call<WeatherResponse> call, Response<WeatherResponse> response ) {
                        if (response.code() == 200) {

                            WeatherResponse weatherResponse = response.body();
                            if (weatherResponse != null) {
                                temMax = weatherResponse.getMain().getTempMax();
                                maxTemp.setText(String.valueOf(temMax));
                                cityName = weatherResponse.getName();
                                country = weatherResponse.getSys().getCountry();
                                count.setText(country);
                                mlocation.setText(cityName);
                                mainweather = weatherResponse.getWeather().get(0).getMain();
                                mWeather.setText(mainweather);
                            }


                        }
                    }

                    @Override
                    public void onFailure( Call<WeatherResponse> call, Throwable t ) {

                    }
                });
            }
        });





    }

}
