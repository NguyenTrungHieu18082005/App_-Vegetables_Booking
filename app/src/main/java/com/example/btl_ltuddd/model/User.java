package com.example.btl_ltuddd.model;

public class User {

    private int id;
    private String fullname;
    private String phone;
    private String email;
    private String password;

    public User() {}

    // Constructor dùng khi đăng ký
    public User(String fullname, String phone, String email, String password) {
        this.fullname = fullname;
        this.phone    = phone;
        this.email    = email;
        this.password = password;
    }

    // Constructor đầy đủ (load từ DB)
    public User(int id, String fullname, String phone, String email, String password) {
        this.id       = id;
        this.fullname = fullname;
        this.phone    = phone;
        this.email    = email;
        this.password = password;
    }

    // Getters
    public int    getId()       { return id; }
    public String getFullname() { return fullname; }
    public String getPhone()    { return phone; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }

    // Setters
    public void setId(int id)             { this.id = id; }
    public void setEmail(String v)        { this.email = v; }
    public void setPassword(String v)     { this.password = v; }
}