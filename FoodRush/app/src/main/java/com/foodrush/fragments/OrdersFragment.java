package com.foodrush.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodrush.R;
import com.foodrush.adapters.OrderAdapter;
import com.foodrush.database.FirebaseHelper;
import com.foodrush.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * OrdersFragment — displays the current user's order history, newest first.
 */
public class OrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private TextView tvEmpty;
    private View progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvEmpty      = view.findViewById(R.id.tvEmpty);
        progressBar  = view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadOrders();
    }

    private void loadOrders() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(R.string.login_to_see_orders);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        FirebaseHelper.getInstance().getOrdersRef()
            .child(user.getUid())
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Order> orders = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Order order = ds.getValue(Order.class);
                        if (order != null) orders.add(order);
                    }

                    // Show newest orders first
                    Collections.sort(orders, (a, b) ->
                            Long.compare(b.getTimestamp(), a.getTimestamp()));

                    progressBar.setVisibility(View.GONE);

                    if (orders.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        orderAdapter = new OrderAdapter(getContext(), orders);
                        recyclerView.setAdapter(orderAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText(R.string.error_generic);
                }
            });
    }
}
