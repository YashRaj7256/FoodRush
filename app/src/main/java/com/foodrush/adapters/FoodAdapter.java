package com.foodrush.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodrush.R;
import com.foodrush.database.CartManager;
import com.foodrush.models.FoodItem;

import java.util.List;

/**
 * FoodAdapter — RecyclerView adapter for displaying food items in a 2-column grid.
 * Handles Add-to-Cart button logic.
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private final Context context;
    private List<FoodItem> foodList;

    public FoodAdapter(Context context, List<FoodItem> foodList) {
        this.context  = context;
        this.foodList = foodList;
    }

    /** Called by HomeFragment when filter/search changes the list */
    public void updateList(List<FoodItem> newList) {
        this.foodList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem item = foodList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("₹%.0f", item.getPrice()));
        holder.tvCategory.setText(item.getCategory());

        if (holder.ivVegType != null) {
            holder.ivVegType.setImageResource(item.isVeg() ? R.drawable.ic_veg : R.drawable.ic_non_veg);
        }

        // Load image using Glide; fall back to placeholder
        Glide.with(context)
             .load(item.getImageUrl())
             .placeholder(R.drawable.ic_food_placeholder)
             .error(R.drawable.ic_food_placeholder)
             .centerCrop()
             .into(holder.ivFood);

        int quantity = CartManager.getInstance().getQuantity(item.getId());
        holder.btnAddToCart.setText(quantity > 0
                ? context.getString(R.string.added_quantity, quantity)
                : context.getString(R.string.add_to_cart));

        holder.btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addItem(item);
            int updatedQuantity = CartManager.getInstance().getQuantity(item.getId());
            holder.btnAddToCart.setText(context.getString(R.string.added_quantity, updatedQuantity));
            Toast.makeText(context, item.getName() + " added to cart 🛒", Toast.LENGTH_SHORT).show();
            if (context instanceof com.foodrush.activities.MainActivity) {
                ((com.foodrush.activities.MainActivity) context).updateCartBadge();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    /** ViewHolder: holds references to all views in item_food.xml */
    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood, ivVegType;
        TextView  tvName, tvPrice, tvCategory;
        Button    btnAddToCart;

        FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFood      = itemView.findViewById(R.id.ivFood);
            ivVegType   = itemView.findViewById(R.id.ivVegType);
            tvName      = itemView.findViewById(R.id.tvName);
            tvPrice     = itemView.findViewById(R.id.tvPrice);
            tvCategory  = itemView.findViewById(R.id.tvCategory);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
