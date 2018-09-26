package com.tourmate.com.tourmate;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddMomentsActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUERST = 1;
    private static final int TAKE_PICTURE_REQUERST = 2;

    private Button mChooseImage, mTakePicture, mUploadImage;
    private ImageView mMomentImage;
    private EditText mMomentDetails;
    private ProgressBar mProgressBar;

    private Uri mImageUri;
    private StorageTask uploadTask;
    private String event_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_moments);

        mChooseImage = findViewById(R.id.btn_choose_image);
        mTakePicture = findViewById(R.id.btn_take_picture);
        mUploadImage = findViewById(R.id.btn_upload_image);
        mMomentImage = findViewById(R.id.iv_moment_image);
        mMomentDetails = findViewById(R.id.et_moment_details);
        mProgressBar = findViewById(R.id.pb_moment_progress_bar);

        event_id = getIntent().getStringExtra("event_id");

        getCameraPermission();

        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(AddMomentsActivity.this, " Upload is in progress", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadFile();
                }
            }
        });

    }

    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUERST);
    }

    public void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_PICTURE_REQUERST);
        }
        //startActivityForResult(intent, TAKE_PICTURE_REQUERST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUERST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(mMomentImage);
        }
        else if (requestCode == TAKE_PICTURE_REQUERST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(mMomentImage);
        }
    }

    public String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadFile(){
        if (mImageUri != null){
            DBHelper.TRAVEL_MOMENTS_REF.keepSynced(true);
            StorageReference fileReference = DBHelper.TRAVEL_MOMENTS_STORAGE_REF.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            uploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //mProgressBar.setProgress(0);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(AddMomentsActivity.this, "Successfully Uploaded Your Moment", Toast.LENGTH_SHORT).show();
                            String moment_id = DBHelper.TRAVEL_MOMENTS_REF.push().getKey();
                            String moment_details = mMomentDetails.getText().toString();
                            String image_url = taskSnapshot.getDownloadUrl().toString();
                            TravelMoment travelMoment = new TravelMoment(moment_id, moment_details, image_url, event_id);
                            DBHelper.TRAVEL_MOMENTS_REF.child(moment_id).setValue(travelMoment);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddMomentsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int)progress);
                        }
                    });
        }
        else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCameraPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, TAKE_PICTURE_REQUERST);
            }
        }
    }
}
