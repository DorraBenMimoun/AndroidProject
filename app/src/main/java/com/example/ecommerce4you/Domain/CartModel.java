package com.example.ecommerce4you.Domain;

import java.io.Serializable;

public class CartModel implements Serializable {
    private String itemId;
    private int quantity;

    public CartModel() {}

    public CartModel(String itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    // getters et setters
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
