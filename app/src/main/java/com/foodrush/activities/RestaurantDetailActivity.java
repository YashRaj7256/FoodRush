package com.foodrush.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodrush.R;
import com.foodrush.adapters.FoodAdapter;
import com.foodrush.database.FirebaseHelper;
import com.foodrush.models.FoodItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity {

    ImageView image;
    TextView name, desc;

    RecyclerView foodRecycler;
    FoodAdapter foodAdapter;
    List<FoodItem> foodList = new ArrayList<>();
    View progressBar;
    TextView emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        image = findViewById(R.id.restaurantImage);
        image.setImageResource(R.drawable.ic_food_placeholder);
        name = findViewById(R.id.restaurantName);
        desc = findViewById(R.id.restaurantDesc);
        foodRecycler = findViewById(R.id.foodRecycler);
        progressBar = findViewById(R.id.progressBar);
        emptyState = findViewById(R.id.emptyState);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // 🔹 Get data
        String rName = getIntent().getStringExtra("name");
        String rDesc = getIntent().getStringExtra("desc");
        String restaurantId = getIntent().getStringExtra("restaurantId");

        // 🔹 Set text
        if (rName != null) name.setText(rName);
        if (rDesc != null) desc.setText(rDesc);

        // 🔹 Recycler setup
        foodRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        foodAdapter = new FoodAdapter(this, foodList);
        foodRecycler.setAdapter(foodAdapter);

        // 🔥 LOAD FOOD FROM FIREBASE (FIXED)
        if (restaurantId != null && !restaurantId.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);

            FirebaseHelper.getInstance()
                    .getRestaurantsRef()
                    .child(restaurantId)
                    .child("foods")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            foodList.clear();

                            for (DataSnapshot ds : snapshot.getChildren()) {

                                FoodItem item = ds.getValue(FoodItem.class);

                                if (item != null) {

                                    // 🔥 CRITICAL FIX (THIS WAS MISSING)
                                    item.setId(ds.getKey());

                                    foodList.add(item);
                                }
                            }

                            foodAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            emptyState.setVisibility(foodList.isEmpty() ? View.VISIBLE : View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                            emptyState.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            emptyState.setVisibility(View.VISIBLE);
        }

        // 🔥 LOAD IMAGE FROM FIREBASE
        if (restaurantId != null) {
            FirebaseHelper.getInstance()
                    .getRestaurantsRef()
                    .child(restaurantId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                            if (imageUrl != null) {
                                Glide.with(RestaurantDetailActivity.this)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.ic_food_placeholder)
                                        .into(image);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
        }
    }
}
