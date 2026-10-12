package com.foodrush.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodrush.R;
import com.foodrush.activities.OrderConfirmationActivity;
import com.foodrush.adapters.CartAdapter;
import com.foodrush.database.CartManager;
import com.foodrush.database.FirebaseHelper;
import com.foodrush.models.CartItem;
import com.foodrush.utils.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * CartFragment — displays items in cart, bill breakdown, payment method picker,
 * and order checkout logic.
 */
public class CartFragment extends Fragment implements CartAdapter.CartUpdateListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView tvTotal, tvEmpty, tvCartSubtitle;
    private TextView tvSubtotal, tvTaxes, tvGrandTotal, tvSelectedPaymentNotice;
    private View emptyStateLayout, cartContent;
    private Button btnPlaceOrder;

    // Payment selectors
    private View btnPaymentCod, btnPaymentUpi, btnPaymentCard;
    private TextView tvCodLabel, tvUpiLabel, tvCardLabel;
    private String selectedPaymentMethod = "Cash on Delivery";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView             = view.findViewById(R.id.recyclerView);
        tvTotal                  = view.findViewById(R.id.tvTotal);
        tvEmpty                  = view.findViewById(R.id.tvEmpty);
        tvCartSubtitle           = view.findViewById(R.id.tvCartSubtitle);
        emptyStateLayout         = view.findViewById(R.id.emptyStateLayout);
        cartContent              = view.findViewById(R.id.cartContent);
        btnPlaceOrder            = view.findViewById(R.id.btnPlaceOrder);

        tvSubtotal               = view.findViewById(R.id.tvSubtotal);
        tvTaxes                  = view.findViewById(R.id.tvTaxes);
        tvGrandTotal             = view.findViewById(R.id.tvGrandTotal);
        tvSelectedPaymentNotice  = view.findViewById(R.id.tvSelectedPaymentNotice);

        btnPaymentCod            = view.findViewById(R.id.btnPaymentCod);
        btnPaymentUpi            = view.findViewById(R.id.btnPaymentUpi);
        btnPaymentCard           = view.findViewById(R.id.btnPaymentCard);

        tvCodLabel               = view.findViewById(R.id.tvCodLabel);
        tvUpiLabel               = view.findViewById(R.id.tvUpiLabel);
        tvCardLabel              = view.findViewById(R.id.tvCardLabel);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setupPaymentSelection();
        refreshCart();

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void setupPaymentSelection() {
        btnPaymentCod.setOnClickListener(v -> selectPayment("Cash on Delivery"));
        btnPaymentUpi.setOnClickListener(v -> selectPayment("UPI / Online Pay"));
        btnPaymentCard.setOnClickListener(v -> selectPayment("Card"));
    }

    private void selectPayment(String method) {
        selectedPaymentMethod = method;

        int selectedBg = R.drawable.bg_payment_selected;
        int unselectedBg = R.drawable.bg_payment_unselected;

        int primaryColor = requireContext().getColor(R.color.primary);
        int secondaryColor = requireContext().getColor(R.color.text_secondary);

        // Reset all
        btnPaymentCod.setBackgroundResource(unselectedBg);
        btnPaymentUpi.setBackgroundResource(unselectedBg);
        btnPaymentCard.setBackgroundResource(unselectedBg);

        tvCodLabel.setTextColor(secondaryColor);
        tvUpiLabel.setTextColor(secondaryColor);
        tvCardLabel.setTextColor(secondaryColor);

        // Apply active state
        if ("Cash on Delivery".equals(method)) {
            btnPaymentCod.setBackgroundResource(selectedBg);
            tvCodLabel.setTextColor(primaryColor);
        } else if ("UPI / Online Pay".equals(method)) {
            btnPaymentUpi.setBackgroundResource(selectedBg);
            tvUpiLabel.setTextColor(primaryColor);
        } else {
            btnPaymentCard.setBackgroundResource(selectedBg);
            tvCardLabel.setTextColor(primaryColor);
        }

        tvSelectedPaymentNotice.setText(method);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) refreshCart();
    }

    /** Reloads cart items and updates the UI */
    public void refreshCart() {
        List<CartItem> items = CartManager.getInstance().getCartItems();

        if (items.isEmpty()) {
            if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.VISIBLE);
            if (cartContent != null) cartContent.setVisibility(View.GONE);
            if (tvCartSubtitle != null) tvCartSubtitle.setText(R.string.empty_cart);
            return;
        }

        if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.GONE);
        if (cartContent != null) cartContent.setVisibility(View.VISIBLE);

        int totalCount = CartManager.getInstance().getTotalCount();
        if (tvCartSubtitle != null) {
            tvCartSubtitle.setText(getString(R.string.items_summary, totalCount));
        }

        cartAdapter = new CartAdapter(getContext(), items, this);
        recyclerView.setAdapter(cartAdapter);
        updateTotal();
    }

    /** Updates the displayed bill details and grand total */
    private void updateTotal() {
        double subtotal = CartManager.getInstance().getTotalPrice();
        double taxes = subtotal * 0.05; // 5% taxes and restaurant handling fee
        double grandTotal = subtotal + taxes;

        String formattedSubtotal = String.format(Locale.getDefault(), "₹%.2f", subtotal);
        String formattedTaxes = String.format(Locale.getDefault(), "₹%.2f", taxes);
        String formattedGrandTotal = String.format(Locale.getDefault(), "₹%.2f", grandTotal);

        if (tvSubtotal != null) tvSubtotal.setText(formattedSubtotal);
        if (tvTaxes != null) tvTaxes.setText(formattedTaxes);
        if (tvGrandTotal != null) tvGrandTotal.setText(formattedGrandTotal);
        if (tvTotal != null) tvTotal.setText(formattedGrandTotal);
    }

    /** Called by CartAdapter when quantity changes */
    @Override
    public void onCartUpdated() {
        updateTotal();
        if (tvCartSubtitle != null) {
            int totalCount = CartManager.getInstance().getTotalCount();
            tvCartSubtitle.setText(getString(R.string.items_summary, totalCount));
        }
        if (CartManager.getInstance().getCartItems().isEmpty()) {
            refreshCart();
        }
    }

    /** Places the order: saves to Firebase, clears cart, sends notification */
    private void placeOrder() {
        List<CartItem> items = CartManager.getInstance().getCartItems();
        List<Map<String, Object>> itemList = new ArrayList<>();

        if (items.isEmpty()) {
            Toast.makeText(getContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), R.string.login_to_order, Toast.LENGTH_SHORT).show();
            return;
        }

        String uid       = currentUser.getUid();
        String orderId   = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        double subtotal  = CartManager.getInstance().getTotalPrice();
        double taxes     = subtotal * 0.05;
        double finalTotal= subtotal + taxes;

        // Build item array
        for (CartItem cartItem : items) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", cartItem.getFoodItem().getName());
            map.put("price", cartItem.getFoodItem().getPrice());
            map.put("quantity", cartItem.getQuantity());
            if (cartItem.getFoodItem().getImageUrl() != null) {
                map.put("imageUrl", cartItem.getFoodItem().getImageUrl());
            }
            itemList.add(map);
        }

        // Build order map
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderId", orderId);
        orderMap.put("userId", uid);
        orderMap.put("items", itemList);
        orderMap.put("totalAmount", finalTotal);
        orderMap.put("paymentMethod", selectedPaymentMethod);
        orderMap.put("status", "Placed");
        orderMap.put("timestamp", System.currentTimeMillis());

        btnPlaceOrder.setEnabled(false);
        btnPlaceOrder.setText(R.string.placing_order);

        FirebaseHelper.getInstance()
                .getOrdersRef()
                .child(uid)
                .child(orderId)
                .setValue(orderMap)
                .addOnCompleteListener(task -> {
                    if (!isAdded()) return;
                    btnPlaceOrder.setEnabled(true);
                    btnPlaceOrder.setText(R.string.place_order);
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(), R.string.order_save_failed, Toast.LENGTH_LONG).show();
                        return;
                    }

                    NotificationHelper.showOrderPlacedNotification(requireContext(), orderId, finalTotal);
                    CartManager.getInstance().clearCart();

                    Intent intent = new Intent(getActivity(), OrderConfirmationActivity.class);
                    intent.putExtra("ORDER_ID", orderId);
                    intent.putExtra("TOTAL", finalTotal);
                    intent.putExtra("PAYMENT_METHOD", selectedPaymentMethod);
                    startActivity(intent);
                });
    }
}
