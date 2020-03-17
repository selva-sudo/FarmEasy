package com.selvaraj.buyerapp.model;

import android.location.Location;

public class Vendor {

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;

    private String dateOfJoining;

    private Location userLocation;

    public Vendor() {
    }

    public Vendor(String email, String firstName, String lastName, String phoneNumber, String address, String dateOfJoining, Location location) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfJoining = dateOfJoining;
        this.userLocation = location;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
}
