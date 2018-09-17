package com.tourmate.com.tourmate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class HomeFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    ImageView mCreateEvent, mlocation_imageview;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        mCreateEvent = v.findViewById((R.id.event_imageview));
        mlocation_imageview = v.findViewById(R.id.location_imageview);

        mlocation_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent(getActivity(),MapsActivity.class);
                startActivity(intent);
            }
        });

        mCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddEventActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    public static Fragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }
}
