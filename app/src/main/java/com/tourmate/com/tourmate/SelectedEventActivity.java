package com.tourmate.com.tourmate;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SelectedEventActivity extends AppCompatActivity {
    private TextView mDestination, mBudget, mFromDate, mToDate, mTotalExpense;
    private ImageView mEditButton, mAddExpenseButton;
    private Button mViewExpenseHistory;
    private String event_id;
    private double totalExpense;
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

        event_id = getIntent().getStringExtra("event_id");
        mDestination.setText(getIntent().getStringExtra("event_destination"));
        mBudget.setText(String.valueOf(getIntent().getDoubleExtra("event_budget",0.0)) + " ৳");
        mFromDate.setText(getIntent().getStringExtra("event_from_date"));
        mToDate.setText(getIntent().getStringExtra("event_to_date"));

        Query query = DBHelper.TRAVEL_EXPENSES_REF.orderByChild("eventID").equalTo(event_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalExpense = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    TravelExpense travelExpense = ds.getValue(TravelExpense.class);
                    totalExpense+=travelExpense.getExpenseAmount();
                }
                mTotalExpense.setText(String.valueOf(totalExpense));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectedEventActivity.this, "Click on Edit Button", Toast.LENGTH_SHORT).show();
            }
        });



        mAddExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedEventActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.entry_expense_layout,null);
                dialogBuilder.setView(dialogView);
                final EditText mExpenseDetails = dialogView.findViewById(R.id.et_expense_details);
                final EditText mExpenseAmount = dialogView.findViewById(R.id.et_expense_amount);
                final Button mEntryExpense = dialogView.findViewById(R.id.btn_entry_expense);


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
                            TravelExpense travelExpense = new TravelExpense(id,details,formattedDate,amount,event_id);
                            DBHelper.TRAVEL_EXPENSES_REF.child(id).setValue(travelExpense);
                            Toast.makeText(SelectedEventActivity.this, "Travel Expense Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogBuilder.setTitle("Entry Expense");
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }

        });

        mViewExpenseHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
