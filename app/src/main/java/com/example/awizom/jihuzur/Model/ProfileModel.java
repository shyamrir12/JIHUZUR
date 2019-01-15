package com.example.awizom.jihuzur.Model;

public class ProfileModel {
    private String Id;
    private String Name;
    private Double Lat;
    private Double Long;
    private boolean Active;
    private String Role;
    private String MobileNo;
    private String IdentityType;
    private String IdentityNo;
    private String RegistrationDate;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLong() {
        return Long;
    }

    public void setLong(Double aLong) {
        Long = aLong;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getIdentityType() {
        return IdentityType;
    }

    public void setIdentityType(String identityType) {
        IdentityType = identityType;
    }

    public String getIdentityNo() {
        return IdentityNo;
    }

    public void setIdentityNo(String identityNo) {
        IdentityNo = identityNo;
    }

    public String getRegistrationDate() {
        return RegistrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        RegistrationDate = registrationDate;
    }
}
