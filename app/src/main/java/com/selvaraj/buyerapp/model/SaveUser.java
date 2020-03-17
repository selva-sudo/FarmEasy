package com.selvaraj.buyerapp.model;

import android.location.Location;

public class SaveUser {

    private String email;

    private String name;

    private String phoneNumber;

    private String address;

    private String dateOfJoining;

    private double lat,lng;

    public SaveUser() {
    }

    public SaveUser(String email, String name, String phoneNumber, String address, String dateOfJoining, double lat, double lng) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfJoining = dateOfJoining;
        this.lat = lat;
        this.lng = lng;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
