package com.tourmate.com.tourmate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterMoments extends RecyclerView.Adapter<AdapterMoments.MomentViewHolder> {
    private Context ctx;
    private List<TravelMoment> momentList;
    private OnItemClickListener mListener;

    public AdapterMoments(Context ctx, List<TravelMoment> momentList) {
        this.ctx = ctx;
        this.momentList = momentList;
    }

    @NonNull
    @Override
    public MomentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.moments_item_layout,parent,false);

        return new MomentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MomentViewHolder holder, int position) {
        TravelMoment travelMoment = momentList.get(position);
        holder.mMomentDetails.setText(travelMoment.getMomentDetails());
        Picasso.with(ctx)
                .load(travelMoment.getImageUrl())
                .placeholder(R.drawable.image_container)
                .fit().centerCrop()
                .into(holder.mMomentImage);
    }

    @Override
    public int getItemCount() {
        return momentList.size();
    }

    public class MomentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        public TextView mMomentDetails;
        public ImageView mMomentImage;
        public MomentViewHolder(View itemView) {
            super(itemView);
            mMomentDetails = itemView.findViewById(R.id.tv_item_moment_details);
            mMomentImage = itemView.findViewById(R.id.iv_item_moment_image);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.OnItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem edit = menu.add(Menu.NONE,1,1,"Edit");
            MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");

            edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.OnEditClick(position);
                            return true;
                        case 2:
                            mListener.OnDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
        void OnEditClick(int position);
        void OnDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
