package com.example.awizom.jihuzur.Model;

public class Profile

 {

    private String ID;
    private String Name;
    private String Lat;
    private String Long;
    private boolean Active;
    private String Role;
    private String MobileNo;
    private String Image;
    private String IdentityImage;
    private  boolean BusyStatus;

     public Profile(String id, String name, String lat, String longs, boolean active, String role, String mobileNo,String image,String identityImage,boolean busyStatus){
        ID=id;
        Name=name;
        Lat=lat;
        Long=longs;
        Active=active;
        Role=role;
        MobileNo= mobileNo;
        Image=image;
        IdentityImage=identityImage;
        BusyStatus=busyStatus;

    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        ID = id;
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

    public void setLong(String longs) {
        Lat = longs;
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
 }
