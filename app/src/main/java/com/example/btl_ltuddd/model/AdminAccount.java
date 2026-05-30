package com.example.btl_ltuddd.model;
public class AdminAccount {
    private int    id;
    private String fullName;
    private String staffId;
    private String email;
    private String password;

    public AdminAccount() {}

    public AdminAccount(int id, String fullName, String staffId,
                        String department, String email,
                        String password, String accessLevel) {
        this.id          = id;
        this.fullName    = fullName;
        this.staffId     = staffId;
        this.email       = email;
        this.password    = password;
    }

    // Getters
    public int    getId()          { return id; }
    public String getFullName()    { return fullName; }
    public String getStaffId()     { return staffId; }
    public String getEmail()       { return email; }
    // Setters
    public void setId(int id)                   { this.id = id; }
    public void setFullName(String v)           { this.fullName = v; }
    public void setStaffId(String v)            { this.staffId = v; }
    public void setEmail(String v)              { this.email = v; }
    public void setPassword(String v)           { this.password = v; }
}