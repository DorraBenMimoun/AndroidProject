package com.example.ecommerce4you.Domain;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private String orderId;
    private String userEmail;
    private String userId;
    private List<ItemsModel> items;
    private double subtotal;
    private double tax;
    private double tva;
    private double deliveryFee;
    private double total;
    private long timestamp;
    private String adress;
    private String phone;
    private boolean confirmed;


    public OrderModel(String orderId, String userId,String userEmail, List<ItemsModel> items, double subtotal, double tax,double tva, double deliveryFee, double total, long timestamp, String adress, String phone,boolean confirmed) {
        this.orderId = orderId;
        this.userId = userId;
        this.userEmail=userEmail;
        this.items = items;
        this.subtotal = subtotal;
        this.tax = tax;
        this.deliveryFee = deliveryFee;
        this.total = total;
        this.timestamp = timestamp;
        this.adress = adress;
        this.phone = phone;
        this.tva =tva;
        this.confirmed=confirmed;
    }

    public OrderModel() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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


