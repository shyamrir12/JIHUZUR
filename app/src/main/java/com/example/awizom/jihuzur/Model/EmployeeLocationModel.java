package com.example.awizom.jihuzur.Model;

public class EmployeeLocationModel {

    private String ID;
    private String Name;
    private Double Lat;
    private Double Long;
    private Boolean Active;
    private String Role;
    private String MobileNo;
    private String Image;

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

    private String IdentityImage;
    private Boolean BusyStatus;
    private String CreatedDate;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }



    public Boolean getActive() {
        return Active;
    }

    public void setActive(Boolean active) {
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getIdentityImage() {
        return IdentityImage;
    }

    public void setIdentityImage(String identityImage) {
        IdentityImage = identityImage;
    }

    public Boolean getBusyStatus() {
        return BusyStatus;
    }

    public void setBusyStatus(Boolean busyStatus) {
        BusyStatus = busyStatus;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}
