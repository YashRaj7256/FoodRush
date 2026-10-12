package com.foodrush.database;
import com.foodrush.models.Restaurant;
import com.foodrush.models.FoodItem;
import com.foodrush.models.Order;
import com.foodrush.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * Helper class that centralizes all Firebase Realtime Database operations.
 * Provides static-style access via singleton pattern.
 */
public class FirebaseHelper {

    private static FirebaseHelper instance;
    private final FirebaseDatabase database;
    private final FirebaseAuth auth;
    private final DatabaseReference restaurantsRef;

    // Database root references
    private final DatabaseReference usersRef;
    private final DatabaseReference foodItemsRef;
    private final DatabaseReference ordersRef;
    private final DatabaseReference allOrdersRef;
    private final DatabaseReference couponsRef;

    private FirebaseHelper() {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        usersRef = database.getReference("users");
        foodItemsRef = database.getReference("food_items");
        ordersRef = database.getReference("orders");
        allOrdersRef = database.getReference("all_orders");
        couponsRef = database.getReference("coupons");
        restaurantsRef = database.getReference("restaurants");
    }

    public static FirebaseHelper getInstance() {
        if (instance == null) instance = new FirebaseHelper();
        return instance;
    }

    // ---- Getters for references ----
    public DatabaseReference getUsersRef()       { return usersRef; }
    public DatabaseReference getRestaurantsRef() { return restaurantsRef; }
    public DatabaseReference getFoodItemsRef()   { return foodItemsRef; }
    public DatabaseReference getOrdersRef()      { return ordersRef; }
    public DatabaseReference getAllOrdersRef()   { return allOrdersRef; }
    public DatabaseReference getCouponsRef()     { return couponsRef; }
    public FirebaseAuth getAuth()                { return auth; }

    /** Save or update a user profile in the database */
    public void saveUser(User user) {
        if (user != null && user.getUid() != null) {
            usersRef.child(user.getUid()).setValue(user);
        }
    }

    /** Save an address under /users/{uid}/addresses/{addressId} */
    public void saveAddress(String uid, com.foodrush.models.Address address) {
        if (uid != null && address != null && address.getId() != null) {
            usersRef.child(uid).child("addresses").child(address.getId()).setValue(address);
        }
    }

    /** Toggle restaurant favorite */
    public void toggleFavorite(String uid, String restaurantId, boolean isFavorite) {
        if (uid != null && restaurantId != null) {
            usersRef.child(uid).child("favoriteRestaurants").child(restaurantId).setValue(isFavorite);
        }
    }

    /** Place an order under /orders/{userId}/{orderId} and /all_orders/{orderId} */
    public void placeOrder(Order order) {
        if (order == null || order.getOrderId() == null) return;
        if (order.getUserId() != null) {
            ordersRef.child(order.getUserId()).child(order.getOrderId()).setValue(order);
        }
        allOrdersRef.child(order.getOrderId()).setValue(order);
    }

    /**
     * Interface for sync progress callback
     */
    public interface DataSeedListener {
        void onSuccess(int count);
        void onError(String message);
    }

    /**
     * Seed full real Ahmedabad database of 50+ restaurants and dishes into Firebase Realtime Database
     * under /restaurants.
     */
    public void seedSampleRestaurantsAndFoods() {
        seedSampleRestaurantsAndFoods(null);
    }

    public void seedSampleRestaurantsAndFoods(final DataSeedListener listener) {
        List<AhmedabadDataSeeder.AhmedabadRestaurantEntry> entries =
                AhmedabadDataSeeder.getAhmedabadRestaurants();

        final int total = entries.size();
        final java.util.concurrent.atomic.AtomicInteger uploadedCount = new java.util.concurrent.atomic.AtomicInteger(0);

        for (AhmedabadDataSeeder.AhmedabadRestaurantEntry entry : entries) {
            Restaurant r = entry.restaurant;
            DatabaseReference rRef = restaurantsRef.child(r.getId());

            java.util.Map<String, Object> restMap = new java.util.HashMap<>();
            restMap.put("id", r.getId());
            restMap.put("name", r.getName());
            restMap.put("category", r.getCategory());
            restMap.put("area", r.getArea());
            restMap.put("description", r.getDescription());
            restMap.put("rating", r.getRating());
            restMap.put("deliveryTime", r.getDeliveryTime());
            restMap.put("imageUrl", r.getImageUrl());

            java.util.Map<String, Object> foodMap = new java.util.HashMap<>();
            for (FoodItem item : entry.foods) {
                foodMap.put(item.getId(), item);
            }
            restMap.put("foods", foodMap);

            rRef.updateChildren(restMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    int c = uploadedCount.incrementAndGet();
                    if (c == total && listener != null) {
                        listener.onSuccess(total);
                    }
                } else if (listener != null) {
                    listener.onError(task.getException() != null ? task.getException().getMessage() : "Unknown error");
                }
            });
        }
    }

    /**
     * Seed sample food items into Firebase.
     * Call this once from your app (e.g., admin screen) or manually in Firebase console.
     */
    public void seedSampleData() {
        seedSampleRestaurantsAndFoods();
    }
}
