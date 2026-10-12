package com.foodrush.models;

public class Coupon {
    private String code;
    private String description;
    private double discountPercent; // e.g., 50 for 50%
    private double maxDiscount;     // e.g., 100 for max Rs 100
    private double minOrderAmount;   // e.g., 199 min order
    private boolean active = true;

    // Required empty constructor for Firebase Realtime Database
    public Coupon() {}

    public Coupon(String code, String description, double discountPercent, double maxDiscount, double minOrderAmount) {
        this.code = code;
        this.description = description;
        this.discountPercent = discountPercent;
        this.maxDiscount = maxDiscount;
        this.minOrderAmount = minOrderAmount;
        this.active = true;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public double getMinOrderAmount() {
        return minOrderAmount;
    }

    public boolean isActive() {
        return active;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public void setMinOrderAmount(double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double calculateDiscount(double subtotal) {
        if (!active || subtotal < minOrderAmount) {
            return 0.0;
        }
        double discount = (subtotal * discountPercent) / 100.0;
        if (maxDiscount > 0 && discount > maxDiscount) {
            discount = maxDiscount;
        }
        return discount;
    }
}
