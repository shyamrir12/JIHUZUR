package com.example.awizom.jihuzur.Model;

public class Service {
    private int ServiceID ;
    private String ServiceName ;
    private String Description;
    private int CatalogID ;
    private String DisplayType;

    public int getServiceID() {
        return ServiceID;
    }

    public void setServiceID(int serviceID) {
        ServiceID = serviceID;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getCatalogID() {
        return CatalogID;
    }

    public void setCatalogID(int catalogID) {
        CatalogID = catalogID;
    }

    public String getDisplayType() {
        return DisplayType;
    }

    public void setDisplayType(String displayType) {
        DisplayType = displayType;
    }
}



