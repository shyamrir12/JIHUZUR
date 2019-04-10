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
    private int Quantity;

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    private boolean Payment;
    private String DiscountName;

    private String Name;
    private String MobileNo;
    private String EmpName;
    private String EmpMob;
    private String TotalTime;
    private int ServiceID;
    private String ServiceName;
    private String ServiceDesc;
    private String CatalogName;
    private String Category;
    private String Image;
    private String DiscountType;
    private String Discount;
    private String Amount;
    private String PricingID;
    private String PricingTerms;

    public int Quantity;
    public String Time;


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


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getEmpMob() {
        return EmpMob;
    }

    public void setEmpMob(String empMob) {
        EmpMob = empMob;
    }

    public String getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
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

    public String getServiceDesc() {
        return ServiceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        ServiceDesc = serviceDesc;
    }

    public String getCatalogName() {
        return CatalogName;
    }

    public void setCatalogName(String catalogName) {
        CatalogName = catalogName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(String discountType) {
        DiscountType = discountType;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPricingID() {
        return PricingID;
    }

    public void setPricingID(String pricingID) {
        PricingID = pricingID;
    }

    public String getPricingTerms() {
        return PricingTerms;
    }

    public void setPricingTerms(String pricingTerms) {
        PricingTerms = pricingTerms;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
