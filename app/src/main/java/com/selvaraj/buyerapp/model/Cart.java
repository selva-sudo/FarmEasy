package com.selvaraj.buyerapp.model;

public class Cart {
    private String buyerName;
    private String productName;
    private String productQuantity;
    private String totalPrice;
    private String dateOfBuying;

    public Cart() {
    }

    public Cart(String buyerName, String productName, String productQuantity, String totalPrice, String dateOfBuying) {
        this.buyerName = buyerName;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.totalPrice = totalPrice;
        this.dateOfBuying = dateOfBuying;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDateOfBuying() {
        return dateOfBuying;
    }

    public void setDateOfBuying(String dateOfBuying) {
        this.dateOfBuying = dateOfBuying;
    }
}
