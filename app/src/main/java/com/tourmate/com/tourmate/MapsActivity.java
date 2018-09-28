package com.tourmate.com.tourmate;


import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tourmate.com.tourmate.Place.PlaceResponse;
import com.tourmate.com.tourmate.Place.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private PlaceClient placeClient;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private static double latitide, longitude;
    private int ProximityRadius = 10000;
    private ImageView mbank,mhotel,mresturent,matm;

    private static String BASE_URL = "https://maps.googleapis.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        placeClient = retrofit.create(PlaceClient.class);


        mbank= findViewById(R.id.bank);
        matm= findViewById(R.id.atm);
        mresturent= findViewById(R.id.food);
        mhotel= findViewById(R.id.hotel);

        mbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Toast.makeText(MapsActivity.this, "Bank is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        mhotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Toast.makeText(MapsActivity.this, "Hotel is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        matm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Toast.makeText(MapsActivity.this, "ATM is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        mresturent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                String url = String.format("/maps/api/place/nearbysearch/json?location=%f,%f&radius=500&type=restaurant&keyword=%s", latitide, longitude, getString(R.string.pplace_api_key));
                Call<PlaceResponse> placeResponseCall = placeClient.getPlace(url);
                placeResponseCall.enqueue(new Callback<PlaceResponse>() {
                    @Override
                    public void onResponse( Call<PlaceResponse> call, Response<PlaceResponse> response ) {
                        if (response.code() == 200) {
                            PlaceResponse placeResponseCall = response.body();
                            List<Result> results = placeResponseCall.getResults();
                            for (int i = 0; i < results.size(); i++) {
                                double lat = results.get(i).getGeometry().getLocation().getLat();
                                double lon = results.get(i).getGeometry().getLocation().getLng();
                                LatLng restaurant = new LatLng(latitide, longitude);
                                mMap.addMarker(new MarkerOptions().position(restaurant));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurant,14));
                            }
                        }
                    }

                    @Override
                    public void onFailure( Call<PlaceResponse> call, Throwable t ) {

                    }
                });

            }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }

    }


    public boolean checkUserLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this,  android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,  android.Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.ACCESS_COARSE_LOCATION}, Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.ACCESS_COARSE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        }
        else
        {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this,  android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }




    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location)
    {
        latitide = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;

        if (currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("My Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,  android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


