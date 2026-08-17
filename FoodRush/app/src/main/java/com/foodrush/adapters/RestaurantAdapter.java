package com.foodrush.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodrush.R;
import com.foodrush.models.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    Context context;
    List<Restaurant> list;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(Restaurant restaurant);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RestaurantAdapter(Context context, List<Restaurant> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(List<Restaurant> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant r = list.get(position);

        holder.name.setText(r.getName());
        holder.desc.setText(r.getDescription());
        Glide.with(context)
                .load(r.getImageUrl())
                .placeholder(R.drawable.ic_food_placeholder)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(r);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, desc;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            desc = itemView.findViewById(R.id.tvDesc);
            image = itemView.findViewById(R.id.ivImage);
        }
    }
}