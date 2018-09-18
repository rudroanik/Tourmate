package com.tourmate.com.tourmate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentCreateEvent extends Fragment {
    private EditText mDestination, mBudget, mFromDate, mToDate;
    private Button mSaveEvent, mFromDateButton, mToDateButton;
//    private DatabaseReference rootRef;
//    private DatabaseReference userRef;
//    private DatabaseReference databaseTravelEvents;
//    private FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_event_layout,container,false);
        mDestination = v.findViewById(R.id.et_travel_destination);
        mBudget = v.findViewById(R.id.et_estimated_budget);
        mFromDate = v.findViewById(R.id.et_from_date);
        mToDate = v.findViewById(R.id.et_to_date);
        //mSaveEvent = v.findViewById(R.id.btnEventSave);
        mFromDateButton = v.findViewById(R.id.btn_from_date);
        mToDateButton = v.findViewById(R.id.btn_to_date);



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

//        mSaveEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveEvent();
//
//
//            }
//        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create New Event");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.btn_save_menu,menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_save_data){
            saveEvent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveEvent(){


        if (TextUtils.isEmpty(mDestination.getText().toString().trim())){
            mDestination.setError("Please Provide Your Travel Destination");
            mDestination.requestFocus();
        }
        else if (TextUtils.isEmpty(mBudget.getText().toString().trim())){
            mBudget.setError("Please Provide Your Estimated Budget");
            mBudget.requestFocus();
        }
        else if (TextUtils.isEmpty(mFromDate.getText().toString().trim())){
            Toast.makeText(getActivity(), "Please Select Your Event Start Date", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(mToDate.getText().toString().trim())){
            Toast.makeText(getActivity(), "Please Select Your Event Close Date", Toast.LENGTH_SHORT).show();
        }
        else{
            String destination = mDestination.getText().toString().trim();
            double budget = Double.parseDouble(mBudget.getText().toString().trim());
            String fromDate = mFromDate.getText().toString().trim();
            String toDate = mToDate.getText().toString().trim();
            String id = DBHelper.TRAVEL_EVENTS_REF.push().getKey();
            TravelEvent travelEvent = new TravelEvent(id,destination,budget,fromDate,toDate);
            DBHelper.TRAVEL_EVENTS_REF.child(id).setValue(travelEvent);
            Toast.makeText(getActivity(), "Travel Event Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseFromDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker =
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
