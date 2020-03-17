package com.selvaraj.buyerapp.model;

public class ProfileItems {
    private int resPath;
    private String itemName;

    public ProfileItems() {
    }

    public ProfileItems(int resPath, String itemName) {
        this.resPath = resPath;
        this.itemName = itemName;
    }

    public int getResPath() {
        return resPath;
    }

    public String getItemName() {
        return itemName;
    }
}
