package com.foodrush.models;

/**
 * Model class representing one item inside the shopping cart.
 * Wraps a FoodItem with an additional quantity field.
 */
public class CartItem {

    private FoodItem foodItem;
    private int quantity;

    public CartItem() {}

    public CartItem(FoodItem foodItem, int quantity) {
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    public FoodItem getFoodItem() { return foodItem; }
    public int getQuantity()      { return quantity; }

    public void setFoodItem(FoodItem foodItem) { this.foodItem = foodItem; }
    public void setQuantity(int quantity)      { this.quantity = quantity; }

    /** Convenience method: total price for this cart line */
    public double getTotalPrice() {
        return foodItem.getPrice() * quantity;
    }
}
