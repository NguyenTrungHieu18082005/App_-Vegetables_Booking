package com.example.btl_ltuddd.model;

public class Product {

    private int id;

    private String name;

    private double price;

    private String description;

    private String category;

    private String imageUrl;

    private int stock;

    private String unit;

    private boolean visible;

    // THÊM DÒNG NÀY
    private int quantity;

    public Product() {
    }

    public Product(
            int id,
            String name,
            double price,
            String description,
            String category,
            String imageUrl,
            int stock,
            String unit,
            boolean visible
    ) {

        this.id = id;

        this.name = name;

        this.price = price;

        this.description = description;

        this.category = category;

        this.imageUrl = imageUrl;

        this.stock = stock;

        this.unit = unit;

        this.visible = visible;
    }

    // ===== GET SET =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description
    ) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(
            String category
    ) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(
            String imageUrl
    ) {
        this.imageUrl = imageUrl;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(
            int stock
    ) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(
            String unit
    ) {
        this.unit = unit;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(
            boolean visible
    ) {
        this.visible = visible;
    }

    // ===== THÊM 2 HÀM NÀY =====

    public int getQuantity() {

        return quantity;

    }

    public void setQuantity(
            int quantity
    ) {

        this.quantity =
                quantity;

    }

}