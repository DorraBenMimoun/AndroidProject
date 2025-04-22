package com.example.ecommerce4you.Domain;

import java.io.Serializable;

public class CartModel implements Serializable {
    // ⚠️ On ne stocke pas itemId dans Firebase mais on l'utilise localement
    private transient  String itemId;
    private int quantity;
    private  double price;
    private String userId;

    public CartModel(String itemId, int quantity, String userId,double price) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.userId = userId;
        this.price=price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CartModel() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String productId) {
        this.itemId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
