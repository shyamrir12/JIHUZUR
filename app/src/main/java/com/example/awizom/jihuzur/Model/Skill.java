package com.example.awizom.jihuzur.Model;

public class Skill {

    public int ID;
    public int CatalogId ;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCatalogId() {
        return CatalogId;
    }

    public void setCatalogId(int catalogId) {
        CatalogId = catalogId;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

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

    public String EmployeeID ;
    public int ServiceID ;
    public String ServiceName;

}
