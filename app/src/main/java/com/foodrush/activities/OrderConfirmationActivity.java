package com.foodrush.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.foodrush.R;

import java.util.Locale;

/**
 * OrderConfirmationActivity — shown after an order is placed.
 * Displays receipt details: Order ID, Payment Method, Total and estimated delivery.
 */
public class OrderConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        TextView tvOrderId       = findViewById(R.id.tvOrderId);
        TextView tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        TextView tvOrderTotal    = findViewById(R.id.tvOrderTotal);
        Button   btnBackHome     = findViewById(R.id.btnBackHome);

        // Receive order data from Intent
        String orderId = getIntent().getStringExtra("ORDER_ID");
        String payment = getIntent().getStringExtra("PAYMENT_METHOD");
        double total   = getIntent().getDoubleExtra("TOTAL", 0);

        tvOrderId.setText("#" + (orderId != null ? orderId : ""));
        tvOrderTotal.setText(String.format(Locale.getDefault(), "₹%.2f", total));

        if (payment != null && !payment.isEmpty()) {
            tvPaymentMethod.setText(payment);
        } else {
            tvPaymentMethod.setText("Cash on Delivery");
        }

        // Go back to main home screen
        btnBackHome.setOnClickListener(v -> navigateToMain());
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        navigateToMain();
    }
}
