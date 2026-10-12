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

    // Coupon views & state
    private android.widget.EditText etCouponCode;
    private com.google.android.material.button.MaterialButton btnApplyCoupon;
    private TextView tvCouponFeedback, tvDiscount;
    private View layoutDiscountRow;
    private com.foodrush.models.Coupon appliedCoupon = null;
    private final Map<String, com.foodrush.models.Coupon> couponMap = new HashMap<>();

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

        // Coupon views
        etCouponCode             = view.findViewById(R.id.etCouponCode);
        btnApplyCoupon           = view.findViewById(R.id.btnApplyCoupon);
        tvCouponFeedback         = view.findViewById(R.id.tvCouponFeedback);
        tvDiscount               = view.findViewById(R.id.tvDiscount);
        layoutDiscountRow        = view.findViewById(R.id.layoutDiscountRow);

        initCoupons();
        setupCouponListeners(view);
        setupAddressSelector(view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setupPaymentSelection();
        refreshCart();

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void setupAddressSelector(View view) {
        TextView tvAddressIcon = view.findViewById(R.id.tvAddressIcon);
        TextView tvAddressType = view.findViewById(R.id.tvAddressType);
        TextView tvAddressDetails = view.findViewById(R.id.tvAddressDetails);
        View btnChangeAddress = view.findViewById(R.id.btnChangeAddress);
        View cardDeliveryAddress = view.findViewById(R.id.cardDeliveryAddress);

        final String[] addressOptions = {
                "🏠 Home - Flat 402, Sunshine Heights, Ring Road, Ahmedabad",
                "🏢 Work - ByteSpace Tech Park, Tower B, 3rd Floor, Ahmedabad",
                "📍 Hotel - Room 204, Grand Palace Hotel, Station Road, Ahmedabad"
        };

        View.OnClickListener clickListener = v -> {
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Select Delivery Address")
                    .setItems(addressOptions, (dialog, which) -> {
                        String selected = addressOptions[which];
                        if (which == 0) {
                            tvAddressIcon.setText("🏠");
                            tvAddressType.setText("Delivering to Home");
                            tvAddressDetails.setText("Flat 402, Sunshine Heights, Ring Road, Ahmedabad");
                        } else if (which == 1) {
                            tvAddressIcon.setText("🏢");
                            tvAddressType.setText("Delivering to Work");
                            tvAddressDetails.setText("ByteSpace Tech Park, Tower B, 3rd Floor, Ahmedabad");
                        } else {
                            tvAddressIcon.setText("📍");
                            tvAddressType.setText("Delivering to Hotel / Other");
                            tvAddressDetails.setText("Room 204, Grand Palace Hotel, Station Road, Ahmedabad");
                        }
                        Toast.makeText(getContext(), "Delivery address updated", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        };

        if (btnChangeAddress != null) btnChangeAddress.setOnClickListener(clickListener);
        if (cardDeliveryAddress != null) cardDeliveryAddress.setOnClickListener(clickListener);
    }

    private void initCoupons() {
        couponMap.put("RUSH50", new com.foodrush.models.Coupon("RUSH50", "50% OFF up to ₹100", 50, 100, 149));
        couponMap.put("HUNGRY", new com.foodrush.models.Coupon("HUNGRY", "Flat ₹75 OFF on orders above ₹199", 100, 75, 199));
        couponMap.put("SAVE20", new com.foodrush.models.Coupon("SAVE20", "20% OFF up to ₹150", 20, 150, 99));
    }

    private void setupCouponListeners(View view) {
        btnApplyCoupon.setOnClickListener(v -> {
            if (appliedCoupon != null) {
                // Remove coupon
                appliedCoupon = null;
                etCouponCode.setEnabled(true);
                etCouponCode.setText("");
                btnApplyCoupon.setText(R.string.apply);
                btnApplyCoupon.setBackgroundTintList(android.content.res.ColorStateList.valueOf(requireContext().getColor(R.color.primary)));
                tvCouponFeedback.setVisibility(View.GONE);
                layoutDiscountRow.setVisibility(View.GONE);
                updateTotal();
                Toast.makeText(getContext(), "Coupon removed", Toast.LENGTH_SHORT).show();
            } else {
                String code = etCouponCode.getText().toString().trim().toUpperCase();
                applyCouponCode(code);
            }
        });

        View chipRush50 = view.findViewById(R.id.chipCouponRush50);
        View chipHungry = view.findViewById(R.id.chipCouponHungry);
        View chipSave20 = view.findViewById(R.id.chipCouponSave20);

        if (chipRush50 != null) chipRush50.setOnClickListener(v -> applyCouponCode("RUSH50"));
        if (chipHungry != null) chipHungry.setOnClickListener(v -> applyCouponCode("HUNGRY"));
        if (chipSave20 != null) chipSave20.setOnClickListener(v -> applyCouponCode("SAVE20"));
    }

    private void applyCouponCode(String code) {
        if (code.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a coupon code", Toast.LENGTH_SHORT).show();
            return;
        }

        com.foodrush.models.Coupon coupon = couponMap.get(code);
        if (coupon == null) {
            Toast.makeText(getContext(), "Invalid coupon code: " + code, Toast.LENGTH_SHORT).show();
            return;
        }

        double subtotal = CartManager.getInstance().getTotalPrice();
        if (subtotal < coupon.getMinOrderAmount()) {
            Toast.makeText(getContext(),
                    String.format(Locale.getDefault(), "Minimum order ₹%.0f required for %s", coupon.getMinOrderAmount(), code),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        appliedCoupon = coupon;
        etCouponCode.setText(coupon.getCode());
        etCouponCode.setEnabled(false);
        btnApplyCoupon.setText(R.string.remove);
        btnApplyCoupon.setBackgroundTintList(android.content.res.ColorStateList.valueOf(requireContext().getColor(R.color.error)));

        double discount = coupon.calculateDiscount(subtotal);
        tvCouponFeedback.setVisibility(View.VISIBLE);
        tvCouponFeedback.setText(getString(R.string.coupon_applied, discount));
        updateTotal();
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
        double discount = 0.0;
        if (appliedCoupon != null) {
            discount = appliedCoupon.calculateDiscount(subtotal);
            if (discount > 0) {
                if (layoutDiscountRow != null) layoutDiscountRow.setVisibility(View.VISIBLE);
                if (tvDiscount != null) {
                    tvDiscount.setText(String.format(Locale.getDefault(), "-₹%.2f", discount));
                }
            } else {
                if (layoutDiscountRow != null) layoutDiscountRow.setVisibility(View.GONE);
            }
        } else {
            if (layoutDiscountRow != null) layoutDiscountRow.setVisibility(View.GONE);
        }

        double taxes = Math.max(0, (subtotal - discount)) * 0.05; // 5% taxes on discounted amount
        double grandTotal = Math.max(0, subtotal - discount + taxes);

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
        double discount  = appliedCoupon != null ? appliedCoupon.calculateDiscount(subtotal) : 0.0;
        double taxes     = Math.max(0, (subtotal - discount)) * 0.05;
        double finalTotal= Math.max(0, subtotal - discount + taxes);

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
        orderMap.put("subtotal", subtotal);
        orderMap.put("discount", discount);
        if (appliedCoupon != null) {
            orderMap.put("couponCode", appliedCoupon.getCode());
        }
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
