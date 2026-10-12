package com.foodrush.models;

/**
 * Model class for saved user delivery addresses (e.g. Home, Work, Hotel).
 * Stored under /users/{uid}/addresses/{addressId}
 */
public class Address {

    private String id;
    private String label;        // e.g. "Home", "Work", "Other"
    private String fullAddress;  // e.g. "Flat 402, Shivalik Highstreet, Vastrapur"
    private String landmark;     // e.g. "Near Vastrapur Lake"
    private String city;         // e.g. "Ahmedabad"
    private String pincode;      // e.g. "380015"
    private double latitude;
    private double longitude;
    private boolean isDefault;

    public Address() {}

    public Address(String id, String label, String fullAddress, String landmark,
                   String city, String pincode, double latitude, double longitude, boolean isDefault) {
        this.id = id;
        this.label = label;
        this.fullAddress = fullAddress;
        this.landmark = landmark;
        this.city = city;
        this.pincode = pincode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isDefault = isDefault;
    }

    public String getId() { return id; }
    public String getLabel() { return label; }
    public String getFullAddress() { return fullAddress; }
    public String getLandmark() { return landmark; }
    public String getCity() { return city; }
    public String getPincode() { return pincode; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public boolean isDefault() { return isDefault; }

    public void setId(String id) { this.id = id; }
    public void setLabel(String label) { this.label = label; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }
    public void setLandmark(String landmark) { this.landmark = landmark; }
    public void setCity(String city) { this.city = city; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }
}
