package com.foodrush.models;

/**
 * Model class for a registered user.
 * Stored in Firebase Realtime Database under /users/{uid}
 */
public class User {

    private String uid;
    private String name;
    private String email;
    private String phone;
    private String address;

    public User() {}

    public User(String uid, String name, String email, String phone, String address) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getUid()     { return uid; }
    public String getName()    { return name; }
    public String getEmail()   { return email; }
    public String getPhone()   { return phone; }
    public String getAddress() { return address; }

    public void setUid(String uid)         { this.uid = uid; }
    public void setName(String name)       { this.name = name; }
    public void setEmail(String email)     { this.email = email; }
    public void setPhone(String phone)     { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
}
