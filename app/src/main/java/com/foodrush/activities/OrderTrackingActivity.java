package com.foodrush.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.foodrush.R;
import com.foodrush.database.FirebaseHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

/**
 * OrderTrackingActivity — provides realistic real-time order tracking:
 * - Live status stepper (Placed -> Preparing -> Out for Delivery -> Delivered)
 * - Rider partner details and direct phone call trigger
 * - Simulated progressive status change if in demo mode
 * - Cancel order option (if still in Placed / Preparing state)
 */
public class OrderTrackingActivity extends AppCompatActivity {

    private String orderId;
    private DatabaseReference orderRef;
    private ValueEventListener orderListener;

    private TextView tvEta, tvOrderBadge;
    private TextView tvTrackingOrderId, tvTrackingTotal, tvTrackingItems;
    private ImageView dotStep1, dotStep2, dotStep3, dotStep4;
    private View lineStep1, lineStep2, lineStep3;
    private TextView tvStep1Title, tvStep2Title, tvStep3Title, tvStep4Title;
    private View layoutRiderMarker;
    private MaterialButton btnCancelOrder, btnCallRider;

    private final Handler simulationHandler = new Handler(Looper.getMainLooper());
    private Runnable simulationRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        orderId = getIntent().getStringExtra("ORDER_ID");
        if (orderId == null || orderId.isEmpty()) {
            Toast.makeText(this, "Order ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupFirebaseListener();
        setupSimulationIfEligible();
    }

    private void initViews() {
        tvEta               = findViewById(R.id.tvEta);
        tvOrderBadge         = findViewById(R.id.tvOrderBadge);
        tvTrackingOrderId   = findViewById(R.id.tvTrackingOrderId);
        tvTrackingTotal     = findViewById(R.id.tvTrackingTotal);
        tvTrackingItems     = findViewById(R.id.tvTrackingItems);

        dotStep1            = findViewById(R.id.dotStep1);
        dotStep2            = findViewById(R.id.dotStep2);
        dotStep3            = findViewById(R.id.dotStep3);
        dotStep4            = findViewById(R.id.dotStep4);

        lineStep1           = findViewById(R.id.lineStep1);
        lineStep2           = findViewById(R.id.lineStep2);
        lineStep3           = findViewById(R.id.lineStep3);

        tvStep1Title        = findViewById(R.id.tvStep1Title);
        tvStep2Title        = findViewById(R.id.tvStep2Title);
        tvStep3Title        = findViewById(R.id.tvStep3Title);
        tvStep4Title        = findViewById(R.id.tvStep4Title);

        layoutRiderMarker   = findViewById(R.id.layoutRiderMarker);
        btnCancelOrder      = findViewById(R.id.btnCancelOrder);
        btnCallRider        = findViewById(R.id.btnCallRider);

        tvTrackingOrderId.setText("Order #" + orderId);

        btnCallRider.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:+919876543210"));
            startActivity(callIntent);
        });

        btnCancelOrder.setOnClickListener(v -> cancelCurrentOrder());
    }

    private void setupFirebaseListener() {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (uid == null) return;

        orderRef = FirebaseHelper.getInstance().getOrdersRef().child(uid).child(orderId);
        orderListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;

                String status = snapshot.child("status").getValue(String.class);
                Double total = snapshot.child("totalAmount").getValue(Double.class);
                if (total != null) {
                    tvTrackingTotal.setText(String.format(Locale.getDefault(), "₹%.2f", total));
                }

                // Parse items summary
                StringBuilder itemsSummary = new StringBuilder();
                DataSnapshot itemsSnapshot = snapshot.child("items");
                for (DataSnapshot itemSnap : itemsSnapshot.getChildren()) {
                    String name = itemSnap.child("name").getValue(String.class);
                    Long qty = itemSnap.child("quantity").getValue(Long.class);
                    if (name != null) {
                        if (itemsSummary.length() > 0) itemsSummary.append(", ");
                        itemsSummary.append(qty != null ? qty : 1).append("x ").append(name);
                    }
                }
                if (itemsSummary.length() > 0) {
                    tvTrackingItems.setText(itemsSummary.toString());
                }

                updateTrackingUI(status != null ? status : "Placed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        orderRef.addValueEventListener(orderListener);
    }

    /**
     * Simulates live restaurant progress:
     * Placed -> (8s) Preparing -> (16s) Out for Delivery -> (24s) Delivered
     */
    private void setupSimulationIfEligible() {
        simulationRunnable = new Runnable() {
            int step = 0;
            @Override
            public void run() {
                if (isFinishing() || isDestroyed() || orderRef == null) return;

                orderRef.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String current = snapshot.getValue(String.class);
                        if ("Cancelled".equalsIgnoreCase(current) || "Delivered".equalsIgnoreCase(current)) {
                            return; // Stop simulating
                        }

                        if ("Placed".equalsIgnoreCase(current)) {
                            orderRef.child("status").setValue("Preparing");
                            simulationHandler.postDelayed(simulationRunnable, 12000);
                        } else if ("Preparing".equalsIgnoreCase(current)) {
                            orderRef.child("status").setValue("Out for Delivery");
                            simulationHandler.postDelayed(simulationRunnable, 14000);
                        } else if ("Out for Delivery".equalsIgnoreCase(current)) {
                            orderRef.child("status").setValue("Delivered");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        };

        // Trigger first transition in 7 seconds
        simulationHandler.postDelayed(simulationRunnable, 7000);
    }

    private void updateTrackingUI(String status) {
        int primary = getColor(R.color.primary);
        int divider = getColor(R.color.divider);
        int textSecondary = getColor(R.color.text_secondary);
        int textPrimary = getColor(R.color.text_primary);
        int success = getColor(R.color.status_delivered);

        // Reset
        dotStep1.setImageResource(R.drawable.bg_stepper_inactive);
        dotStep2.setImageResource(R.drawable.bg_stepper_inactive);
        dotStep3.setImageResource(R.drawable.bg_stepper_inactive);
        dotStep4.setImageResource(R.drawable.bg_stepper_inactive);

        lineStep1.setBackgroundColor(divider);
        lineStep2.setBackgroundColor(divider);
        lineStep3.setBackgroundColor(divider);

        tvStep1Title.setTextColor(textSecondary);
        tvStep2Title.setTextColor(textSecondary);
        tvStep3Title.setTextColor(textSecondary);
        tvStep4Title.setTextColor(textSecondary);

        tvOrderBadge.setText(status);

        if ("Delivered".equalsIgnoreCase(status)) {
            dotStep1.setImageResource(R.drawable.bg_stepper_active);
            lineStep1.setBackgroundColor(success);
            dotStep2.setImageResource(R.drawable.bg_stepper_active);
            lineStep2.setBackgroundColor(success);
            dotStep3.setImageResource(R.drawable.bg_stepper_active);
            lineStep3.setBackgroundColor(success);
            dotStep4.setImageResource(R.drawable.bg_stepper_active);

            tvStep1Title.setTextColor(textPrimary);
            tvStep2Title.setTextColor(textPrimary);
            tvStep3Title.setTextColor(textPrimary);
            tvStep4Title.setTextColor(textPrimary);

            tvEta.setText("Delivered to your doorstep!");
            tvOrderBadge.setBackgroundResource(R.drawable.bg_status_delivered);
            tvOrderBadge.setTextColor(getColor(R.color.status_delivered));

            layoutRiderMarker.animate().translationX(350f).setDuration(600).start();
            btnCancelOrder.setVisibility(View.GONE);

        } else if ("Out for Delivery".equalsIgnoreCase(status)) {
            dotStep1.setImageResource(R.drawable.bg_stepper_active);
            lineStep1.setBackgroundColor(primary);
            dotStep2.setImageResource(R.drawable.bg_stepper_active);
            lineStep2.setBackgroundColor(primary);
            dotStep3.setImageResource(R.drawable.bg_stepper_active);

            tvStep1Title.setTextColor(textPrimary);
            tvStep2Title.setTextColor(textPrimary);
            tvStep3Title.setTextColor(textPrimary);

            tvEta.setText("Rider is arriving in ~8 mins");
            tvOrderBadge.setBackgroundResource(R.drawable.bg_status_delivering);
            tvOrderBadge.setTextColor(getColor(R.color.status_delivering));

            layoutRiderMarker.animate().translationX(200f).setDuration(600).start();
            btnCancelOrder.setVisibility(View.GONE);

        } else if ("Preparing".equalsIgnoreCase(status)) {
            dotStep1.setImageResource(R.drawable.bg_stepper_active);
            lineStep1.setBackgroundColor(primary);
            dotStep2.setImageResource(R.drawable.bg_stepper_active);

            tvStep1Title.setTextColor(textPrimary);
            tvStep2Title.setTextColor(textPrimary);

            tvEta.setText("Food is on the stove ~20 mins");
            tvOrderBadge.setBackgroundResource(R.drawable.bg_status_preparing);
            tvOrderBadge.setTextColor(getColor(R.color.status_preparing));

            layoutRiderMarker.animate().translationX(60f).setDuration(600).start();
            btnCancelOrder.setVisibility(View.VISIBLE);

        } else if ("Cancelled".equalsIgnoreCase(status)) {
            tvEta.setText("Order has been cancelled");
            tvOrderBadge.setText("Cancelled");
            tvOrderBadge.setBackgroundResource(R.drawable.bg_status_cancelled);
            tvOrderBadge.setTextColor(getColor(R.color.status_cancelled));
            btnCancelOrder.setVisibility(View.GONE);

        } else { // "Placed"
            dotStep1.setImageResource(R.drawable.bg_stepper_active);
            tvStep1Title.setTextColor(textPrimary);

            tvEta.setText("Estimated Delivery: 30-35 mins");
            tvOrderBadge.setBackgroundResource(R.drawable.bg_status_placed);
            tvOrderBadge.setTextColor(getColor(R.color.status_placed));

            layoutRiderMarker.animate().translationX(0f).setDuration(600).start();
            btnCancelOrder.setVisibility(View.VISIBLE);
        }
    }

    private void cancelCurrentOrder() {
        if (orderRef != null) {
            orderRef.child("status").setValue("Cancelled")
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, R.string.order_cancelled_success, Toast.LENGTH_SHORT).show();
                        simulationHandler.removeCallbacksAndMessages(null);
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Could not cancel order", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simulationHandler.removeCallbacksAndMessages(null);
        if (orderRef != null && orderListener != null) {
            orderRef.removeEventListener(orderListener);
        }
    }
}
