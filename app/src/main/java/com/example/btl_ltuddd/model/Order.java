package com.example.btl_ltuddd.model;

public class Order {

    private int id;
    private String orderCode;
    private int userId;
    private String customerName;
    private String email;
    private String phone;
    private double subtotal;
    private double total;
    private String status;
    private String createdAt;
    private String address;

    public Order() {}

    // Constructor dùng khi tạo đơn hàng mới (client)
    public Order(int userId, String customerName, String email,
                 String phone, double subtotal, double total) {
        this.userId = userId;
        this.customerName = customerName;
        this.email = email;
        this.phone = phone;
        this.subtotal = subtotal;
        this.total = total;
        this.status = "pending";
    }

    // Constructor dùng khi load từ DB (admin queryOrders)
    public Order(int id, String orderCode, String customerName,
                 String phone, double total, String status,
                 String createdAt, String address) {
        this.id = id;
        this.orderCode = orderCode;
        this.customerName = customerName;
        this.phone = phone;
        this.total = total;
        this.status = status;
        this.createdAt = createdAt;
        this.address = address;
    }

    // Getters
    public int getId()              { return id; }
    public String getOrderCode()    { return orderCode; }
    public int getUserId()          { return userId; }
    public String getCustomerName() { return customerName; }
    public String getEmail()        { return email; }
    public String getPhone()        { return phone; }
    public double getSubtotal()     { return subtotal; }
    public double getTotal()        { return total; }
    public String getStatus()       { return status; }
    public String getCreatedAt()    { return createdAt; }
    public String getAddress()      { return address; }
}
