package com.foodrush.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 * OrderAdapter — displays past orders with order status pill, payment method,
 * and a 3-step timeline tracker (Confirmed -> Kitchen -> Delivered).
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

        holder.tvOrderId.setText("Order #" + (order.getOrderId() != null ? order.getOrderId() : ""));
        holder.tvDate.setText(DATE_FORMAT.format(new Date(order.getTimestamp())));
        holder.tvTotal.setText(String.format(Locale.getDefault(), "₹%.2f", order.getTotalAmount()));

        String status = order.getStatus() == null ? "Placed" : order.getStatus();
        holder.tvStatus.setText(status);

        // Payment method display
        String payment = order.getPaymentMethod();
        if (payment == null || payment.isEmpty()) {
            payment = "Cash on Delivery";
        }
        String paymentIcon = "💵 ";
        if (payment.toLowerCase().contains("upi") || payment.toLowerCase().contains("online")) {
            paymentIcon = "📱 ";
        } else if (payment.toLowerCase().contains("card")) {
            paymentIcon = "💳 ";
        }
        holder.tvPaymentMethod.setText(paymentIcon + payment);

        // Count total items
        int itemCount = 0;
        if (order.getItems() != null) {
            for (com.foodrush.models.CartItem item : order.getItems()) {
                itemCount += item.getQuantity();
            }
        }
        holder.tvItemCount.setText(itemCount + (itemCount == 1 ? " item" : " items"));

        // Status Styling and Timeline Progress
        bindStatusAndTimeline(holder, status);
    }

    private void bindStatusAndTimeline(@NonNull OrderViewHolder holder, String status) {
        int primaryColor = context.getColor(R.color.primary);
        int dividerColor = context.getColor(R.color.divider);

        // Reset timeline points
        holder.step1.setImageTintList(ColorStateList.valueOf(dividerColor));
        holder.line1.setBackgroundColor(dividerColor);
        holder.step2.setImageTintList(ColorStateList.valueOf(dividerColor));
        holder.line2.setBackgroundColor(dividerColor);
        holder.step3.setImageTintList(ColorStateList.valueOf(dividerColor));

        if ("Delivered".equalsIgnoreCase(status)) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_delivered);
            holder.tvStatus.setTextColor(context.getColor(R.color.status_delivered));

            holder.step1.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.status_delivered)));
            holder.line1.setBackgroundColor(context.getColor(R.color.status_delivered));
            holder.step2.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.status_delivered)));
            holder.line2.setBackgroundColor(context.getColor(R.color.status_delivered));
            holder.step3.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.status_delivered)));
        } else if ("Preparing".equalsIgnoreCase(status) || "In Kitchen".equalsIgnoreCase(status)) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_preparing);
            holder.tvStatus.setTextColor(context.getColor(R.color.status_preparing));

            holder.step1.setImageTintList(ColorStateList.valueOf(primaryColor));
            holder.line1.setBackgroundColor(primaryColor);
            holder.step2.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.status_preparing)));
        } else if ("Cancelled".equalsIgnoreCase(status)) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_cancelled);
            holder.tvStatus.setTextColor(context.getColor(R.color.status_cancelled));
        } else {
            // Placed / Confirmed / Default
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_placed);
            holder.tvStatus.setTextColor(context.getColor(R.color.status_placed));

            holder.step1.setImageTintList(ColorStateList.valueOf(primaryColor));
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate, tvTotal, tvStatus, tvItemCount, tvPaymentMethod;
        ImageView step1, step2, step3;
        View line1, line2;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId       = itemView.findViewById(R.id.tvOrderId);
            tvDate          = itemView.findViewById(R.id.tvDate);
            tvTotal         = itemView.findViewById(R.id.tvTotal);
            tvStatus        = itemView.findViewById(R.id.tvStatus);
            tvItemCount     = itemView.findViewById(R.id.tvItemCount);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);

            step1 = itemView.findViewById(R.id.step1);
            step2 = itemView.findViewById(R.id.step2);
            step3 = itemView.findViewById(R.id.step3);
            line1 = itemView.findViewById(R.id.line1);
            line2 = itemView.findViewById(R.id.line2);
        }
    }
}
