package com.foodrush.models;

public class Restaurant {

    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private int image;
    private String category;
    private double rating = 4.3;
    private String deliveryTime = "25-30 mins";
    private String area = "Ahmedabad";

    // Required empty constructor for Firebase
    public Restaurant() {}

    public Restaurant(String name, String description, int image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public Restaurant(String id, String name, String category, String area, String description, double rating, String deliveryTime, String imageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.area = area;
        this.description = description;
        this.rating = rating;
        this.deliveryTime = deliveryTime;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public int getImage() { return image; }
    public String getCategory() { return category; }
    public double getRating() { return rating; }
    public String getDeliveryTime() { return deliveryTime; }
    public String getArea() { return area; }

    // Setters
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setImage(int image) { this.image = image; }
    public void setCategory(String category) { this.category = category; }
    public void setRating(double rating) { this.rating = rating; }
    public void setDeliveryTime(String deliveryTime) { this.deliveryTime = deliveryTime; }
    public void setArea(String area) { this.area = area; }
}