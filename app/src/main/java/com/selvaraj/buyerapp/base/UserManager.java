package com.selvaraj.buyerapp.base;

import android.location.Location;

import com.selvaraj.buyerapp.model.Products;
import com.selvaraj.buyerapp.model.SaveUser;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<String> userList = new ArrayList<>();
    private String userEmail, userPassword;
    private String lastKnownLocation;
    private List<String> farmerIdList=new ArrayList<>();
    private Products selectedProduct;
    private String userName;
    private SaveUser authUser;
    private Location vendorLocation;

    public Location getVendorLocation() {
        return vendorLocation;
    }

    public void setVendorLocation(Location vendorLocation) {
        this.vendorLocation = vendorLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getFarmerIdList() {
        return farmerIdList;
    }

    public void setFarmerIdList(List<String> farmerIdList) {
        this.farmerIdList = farmerIdList;
    }

    public Products getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Products selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public String getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(String lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public SaveUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(SaveUser authUser) {
        this.authUser = authUser;
    }

    public List<String> getUserList() {
        return userList;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
