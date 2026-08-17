package com.foodrush.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.foodrush.R;

/**
 * OrderConfirmationActivity — shown after an order is placed.
 * Receives order details via Intent extras.
 */
public class OrderConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        TextView tvOrderId    = findViewById(R.id.tvOrderId);
        TextView tvOrderTotal = findViewById(R.id.tvOrderTotal);
        Button   btnBackHome  = findViewById(R.id.btnBackHome);

        // Receive order data from Intent
        String orderId = getIntent().getStringExtra("ORDER_ID");
        double total   = getIntent().getDoubleExtra("TOTAL", 0);

        tvOrderId.setText("Order ID: #" + orderId);
        tvOrderTotal.setText("Total Paid: ₹" + String.format("%.2f", total));

        // Go back to main home screen
        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    // Prevent back navigation from this screen
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
