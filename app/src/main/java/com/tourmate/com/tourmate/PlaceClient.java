package com.tourmate.com.tourmate;

import com.tourmate.com.tourmate.Place.PlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface PlaceClient {
    @GET
    Call<PlaceResponse> getPlace( @Url String url);
}
