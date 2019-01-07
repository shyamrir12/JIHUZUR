package com.example.awizom.jihuzur.Model;

public class EmployeeProfile

 {

    private String Id;
    private String Name;
    private String Lat;
    private String Long;
    private boolean Active;
    private String Role;
    private String MobileNo;
    private String IdentityType;
    private String IdentityNo;
    private String RegistrationDate;


     public EmployeeProfile(String id,String name,String lat,String longs,boolean active,String role,String mobileNo,String identityType,String identityNo,String registrationDate){
        Id=id;
        Name=name;
        Lat=lat;
        Long=longs;
        Active=active;
        Role=role;
        MobileNo= mobileNo;
        IdentityType=identityType;
        IdentityNo=identityNo;
        RegistrationDate=registrationDate;
    }

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

     public String getRegistrationDate()
              {
                  return RegistrationDate;
              }
     public void setRegistrationDate(String registrationDate)
              {
                  RegistrationDate = registrationDate;
              }

 }
