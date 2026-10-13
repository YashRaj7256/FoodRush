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
    OnFavoriteClickListener favoriteListener;
    java.util.Set<String> favoriteIds = new java.util.HashSet<>();

    public interface OnItemClickListener {
        void onClick(Restaurant restaurant);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Restaurant restaurant, boolean isFavorite);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener favoriteListener) {
        this.favoriteListener = favoriteListener;
    }

    public void setFavorites(java.util.Set<String> favorites) {
        this.favoriteIds = favorites != null ? favorites : new java.util.HashSet<>();
        notifyDataSetChanged();
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

        if (holder.tvRating != null) {
            holder.tvRating.setText(String.format(java.util.Locale.getDefault(), "⭐ %.1f", r.getRating()));
        }
        if (holder.tvArea != null) {
            holder.tvArea.setText("📍 " + (r.getArea() != null ? r.getArea() : "Ahmedabad"));
        }
        if (holder.tvDeliveryTime != null) {
            holder.tvDeliveryTime.setText("⏱ " + (r.getDeliveryTime() != null ? r.getDeliveryTime() : "30m"));
        }

        Glide.with(context)
                .load(r.getImageUrl())
                .placeholder(R.drawable.ic_food_placeholder)
                .into(holder.image);

        final boolean isFav = favoriteIds.contains(r.getId());
        if (holder.ivFavorite != null) {
            holder.ivFavorite.setColorFilter(
                    androidx.core.content.ContextCompat.getColor(context, isFav ? R.color.primary : R.color.text_disabled)
            );
            holder.ivFavorite.setOnClickListener(v -> {
                boolean newFavState = !favoriteIds.contains(r.getId());
                if (newFavState) {
                    favoriteIds.add(r.getId());
                } else {
                    favoriteIds.remove(r.getId());
                }
                holder.ivFavorite.setColorFilter(
                        androidx.core.content.ContextCompat.getColor(context, newFavState ? R.color.primary : R.color.text_disabled)
                );
                if (favoriteListener != null) {
                    favoriteListener.onFavoriteClick(r, newFavState);
                }
            });
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(r);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, desc, tvRating, tvArea, tvDeliveryTime;
        ImageView image, ivFavorite;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            desc = itemView.findViewById(R.id.tvDesc);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvArea = itemView.findViewById(R.id.tvArea);
            tvDeliveryTime = itemView.findViewById(R.id.tvDeliveryTime);
            image = itemView.findViewById(R.id.ivImage);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
        }
    }
}