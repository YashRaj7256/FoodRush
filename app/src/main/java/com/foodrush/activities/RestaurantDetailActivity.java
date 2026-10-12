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
import com.foodrush.database.AhmedabadDataSeeder;
import com.foodrush.database.FirebaseHelper;
import com.foodrush.models.FoodItem;
import com.google.android.material.switchmaterial.SwitchMaterial;
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
    private final List<FoodItem> foodList = new ArrayList<>();
    private final List<FoodItem> rawFoodList = new ArrayList<>();
    private boolean isVegOnly = false;
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

        SwitchMaterial switchVegOnly = findViewById(R.id.switchVegOnly);
        if (switchVegOnly != null) {
            switchVegOnly.setOnCheckedChangeListener((btn, isChecked) -> {
                isVegOnly = isChecked;
                applyVegFilter();
            });
        }

        // 🔥 LOAD FOOD FROM FIREBASE
        if (restaurantId != null && !restaurantId.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);

            FirebaseHelper.getInstance()
                    .getRestaurantsRef()
                    .child(restaurantId)
                    .child("foods")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            rawFoodList.clear();

                            if (!snapshot.exists() || snapshot.getChildrenCount() == 0) {
                                // Fallback mock dishes from Ahmedabad database if Firebase hasn't synced yet
                                List<FoodItem> fallbackFoods = AhmedabadDataSeeder.getFoodsForRestaurant(restaurantId);
                                if (!fallbackFoods.isEmpty()) {
                                    rawFoodList.addAll(fallbackFoods);
                                } else {
                                    rawFoodList.add(new FoodItem("f1", "Classic Veg Burger", "Crispy spiced potato patty with lettuce & mayo", 129, "https://images.unsplash.com/photo-1550547660-d9450f859349?w=500", "Burger"));
                                    rawFoodList.add(new FoodItem("f2", "Chicken Crisp Burger", "Juicy crispy fried chicken patty with spicy sauce", 199, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500", "Burger"));
                                }
                            } else {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    FoodItem item = ds.getValue(FoodItem.class);
                                    if (item != null) {
                                        item.setId(ds.getKey());
                                        rawFoodList.add(item);
                                    }
                                }
                            }

                            applyVegFilter();
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                            if (rawFoodList.isEmpty()) {
                                List<FoodItem> fallbackFoods = AhmedabadDataSeeder.getFoodsForRestaurant(restaurantId);
                                if (!fallbackFoods.isEmpty()) {
                                    rawFoodList.addAll(fallbackFoods);
                                } else {
                                    rawFoodList.add(new FoodItem("f1", "Classic Veg Burger", "Crispy spiced potato patty with lettuce & mayo", 129, "https://images.unsplash.com/photo-1550547660-d9450f859349?w=500", "Burger"));
                                }
                                applyVegFilter();
                            } else {
                                emptyState.setVisibility(View.VISIBLE);
                            }
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

    private void applyVegFilter() {
        foodList.clear();
        for (FoodItem item : rawFoodList) {
            if (!isVegOnly || item.isVeg()) {
                foodList.add(item);
            }
        }
        foodAdapter.notifyDataSetChanged();
        emptyState.setVisibility(foodList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
