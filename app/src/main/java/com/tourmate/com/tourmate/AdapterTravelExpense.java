package com.tourmate.com.tourmate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTravelExpense extends RecyclerView.Adapter<AdapterTravelExpense.MyViewHolder> {
    private ArrayList<TravelExpense> expenseList = new ArrayList<>();
    Context ctx;

    public AdapterTravelExpense(ArrayList<TravelExpense> expenseList, Context ctx) {
        this.expenseList = expenseList;
        this.ctx = ctx;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mExpenseDateAndTime, mExpenseDetails, mExpenseAmount;
        ArrayList<TravelExpense> eventList = new ArrayList<>();
        Context ctx;
        public MyViewHolder(View itemView, Context ctx, ArrayList<TravelExpense> expenseList) {
            super(itemView);
            this.ctx = ctx;
            this.eventList = expenseList;
            this.mExpenseDateAndTime = itemView.findViewById(R.id.tv_expense_date_and_time);
            this.mExpenseDetails = itemView.findViewById(R.id.tv_expense_details);
            this.mExpenseAmount = itemView.findViewById(R.id.tv_expense_amount);

        }


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_expense_card_view_layout, parent, false);
        return new MyViewHolder(view,ctx,expenseList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TravelExpense travelExpense = expenseList.get(position);
        holder.mExpenseDateAndTime.setText(travelExpense.getExpenseDateAndTime());
        holder.mExpenseDetails.setText(travelExpense.getExpenseDetails());
        holder.mExpenseAmount.setText(String.valueOf(travelExpense.getExpenseAmount()) + " ৳");


    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }


}
