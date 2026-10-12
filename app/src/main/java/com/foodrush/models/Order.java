package com.foodrush.models;

import java.util.List;

/**
 * Model class representing a placed order.
 * Stored in Firebase Realtime Database under /orders/{userId}/{orderId}
 */
public class Order {

    private String orderId;
    private String userId;
    private List<CartItem> items;
    private double totalAmount;
    private String status;        // "Placed", "Preparing", "On the Way", "Delivered"
    private String paymentMethod; // "Cash on Delivery", "UPI", "Card"
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
    public String getOrderId()        { return orderId; }
    public String getUserId()         { return userId; }
    public List<CartItem> getItems()  { return items; }
    public double getTotalAmount()    { return totalAmount; }
    public String getStatus()         { return status; }
    public String getPaymentMethod()  { return paymentMethod; }
    public long getTimestamp()        { return timestamp; }

    // ---- Setters ----
    public void setOrderId(String orderId)          { this.orderId = orderId; }
    public void setUserId(String userId)            { this.userId = userId; }
    public void setItems(List<CartItem> items)      { this.items = items; }
    public void setTotalAmount(double totalAmount)  { this.totalAmount = totalAmount; }
    public void setStatus(String status)            { this.status = status; }
    public void setPaymentMethod(String p)          { this.paymentMethod = p; }
    public void setTimestamp(long timestamp)        { this.timestamp = timestamp; }
}
