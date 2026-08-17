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
import com.foodrush.models.Order;
import com.foodrush.utils.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * CartFragment — shows all items in the cart with totals and place-order button.
 */
public class CartFragment extends Fragment implements CartAdapter.CartUpdateListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView tvTotal, tvEmpty;
    private Button btnPlaceOrder;
    private View cartContent;

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

        recyclerView  = view.findViewById(R.id.recyclerView);
        tvTotal       = view.findViewById(R.id.tvTotal);
        tvEmpty       = view.findViewById(R.id.tvEmpty);
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);
        cartContent   = view.findViewById(R.id.cartContent);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshCart();

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    /** Reloads cart items and updates the UI */
    public void refreshCart() {
        List<CartItem> items = CartManager.getInstance().getCartItems();

        if (items.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            cartContent.setVisibility(View.GONE);
            return;
        }

        tvEmpty.setVisibility(View.GONE);
        cartContent.setVisibility(View.VISIBLE);

        cartAdapter = new CartAdapter(getContext(), items, this);
        recyclerView.setAdapter(cartAdapter);
        updateTotal();
    }

    /** Updates the displayed total price */
    private void updateTotal() {
        double total = CartManager.getInstance().getTotalPrice();
        tvTotal.setText(String.format("Total: ₹%.2f", total));
    }

    /** Called by CartAdapter when quantity changes */
    @Override
    public void onCartUpdated() {
        updateTotal();
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

        String uid     = currentUser.getUid();
        String orderId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        double total   = CartManager.getInstance().getTotalPrice();

        // Build and save order
        for (CartItem cartItem : items) {
            Map<String, Object> map = new HashMap<>();

            map.put("name", cartItem.getFoodItem().getName());
            map.put("price", cartItem.getFoodItem().getPrice());
            map.put("quantity", cartItem.getQuantity());

            itemList.add(map);
        }

        // 🔥 Build order map
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderId", orderId);
        orderMap.put("userId", uid);
        orderMap.put("items", itemList);
        orderMap.put("totalAmount", total);
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

                    NotificationHelper.showOrderPlacedNotification(requireContext(), orderId, total);
                    CartManager.getInstance().clearCart();

                    Intent intent = new Intent(getActivity(), OrderConfirmationActivity.class);
                    intent.putExtra("ORDER_ID", orderId);
                    intent.putExtra("TOTAL", total);
                    startActivity(intent);
                });
    }
}
