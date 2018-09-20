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
    private ArrayList<TravelEvent> eventList = new ArrayList<>();
    Context ctx;

    public AdapterTravelExpense(ArrayList<TravelEvent> eventList, Context ctx) {
        this.eventList = eventList;
        this.ctx = ctx;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mDestination, mBudget, mFromDate, mToDate;
        ArrayList<TravelEvent> eventList = new ArrayList<>();
        Context ctx;
        public MyViewHolder(View itemView, Context ctx, ArrayList<TravelEvent> eventList) {
            super(itemView);
            this.ctx = ctx;
            this.eventList = eventList;
            itemView.setOnClickListener(this);
            this.mDestination = itemView.findViewById(R.id.tv_destination);
            this.mBudget = itemView.findViewById(R.id.tv_estimated_budget);
            this.mFromDate = itemView.findViewById(R.id.tv_from_date);
            this.mToDate = itemView.findViewById(R.id.tv_to_date);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            TravelEvent travelEvent = this.eventList.get(position);
            Intent intent = new Intent(this.ctx,SelectedEventActivity.class);
            intent.putExtra("event_id",travelEvent.getEventID());
            intent.putExtra("event_destination",travelEvent.getDestination());
            intent.putExtra("event_budget",travelEvent.getEstimatedBudget());
            intent.putExtra("event_from_date",travelEvent.getFromDate());
            intent.putExtra("event_to_date",travelEvent.getToDate());
            this.ctx.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_event_card_view_layout, parent, false);
        return new MyViewHolder(view,ctx,eventList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TravelEvent travelEvent = eventList.get(position);
        holder.mDestination.setText(travelEvent.getDestination());
        holder.mBudget.setText("Estimated Budget: " + travelEvent.getEstimatedBudget() + " ৳");
        holder.mFromDate.setText(travelEvent.getFromDate());
        holder.mToDate.setText(travelEvent.getToDate());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


}
