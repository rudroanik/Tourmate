package com.tourmate.com.tourmate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DBHelper {
    public static final FirebaseUser FIREBASE_USER = FirebaseAuth.getInstance().getCurrentUser();
    public static final DatabaseReference ROOT_REF = FirebaseDatabase.getInstance().getReference();
    public static final StorageReference STORAGE_REF = FirebaseStorage.getInstance().getReference();
    public static final DatabaseReference USER_REF = ROOT_REF.child("users");
    public static final DatabaseReference TRAVEL_EVENTS_REF = ROOT_REF.child("travel_events");
    public static final DatabaseReference TRAVEL_EXPENSES_REF = ROOT_REF.child("travel_expenses");
    public static final DatabaseReference TRAVEL_MOMENTS_REF = ROOT_REF.child("travel_moments");
    public static final StorageReference TRAVEL_MOMENTS_STORAGE_REF = STORAGE_REF.child("travel_moments");


}
