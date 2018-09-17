package com.tourmate.com.tourmate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {
    EditText mDestination, mBudget, mFromDate, mToDate;
    Button mSaveEvent, mFromDateButton, mToDateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        mDestination = findViewById(R.id.et_travel_destination);
        mBudget = findViewById(R.id.et_estimated_budget);
        mFromDate = findViewById(R.id.et_from_date);
        mToDate = findViewById(R.id.et_to_date);
        mSaveEvent = findViewById(R.id.btnEventSave);
        mFromDateButton = findViewById(R.id.btn_from_date);
        mToDateButton = findViewById(R.id.btn_to_date);

        mFromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFromDate();

            }
        });
        mToDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseToDate();
            }
        });

        mSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseTravelEvents;
                databaseTravelEvents = FirebaseDatabase.getInstance().getReference("travel_events");

                String destination = mDestination.getText().toString().trim();
                String budget = mBudget.getText().toString().trim();
                String fromDate = mFromDate.getText().toString().trim();
                String toDate = mToDate.getText().toString().trim();

                if (TextUtils.isEmpty(destination)){
                    mDestination.setError("Please Provide Your Travel Destination");
                    mDestination.requestFocus();
                }
                else if (TextUtils.isEmpty(budget)){
                    mBudget.setError("Please Provide Your Estimated Budget");
                    mBudget.requestFocus();
                }
                else if (TextUtils.isEmpty(fromDate)){
                    Toast.makeText(AddEventActivity.this, "Please Select Your Event Start Date", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(toDate)){
                    Toast.makeText(AddEventActivity.this, "Please Select Your Event Close Date", Toast.LENGTH_SHORT).show();
                }
                else{
                    String id = databaseTravelEvents.push().getKey();
                    TravelEvent travelEvent = new TravelEvent(id,destination,budget,fromDate,toDate);
                    databaseTravelEvents.child(id).setValue(travelEvent);
                    Toast.makeText(AddEventActivity.this, "Travel Event Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void chooseFromDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker view, final int year, final int month,
                                          final int dayOfMonth) {

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        calendar.set(year, month, dayOfMonth);
                        String dateString = sdf.format(calendar.getTime());

                        mFromDate.setText(dateString); // set the date
                    }
                }, year, month, day); // set date picker to current date

        datePicker.show();

        datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(final DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }
    private void chooseToDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker view, final int year, final int month,
                                          final int dayOfMonth) {

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        calendar.set(year, month, dayOfMonth);
                        String dateString = sdf.format(calendar.getTime());

                        mToDate.setText(dateString); // set the date
                    }
                }, year, month, day); // set date picker to current date

        datePicker.show();

        datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(final DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }
}
