package com.example.btl_ltuddd.model;

public class Cart {

    private int id;

    private int userId;

    private int productId;

    private String productName;

    private double productPrice;

    private String productImage;

    private String productUnit;

    private int quantity;

    // Constructor rỗng
    public Cart() {
    }

    // Constructor đầy đủ
    public Cart(
            int id,
            int userId,
            int productId,
            String productName,
            double productPrice,
            String productImage,
            String productUnit,
            int quantity
    ) {

        this.id = id;

        this.userId = userId;

        this.productId = productId;

        this.productName = productName;

        this.productPrice = productPrice;

        this.productImage = productImage;

        this.productUnit = productUnit;

        this.quantity = quantity;
    }

    // Getter Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}