package com.foodrush.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodrush.R;
import com.foodrush.models.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * OrderAdapter — displays past orders in a simple list.
 * Shows order ID, date, item count, total, and status badge.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final Context context;
    private final List<Order> orders;

    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders  = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.tvOrderId.setText("Order #" + order.getOrderId());
        holder.tvDate.setText(DATE_FORMAT.format(new Date(order.getTimestamp())));
        holder.tvTotal.setText(String.format("₹%.2f", order.getTotalAmount()));
        holder.tvStatus.setText(order.getStatus());

        // Count total items
        int itemCount = 0;
        if (order.getItems() != null) {
            for (com.foodrush.models.CartItem item : order.getItems()) {
                itemCount += item.getQuantity();
            }
        }
        holder.tvItemCount.setText(itemCount + (itemCount == 1 ? " item" : " items"));

        // Color-code status
        int statusColor;
        switch (order.getStatus()) {
            case "Delivered": statusColor = context.getColor(R.color.green); break;
            case "Cancelled": statusColor = context.getColor(R.color.red);   break;
            default:          statusColor = context.getColor(R.color.primary); break;
        }
        holder.tvStatus.setTextColor(statusColor);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate, tvTotal, tvStatus, tvItemCount;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId   = itemView.findViewById(R.id.tvOrderId);
            tvDate      = itemView.findViewById(R.id.tvDate);
            tvTotal     = itemView.findViewById(R.id.tvTotal);
            tvStatus    = itemView.findViewById(R.id.tvStatus);
            tvItemCount = itemView.findViewById(R.id.tvItemCount);
        }
    }
}
