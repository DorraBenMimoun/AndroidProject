package com.example.ecommerce4you.Domain;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private String orderId;
    private String userId;
    private List<ItemsModel> items;
    private double subtotal;
    private double tax;
    private double deliveryFee;
    private double total;
    private long timestamp;

    public OrderModel(String orderId, String userId, List<ItemsModel> items, double subtotal, double tax, double deliveryFee, double total, long timestamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.subtotal = subtotal;
        this.tax = tax;
        this.deliveryFee = deliveryFee;
        this.total = total;
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ItemsModel> getItems() {
        return items;
    }

    public void setItems(List<ItemsModel> items) {
        this.items = items;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}


