package com.foodrush.models;

import java.util.List;

/**
 * Enhanced model class representing a placed order in FoodRush.
 * Stored in Firebase Realtime Database under:
 * - /orders/{userId}/{orderId} (User history)
 * - /all_orders/{orderId} (Live kitchen & delivery dispatch)
 */
public class Order {

    private String orderId;
    private String userId;
    private String userName;
    private String userPhone;
    private String restaurantId;
    private String restaurantName;
    private List<CartItem> items;
    private double subtotal;
    private double deliveryFee = 35.0;
    private double platformFee = 5.0;
    private double discountAmount = 0.0;
    private String appliedCoupon = "";
    private double totalAmount;
    private String status;        // "Placed", "Preparing", "On the Way", "Delivered", "Cancelled"
    private String paymentMethod; // "Cash on Delivery", "UPI", "Credit/Debit Card", "FoodRush Wallet"
    private boolean paymentCompleted = false;
    private String deliveryAddress;
    private String deliveryInstructions = "";
    private String deliveryPartnerName = "Ramesh Kumar";
    private String deliveryPartnerPhone = "+91 98765 43210";
    private long timestamp;

    // Firebase requires empty constructor
    public Order() {}

    public Order(String orderId, String userId, List<CartItem> items,
                 double totalAmount, String status, long timestamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.timestamp = timestamp;
    }

    // ---- Getters ----
    public String getOrderId()              { return orderId; }
    public String getUserId()               { return userId; }
    public String getUserName()             { return userName; }
    public String getUserPhone()            { return userPhone; }
    public String getRestaurantId()         { return restaurantId; }
    public String getRestaurantName()       { return restaurantName; }
    public List<CartItem> getItems()        { return items; }
    public double getSubtotal()             { return subtotal; }
    public double getDeliveryFee()          { return deliveryFee; }
    public double getPlatformFee()          { return platformFee; }
    public double getDiscountAmount()       { return discountAmount; }
    public String getAppliedCoupon()        { return appliedCoupon; }
    public double getTotalAmount()          { return totalAmount; }
    public String getStatus()               { return status; }
    public String getPaymentMethod()        { return paymentMethod; }
    public boolean isPaymentCompleted()     { return paymentCompleted; }
    public String getDeliveryAddress()      { return deliveryAddress; }
    public String getDeliveryInstructions() { return deliveryInstructions; }
    public String getDeliveryPartnerName()  { return deliveryPartnerName; }
    public String getDeliveryPartnerPhone() { return deliveryPartnerPhone; }
    public long getTimestamp()              { return timestamp; }

    // ---- Setters ----
    public void setOrderId(String orderId)                      { this.orderId = orderId; }
    public void setUserId(String userId)                        { this.userId = userId; }
    public void setUserName(String userName)                    { this.userName = userName; }
    public void setUserPhone(String userPhone)                  { this.userPhone = userPhone; }
    public void setRestaurantId(String restaurantId)            { this.restaurantId = restaurantId; }
    public void setRestaurantName(String restaurantName)        { this.restaurantName = restaurantName; }
    public void setItems(List<CartItem> items)                  { this.items = items; }
    public void setSubtotal(double subtotal)                    { this.subtotal = subtotal; }
    public void setDeliveryFee(double deliveryFee)              { this.deliveryFee = deliveryFee; }
    public void setPlatformFee(double platformFee)              { this.platformFee = platformFee; }
    public void setDiscountAmount(double discountAmount)        { this.discountAmount = discountAmount; }
    public void setAppliedCoupon(String appliedCoupon)          { this.appliedCoupon = appliedCoupon; }
    public void setTotalAmount(double totalAmount)              { this.totalAmount = totalAmount; }
    public void setStatus(String status)                        { this.status = status; }
    public void setPaymentMethod(String paymentMethod)          { this.paymentMethod = paymentMethod; }
    public void setPaymentCompleted(boolean paymentCompleted)   { this.paymentCompleted = paymentCompleted; }
    public void setDeliveryAddress(String deliveryAddress)      { this.deliveryAddress = deliveryAddress; }
    public void setDeliveryInstructions(String instructions)    { this.deliveryInstructions = instructions; }
    public void setDeliveryPartnerName(String partnerName)      { this.deliveryPartnerName = partnerName; }
    public void setDeliveryPartnerPhone(String partnerPhone)    { this.deliveryPartnerPhone = partnerPhone; }
    public void setTimestamp(long timestamp)                    { this.timestamp = timestamp; }
}
