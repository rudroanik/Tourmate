package com.tourmate.com.tourmate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class EventlistFragment extends Fragment {


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eventlist, container, false);
    }

    public static Fragment newInstance() {

        EventlistFragment eventlistFragment = new EventlistFragment();
        return  eventlistFragment;
    }
}
