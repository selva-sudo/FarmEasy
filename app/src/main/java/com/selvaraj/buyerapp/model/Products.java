package com.selvaraj.buyerapp.model;

import android.location.Location;

public class Products {
    private String name;
    private String vendorName;
    private String quantity;
    private String price;
    private String address;
    private float rating;
    private String date;
    private double lat,lng;

    public Products() {
    }

    public Products(String name, String vendorName, String quantity, String price, String address, float rating, String date, double lat, double lng) {
        this.name = name;
        this.vendorName = vendorName;
        this.quantity = quantity;
        this.price = price;
        this.address = address;
        this.rating = rating;
        this.date = date;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public float getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
