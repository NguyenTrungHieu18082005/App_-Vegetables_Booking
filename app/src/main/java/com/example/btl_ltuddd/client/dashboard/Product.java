package com.example.btl_ltuddd.client.dashboard;

public class Product {

    String name, price, unit, tag;
    int image;

    public Product(String name, String price, String unit, int image, String tag) {
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.image = image;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    public int getImage() {
        return image;
    }

    public String getTag() {
        return tag;
    }
}
