package com.foodrush.database;
import com.foodrush.models.Restaurant;
import com.foodrush.models.FoodItem;
import com.foodrush.models.Order;
import com.foodrush.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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




    private FirebaseHelper() {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        usersRef = database.getReference("users");
        foodItemsRef = database.getReference("food_items");
        ordersRef = database.getReference("orders");
        restaurantsRef = database.getReference("restaurants");
    }

    public static FirebaseHelper getInstance() {
        if (instance == null) instance = new FirebaseHelper();
        return instance;
    }

    // ---- Getters for references ----
    public DatabaseReference getUsersRef()     { return usersRef; }
    public DatabaseReference getRestaurantsRef() {
        return restaurantsRef;
    }
    public DatabaseReference getFoodItemsRef() { return foodItemsRef; }
    public DatabaseReference getOrdersRef()    { return ordersRef; }
    public FirebaseAuth getAuth()              { return auth; }

    /** Save a user profile to the database */
    public void saveUser(User user) {
        usersRef.child(user.getUid()).setValue(user);
    }

    /** Place an order in the database under /orders/{userId}/{orderId} */
    public void placeOrder(Order order) {
        ordersRef.child(order.getUserId())
                 .child(order.getOrderId())
                 .setValue(order);
    }

    /**
     * Seed sample food items into Firebase.
     * Call this once from your app (e.g., admin screen) or manually in Firebase console.
     */
    public void seedSampleData() {
        String[] names = {
            "Classic Burger", "Pepperoni Pizza", "Veg Biryani",
            "Paneer Tikka", "Pasta Alfredo", "Masala Dosa",
            "Cold Coffee", "Mango Lassi", "French Fries", "Chocolate Brownie"
        };
        String[] descriptions = {
            "Juicy beef patty with fresh veggies",
            "Loaded with pepperoni and cheese",
            "Fragrant basmati with mixed vegetables",
            "Tender paneer in spiced marinade",
            "Creamy Alfredo with penne",
            "Crispy dosa with sambar and chutney",
            "Rich cold coffee with ice cream",
            "Sweet mango blended with yogurt",
            "Golden crispy fries with dips",
            "Warm gooey chocolate brownie"
        };
        double[] prices = {149, 249, 179, 199, 229, 89, 99, 79, 69, 119};
        String[] categories = {
            "Burger","Pizza","Rice","Starters","Pasta",
            "South Indian","Drinks","Drinks","Snacks","Desserts"
        };

        for (int i = 0; i < names.length; i++) {
            String id = "item_" + (i + 1);
            FoodItem item = new FoodItem(
                id, names[i], descriptions[i], prices[i],
                "https://via.placeholder.com/300x200?text=" + names[i].replace(" ", "+"),
                categories[i]
            );
            foodItemsRef.child(id).setValue(item);
        }
    }
}
