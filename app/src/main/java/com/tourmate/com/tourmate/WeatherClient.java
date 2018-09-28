package com.tourmate.com.tourmate;


import com.tourmate.com.tourmate.Weather.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherClient {


    @GET
    Call<WeatherResponse> getWeathe( @Url String url);

}
