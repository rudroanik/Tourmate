package com.tourmate.com.tourmate;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;

public class NearbyPlaces extends AsyncTask<Object,String,String> {

    private String googlePlaceData,url;
    private GoogleMap mMap;
    @Override
    protected String doInBackground( Object... objects ) {
        return null;
    }

    @Override
    protected void onPostExecute( String s ) {
        super.onPostExecute(s);
    }
}
