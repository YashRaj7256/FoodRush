package com.foodrush.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class for a registered user.
 * Stored in Firebase Realtime Database under /users/{uid}
 * Includes rich profile details required for an authentic food delivery platform:
 * - Basic profile: uid, name, email, phone, avatarUrl
 * - Default address & multiple structured saved addresses
 * - Ahmedabad city / area context
 * - Wallet balance & FoodRush Coins
 * - Favorites (favorite restaurant IDs)
 * - Push notification token & registration timestamp
 */
public class User {

    private String uid;
    private String name;
    private String email;
    private String phone;
    private String avatarUrl;
    private String defaultAddress;
    private String preferredArea = "Ahmedabad";
    private double walletBalance = 0.0;
    private int foodRushCoins = 50; // Welcome reward coins
    private long createdAt = System.currentTimeMillis();
    private String fcmToken = "";
    private Map<String, Boolean> favoriteRestaurants = new HashMap<>();
    private Map<String, Address> addresses = new HashMap<>();

    public User() {}

    public User(String uid, String name, String email, String phone, String defaultAddress) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.defaultAddress = defaultAddress;
        this.createdAt = System.currentTimeMillis();
    }

    // ---- Getters ----
    public String getUid()                     { return uid; }
    public String getName()                    { return name; }
    public String getEmail()                   { return email; }
    public String getPhone()                   { return phone; }
    public String getAvatarUrl()               { return avatarUrl; }
    public String getDefaultAddress()          { return defaultAddress; }
    public String getAddress()                 { return defaultAddress; } // backward compatibility
    public String getPreferredArea()           { return preferredArea; }
    public double getWalletBalance()           { return walletBalance; }
    public int getFoodRushCoins()              { return foodRushCoins; }
    public long getCreatedAt()                 { return createdAt; }
    public String getFcmToken()                { return fcmToken; }
    public Map<String, Boolean> getFavoriteRestaurants() { return favoriteRestaurants; }
    public Map<String, Address> getAddresses() { return addresses; }

    // ---- Setters ----
    public void setUid(String uid)                                     { this.uid = uid; }
    public void setName(String name)                                   { this.name = name; }
    public void setEmail(String email)                                 { this.email = email; }
    public void setPhone(String phone)                                 { this.phone = phone; }
    public void setAvatarUrl(String avatarUrl)                         { this.avatarUrl = avatarUrl; }
    public void setDefaultAddress(String defaultAddress)               { this.defaultAddress = defaultAddress; }
    public void setAddress(String address)                             { this.defaultAddress = address; } // backward compatibility
    public void setPreferredArea(String preferredArea)                 { this.preferredArea = preferredArea; }
    public void setWalletBalance(double walletBalance)                 { this.walletBalance = walletBalance; }
    public void setFoodRushCoins(int foodRushCoins)                    { this.foodRushCoins = foodRushCoins; }
    public void setCreatedAt(long createdAt)                           { this.createdAt = createdAt; }
    public void setFcmToken(String fcmToken)                           { this.fcmToken = fcmToken; }
    public void setFavoriteRestaurants(Map<String, Boolean> favorites) { this.favoriteRestaurants = favorites; }
    public void setAddresses(Map<String, Address> addresses)           { this.addresses = addresses; }
}
