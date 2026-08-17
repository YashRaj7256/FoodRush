package com.foodrush.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodrush.R;
import com.foodrush.database.CartManager;
import com.foodrush.models.CartItem;

import java.util.List;

/**
 * CartAdapter — displays cart items with +/- quantity controls and delete button.
 * Communicates quantity changes back to CartFragment via CartUpdateListener.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final List<CartItem> cartItems;
    private final CartUpdateListener listener;

    /** Interface so CartFragment can react to cart changes */
    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, CartUpdateListener listener) {
        this.context   = context;
        this.cartItems = cartItems;
        this.listener  = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.tvName.setText(cartItem.getFoodItem().getName());
        holder.tvPrice.setText(String.format("₹%.0f", cartItem.getFoodItem().getPrice()));
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.tvItemTotal.setText(String.format("₹%.2f", cartItem.getTotalPrice()));

        // Increase quantity
        holder.btnPlus.setOnClickListener(v -> {
            CartManager.getInstance().addItem(cartItem.getFoodItem());
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            notifyItemChanged(position);
            listener.onCartUpdated();
        });

        // Decrease quantity (removes if reaches 0)
        holder.btnMinus.setOnClickListener(v -> {
            CartManager.getInstance().removeItem(cartItem.getFoodItem());
            int newQty = cartItem.getQuantity() - 1;
            if (newQty <= 0) {
                cartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
            } else {
                cartItem.setQuantity(newQty);
                notifyItemChanged(position);
            }
            listener.onCartUpdated();
        });

        // Delete item from cart
        holder.btnDelete.setOnClickListener(v -> {
            CartManager.getInstance().deleteItem(cartItem.getFoodItem().getId());
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            listener.onCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView    tvName, tvPrice, tvQuantity, tvItemTotal;
        ImageButton btnPlus, btnMinus, btnDelete;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName      = itemView.findViewById(R.id.tvName);
            tvPrice     = itemView.findViewById(R.id.tvPrice);
            tvQuantity  = itemView.findViewById(R.id.tvQuantity);
            tvItemTotal = itemView.findViewById(R.id.tvItemTotal);
            btnPlus     = itemView.findViewById(R.id.btnPlus);
            btnMinus    = itemView.findViewById(R.id.btnMinus);
            btnDelete   = itemView.findViewById(R.id.btnDelete);
        }
    }
}
