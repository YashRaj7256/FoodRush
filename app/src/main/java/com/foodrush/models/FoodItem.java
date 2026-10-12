package com.foodrush.models;

/**
 * Model class representing a single food food item in the app.
 * Used for RecyclerView display and Firebase data mapping.
 */
public class FoodItem {

    private String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;   // e.g. "Burger", "Pizza", "Drinks"
    private boolean available;
    private boolean isVeg = true; // default true

    // Required empty constructor for Firebase deserialization
    public FoodItem() {}

    /** Full constructor for Firebase and API items */
    public FoodItem(String id, String name, String description,
                    double price, String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.available = true;
    }

    /** 
     * Convenience constructor for local items (e.g. in RestaurantDetailActivity)
     * Maps the 4 arguments used in the UI to the full model.
     */
    public FoodItem(String name, String description, double price, int imageResId) {
        this.id = name.toLowerCase().replace(" ", "_");
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = ""; // Glide will use the default placeholder
        this.category = "Food";
        this.available = true;
    }

    // ---- Getters ----
    public String getId()          { return id; }
    public String getName()        { return name; }
    public String getDescription() { return description; }
    public double getPrice()       { return price; }
    public String getImageUrl()    { return imageUrl; }
    public String getCategory()    { return category; }
    public boolean isAvailable()   { return available; }
    public boolean isVeg()         {
        if (name != null) {
            String lower = name.toLowerCase();
            if (lower.contains("chicken") || lower.contains("mutton") || lower.contains("fish") ||
                lower.contains("meat") || lower.contains("egg") || lower.contains("prawn") || lower.contains("beef") || lower.contains("bacon")) {
                return false;
            }
        }
        return isVeg;
    }

    // ---- Setters ----
    public void setId(String id)               { this.id = id; }
    public void setName(String name)           { this.name = name; }
    public void setDescription(String d)       { this.description = d; }
    public void setPrice(double price)         { this.price = price; }
    public void setImageUrl(String imageUrl)   { this.imageUrl = imageUrl; }
    public void setCategory(String category)   { this.category = category; }
    public void setAvailable(boolean available){ this.available = available; }
    public void setVeg(boolean veg)            { this.isVeg = veg; }
}
