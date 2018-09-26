package com.tourmate.com.tourmate;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MomentsGalleryActivity extends AppCompatActivity implements AdapterMoments.OnItemClickListener {
    private RecyclerView mMomentGalleryView;
    private AdapterMoments mAdapter;
    private List<TravelMoment> momentList;

    private String event_id;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private ValueEventListener mDBListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments_gallery);

        mProgressBar = findViewById(R.id.pb_circle_progressbar);
        mMomentGalleryView = findViewById(R.id.rv_moments_gallery);
        mMomentGalleryView.setHasFixedSize(true);
        mMomentGalleryView.setLayoutManager(new GridLayoutManager(this,2));
        mMomentGalleryView.setItemAnimator(new DefaultItemAnimator());


        momentList = new ArrayList<>();
        mAdapter = new AdapterMoments(MomentsGalleryActivity.this,momentList);
        mMomentGalleryView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(MomentsGalleryActivity.this);

        mStorage = FirebaseStorage.getInstance();
        event_id = getIntent().getStringExtra("event_id");
        Query query = DBHelper.TRAVEL_MOMENTS_REF.orderByChild("eventID").equalTo(event_id);
        mDBListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                momentList.clear();
                for (DataSnapshot momentData : dataSnapshot.getChildren()){
                    TravelMoment travelMoment = momentData.getValue(TravelMoment.class);
                    momentList.add(travelMoment);
                }
                mAdapter.notifyDataSetChanged();

                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MomentsGalleryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void OnEditClick(int position) {

    }

    @Override
    public void OnDeleteClick(int position) {
        TravelMoment travelMoment = momentList.get(position);
        final String moment_id = travelMoment.getMomentID();
        StorageReference imageRef = mStorage.getReferenceFromUrl(travelMoment.getImageUrl());
        imageRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DBHelper.TRAVEL_MOMENTS_REF.child(moment_id).removeValue();
                        Toast.makeText(MomentsGalleryActivity.this, "Image Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MomentsGalleryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBHelper.TRAVEL_MOMENTS_REF.removeEventListener(mDBListener);
    }
}
