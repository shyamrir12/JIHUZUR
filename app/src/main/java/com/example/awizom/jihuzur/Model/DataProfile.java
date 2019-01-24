package com.example.awizom.jihuzur.Model;

import java.util.Date;
import java.util.List;

public class DataProfile {


    public String ID;
    public Object Name;
    public Object Lat;
    public Object Long;
  public boolean Active;
    public String Role;
    public String MobileNo;
    public Object Image;
    public Object IdentityImage;
    public boolean BusyStatus;
    public String CreatedDate;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Object getName() {
        return Name;
    }

    public void setName(Object name) {
        Name = name;
    }

    public Object getLat() {
        return Lat;
    }

    public void setLat(Object lat) {
        Lat = lat;
    }

    public Object getLong() {
        return Long;
    }

    public void setLong(Object aLong) {
        Long = aLong;
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

    public Object getImage() {
        return Image;
    }

    public void setImage(Object image) {
        Image = image;
    }

    public Object getIdentityImage() {
        return IdentityImage;
    }

    public void setIdentityImage(Object identityImage) {
        IdentityImage = identityImage;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
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
}
