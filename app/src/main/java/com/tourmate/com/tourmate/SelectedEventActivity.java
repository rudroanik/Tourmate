package com.tourmate.com.tourmate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SelectedEventActivity extends AppCompatActivity {
    private TextView mDestination, mBudget, mFromDate, mToDate, mTotalExpense;
    private ImageView mEditButton, mAddExpenseButton;
    private Button mViewExpenseHistory, mAddMoments, mMomentsGallery;
    private String event_id, destination, from_date, to_date;
    private double budget;
    private double totalExpense;
    RecyclerView.Adapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_event);
        mDestination = findViewById(R.id.tv_s_destination);
        mBudget = findViewById(R.id.tv_s_estimated_budget);
        mFromDate = findViewById(R.id.tv_s_from_date);
        mToDate = findViewById(R.id.tv_s_to_date);

        mEditButton = findViewById(R.id.iv_edit_button);

        mTotalExpense = findViewById(R.id.tv_s_total_expense);
        mAddExpenseButton = findViewById(R.id.iv_add_expense_button);
        mViewExpenseHistory = findViewById(R.id.btn_view_expense_history);

        mAddMoments = findViewById(R.id.btn_add_moments);
        mMomentsGallery = findViewById(R.id.btn_moments_gallery);

        //get values from extras
        event_id = getIntent().getStringExtra("event_id");
        destination = getIntent().getStringExtra("event_destination");
        from_date = getIntent().getStringExtra("event_from_date");
        to_date = getIntent().getStringExtra("event_to_date");
        budget = getIntent().getDoubleExtra("event_budget",0.0);

        //set event info in the layout
        mDestination.setText(destination);
        mBudget.setText(String.valueOf(budget) + " ৳");
        mFromDate.setText(from_date);
        mToDate.setText(to_date);

        //query for getting total expense
        Query query = DBHelper.TRAVEL_EXPENSES_REF.orderByChild("eventID").equalTo(event_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalExpense = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    TravelExpense travelExpense = ds.getValue(TravelExpense.class);
                    totalExpense+=travelExpense.getExpenseAmount();
                }
                mTotalExpense.setText(String.valueOf(totalExpense)+" ৳");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SelectedEventActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedEventActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.update_event_layout,null);
                dialogBuilder.setView(dialogView);
                final EditText mDestinationUpdate, mBudgetUpdate, mFromDateUpdate, mToDateUpdate;
                final Button mUpdateEventButton,mFromDateButton, mToDateButton;
                mDestinationUpdate = dialogView.findViewById(R.id.et_update_travel_destination);
                mBudgetUpdate = dialogView.findViewById(R.id.et_update_estimated_budget);
                mFromDateUpdate = dialogView.findViewById(R.id.et_update_from_date);
                mToDateUpdate = dialogView.findViewById(R.id.et_update_to_date);
                mFromDateButton = dialogView.findViewById(R.id.btn_update_from_date);
                mToDateButton = dialogView.findViewById(R.id.btn_update_to_date);
                mUpdateEventButton = dialogView.findViewById(R.id.btn_update_Event);

                dialogBuilder.setTitle("Updating Travel Event");
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                mDestinationUpdate.setText(destination);
                mBudgetUpdate.setText(String.valueOf(budget));
                mFromDateUpdate.setText(from_date);
                mToDateUpdate.setText(to_date);

                mFromDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseDate(mFromDateUpdate);
                    }
                });

                mToDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseDate(mToDateUpdate);
                    }
                });

                mUpdateEventButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(mDestinationUpdate.getText().toString().trim())){
                            mDestinationUpdate.setError("Please Provide Your Travel Destination");
                            mDestinationUpdate.requestFocus();
                        }
                        else if (TextUtils.isEmpty(mBudgetUpdate.getText().toString().trim())){
                            mBudgetUpdate.setError("Please Provide Your Estimated Budget");
                            mBudgetUpdate.requestFocus();
                        }
                        else if (TextUtils.isEmpty(mFromDateUpdate.getText().toString().trim())){
                            Toast.makeText(SelectedEventActivity.this, "Please Select From Date For Your Travel Event", Toast.LENGTH_SHORT).show();
                            mFromDateUpdate.requestFocus();
                        }
                        else if (TextUtils.isEmpty(mToDateUpdate.getText().toString().trim())){
                            Toast.makeText(SelectedEventActivity.this, "Please Select To Date For Your Travel Event", Toast.LENGTH_SHORT).show();
                            mToDateUpdate.requestFocus();
                        }
                        else{
                            DBHelper.TRAVEL_EVENTS_REF.keepSynced(true);
                            String updated_destination = mDestinationUpdate.getText().toString().trim();
                            double updated_budget = Double.parseDouble(mBudgetUpdate.getText().toString().trim());
                            String updated_from_date = mFromDateUpdate.getText().toString().trim();
                            String updated_to_date = mToDateUpdate.getText().toString().trim();

                            TravelEvent travelEvent = new TravelEvent(event_id,updated_destination,updated_budget,updated_from_date,updated_to_date, DBHelper.FIREBASE_USER.getUid());
                            DBHelper.TRAVEL_EVENTS_REF.child(event_id).setValue(travelEvent);
                            Toast.makeText(SelectedEventActivity.this, "Travel Expense Updated Successfully", Toast.LENGTH_SHORT).show();
                            mDestination.setText(updated_destination);
                            mBudget.setText(String.valueOf(updated_budget) + " ৳");
                            mFromDate.setText(updated_from_date);
                            mToDate.setText(updated_to_date);
                            alertDialog.dismiss();

                        }
                    }
                });

            }
        });



        mAddExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense(event_id);

            }

        });

        mViewExpenseHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewExpenseHistory(event_id);
            }
        });

        mAddMoments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedEventActivity.this,AddMomentsActivity.class);
                intent.putExtra("event_id",event_id);
                startActivity(intent);
            }
        });

        mMomentsGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedEventActivity.this,MomentsGalleryActivity.class);
                intent.putExtra("event_id",event_id);
                startActivity(intent);
            }
        });
    }

    public void viewExpenseHistory(String eventID){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedEventActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.view_expense_layout,null);
        dialogBuilder.setView(dialogView);
        final RecyclerView mViewExpense = dialogView.findViewById(R.id.rv_view_expense);
        mViewExpense.setHasFixedSize(true);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SelectedEventActivity.this);
        mViewExpense.setLayoutManager(mLayoutManager);
        mViewExpense.setItemAnimator(new DefaultItemAnimator());
        final ArrayList<TravelExpense> travelExpenses = new ArrayList<>();
        Query query = DBHelper.TRAVEL_EXPENSES_REF.orderByChild("eventID").equalTo(eventID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                travelExpenses.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    TravelExpense travelExpense = ds.getValue(TravelExpense.class);
                    travelExpenses.add(travelExpense);
                }
                mAdapter = new AdapterTravelExpense(travelExpenses,SelectedEventActivity.this);
                mViewExpense.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialogBuilder.setTitle("Expense History");
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void addExpense(final String eventID){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedEventActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.entry_expense_layout,null);
        dialogBuilder.setView(dialogView);
        final EditText mExpenseDetails = dialogView.findViewById(R.id.et_expense_details);
        final EditText mExpenseAmount = dialogView.findViewById(R.id.et_expense_amount);
        final Button mEntryExpense = dialogView.findViewById(R.id.btn_entry_expense);

        dialogBuilder.setTitle("Entry Expense");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        mEntryExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mExpenseDetails.getText().toString().trim())){
                    mExpenseDetails.setError("Please Provide Your Travel Destination");
                    mExpenseDetails.requestFocus();
                }
                else if (TextUtils.isEmpty(mExpenseAmount.getText().toString().trim())){
                    mExpenseAmount.setError("Please Provide Your Estimated Budget");
                    mExpenseAmount.requestFocus();
                }
                else{
                    DBHelper.TRAVEL_EXPENSES_REF.keepSynced(true);
                    String details = mExpenseDetails.getText().toString().trim();
                    double amount = Double.parseDouble(mExpenseAmount.getText().toString().trim());
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    String id = DBHelper.TRAVEL_EXPENSES_REF.push().getKey();
                    TravelExpense travelExpense = new TravelExpense(id,details,formattedDate,amount,eventID);
                    DBHelper.TRAVEL_EXPENSES_REF.child(id).setValue(travelExpense);
                    Toast.makeText(SelectedEventActivity.this, "Travel Expense Added Successfully", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void chooseDate(final EditText date) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker =
                new DatePickerDialog(SelectedEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker view, final int year, final int month,
                                          final int dayOfMonth) {

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        calendar.set(year, month, dayOfMonth);
                        String dateString = sdf.format(calendar.getTime());

                        date.setText(dateString); // set the date
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
