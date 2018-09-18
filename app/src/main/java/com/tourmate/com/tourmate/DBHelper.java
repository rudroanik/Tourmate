package com.tourmate.com.tourmate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBHelper {
    public static final FirebaseUser FIREBASE_USER = FirebaseAuth.getInstance().getCurrentUser();
    public static final DatabaseReference ROOT_REF = FirebaseDatabase.getInstance().getReference();
    public static final DatabaseReference USER_REF = ROOT_REF.child("users");
    public static final DatabaseReference TRAVEL_EVENTS_REF = USER_REF.child(FIREBASE_USER.getUid()).child("travel_events");

}
