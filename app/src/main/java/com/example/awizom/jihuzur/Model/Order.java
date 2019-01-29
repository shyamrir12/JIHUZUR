package com.example.awizom.jihuzur.Model;

import java.sql.Time;
import java.util.Date;

public class Order {

    private int OrderID;
    private String CustomerID;
    private String EmployeeID;
    private String OrderDate;
    private String OrderStartTime;
    private String OrderEndTime;
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

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderStartTime() {
        return OrderStartTime;
    }

    public void setOrderStartTime(String orderStartTime) {
        OrderStartTime = orderStartTime;
    }

    public String getOrderEndTime() {
        return OrderEndTime;
    }

    public void setOrderEndTime(String orderEndTime) {
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
