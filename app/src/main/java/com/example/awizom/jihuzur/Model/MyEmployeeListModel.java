package com.example.awizom.jihuzur.Model;

public class MyEmployeeListModel {

    public String ID;
    public String Name;
    public String Lat;
    public String Long;

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

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
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

    public boolean isBusyStatus() {
        return BusyStatus;
    }

    public void setBusyStatus(boolean busyStatus) {
        BusyStatus = busyStatus;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public boolean Active;
    public String Role;
    public String MobileNo;
    public String Image;
    public String IdentityImage;
    public boolean BusyStatus;
    public String CreatedDate;


}
