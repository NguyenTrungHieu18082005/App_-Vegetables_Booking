package com.example.btl_ltuddd.model;


public class Order {
    private int id;
    private String orderCode;
    private String customerName;
    private String customerPhone;
    private double totalAmount;
    private String status; // "pending" | "shipping" | "done" | "cancelled"
    private String createdAt;
    private String address;

    public Order() {}

    public Order(int id, String orderCode, String customerName, String customerPhone,
                 double totalAmount, String status, String createdAt, String address) {
        this.id = id;
        this.orderCode = orderCode;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.address = address;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOrderCode() { return orderCode; }
    public void setOrderCode(String orderCode) { this.orderCode = orderCode; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    /** 2 chữ cái đầu tên để hiển thị avatar */
    public String getAvatarText() {
        if (customerName == null || customerName.isEmpty()) return "?";
        String[] parts = customerName.trim().split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
        }
        return customerName.substring(0, Math.min(2, customerName.length())).toUpperCase();
    }
}