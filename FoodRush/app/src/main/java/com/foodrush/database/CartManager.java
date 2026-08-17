package com.foodrush.database;

import com.foodrush.models.CartItem;
import com.foodrush.models.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // 🔥 SAFE ADD ITEM
    public void addItem(FoodItem foodItem) {

        if (foodItem == null || foodItem.getId() == null) return;

        for (CartItem cartItem : cartItems) {

            if (cartItem.getFoodItem() != null &&
                    cartItem.getFoodItem().getId() != null &&
                    cartItem.getFoodItem().getId().equals(foodItem.getId())) {

                cartItem.setQuantity(cartItem.getQuantity() + 1);
                return;
            }
        }

        cartItems.add(new CartItem(foodItem, 1));
    }

    // 🔥 SAFE REMOVE
    public void removeItem(FoodItem foodItem) {

        if (foodItem == null || foodItem.getId() == null) return;

        CartItem toRemove = null;

        for (CartItem cartItem : cartItems) {

            if (cartItem.getFoodItem() != null &&
                    cartItem.getFoodItem().getId() != null &&
                    cartItem.getFoodItem().getId().equals(foodItem.getId())) {

                if (cartItem.getQuantity() > 1) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                } else {
                    toRemove = cartItem;
                }
                break;
            }
        }

        if (toRemove != null) cartItems.remove(toRemove);
    }

    // 🔥 SAFE DELETE
    public void deleteItem(String foodItemId) {

        if (foodItemId == null) return;

        cartItems.removeIf(c ->
                c.getFoodItem() != null &&
                        c.getFoodItem().getId() != null &&
                        c.getFoodItem().getId().equals(foodItemId)
        );
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getTotalCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    public void clearCart() {
        cartItems.clear();
    }

    // 🔥 SAFE CHECK (THIS WAS CRASHING)
    public boolean isInCart(String foodItemId) {

        if (foodItemId == null) return false;

        for (CartItem item : cartItems) {

            if (item.getFoodItem() != null &&
                    item.getFoodItem().getId() != null &&
                    item.getFoodItem().getId().equals(foodItemId)) {

                return true;
            }
        }

        return false;
    }
}