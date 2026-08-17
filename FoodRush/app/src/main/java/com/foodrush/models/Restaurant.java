package com.foodrush.models;

public class Restaurant {

    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private int image;

    // Required empty constructor for Firebase
    public Restaurant() {}

    public Restaurant(String name, String description, int image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public int getImage() { return image; }

    // Setters
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setImage(int image) { this.image = image; }
}