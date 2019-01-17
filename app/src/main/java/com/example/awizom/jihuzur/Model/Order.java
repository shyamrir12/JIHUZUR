package com.example.awizom.jihuzur.Model;

import java.sql.Time;
import java.util.Date;

public class Order {

    private int OrderID;
    private String CustomerID;
    private String EmployeeID;
    private Date OrderDate;
    private Time OrderStartTime;
    private Time OrderEndTime;
    private int CatalogID;
    private boolean Payment;
    private String DiscountName;


    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(Date orderDate) {
        OrderDate = orderDate;
    }

    public Time getOrderStartTime() {
        return OrderStartTime;
    }

    public void setOrderStartTime(Time orderStartTime) {
        OrderStartTime = orderStartTime;
    }

    public Time getOrderEndTime() {
        return OrderEndTime;
    }

    public void setOrderEndTime(Time orderEndTime) {
        OrderEndTime = orderEndTime;
    }

    public int getCatalogID() {
        return CatalogID;
    }

    public void setCatalogID(int catalogID) {
        CatalogID = catalogID;
    }

    public boolean isPayment() {
        return Payment;
    }

    public void setPayment(boolean payment) {
        Payment = payment;
    }

    public String getDiscountName() {
        return DiscountName;
    }

    public void setDiscountName(String discountName) {
        DiscountName = discountName;
    }
}
