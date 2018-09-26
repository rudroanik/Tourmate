package com.tourmate.com.tourmate;

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
import android.widget.EditText;
import android.widget.Toast;

public class FragmentEntryExpense extends Fragment {
    private EditText mExpenseDetails, mExpenseAmount;
    String event_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.entry_expense_layout,container,false);
        mExpenseDetails = v.findViewById(R.id.et_expense_details);
        mExpenseAmount = v.findViewById(R.id.et_expense_amount);

        event_id = getActivity().getIntent().getStringExtra("event_id");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Entry Expense");
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
            addExpense();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addExpense(){


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

            String date_and_time = "bnfdhnsdfnksfdsadsdkf";
            String id = DBHelper.TRAVEL_EXPENSES_REF.push().getKey();
            TravelExpense travelExpense = new TravelExpense(id,details,date_and_time,amount,event_id);
            DBHelper.TRAVEL_EXPENSES_REF.child(id).setValue(travelExpense);
            Toast.makeText(getActivity(), "Travel Expense Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

}
