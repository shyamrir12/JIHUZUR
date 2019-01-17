package com.example.awizom.jihuzur.Model;

import java.util.Date;

public class Rating {

    private int RateID;
    private String CustomerID;
    private int Rating;
    private int OrderID;
    private boolean Active;
    private Date RateDate;

    public int getRateID() {
        return RateID;
    }

    public void setRateID(int rateID) {
        RateID = rateID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public Date getRateDate() {
        return RateDate;
    }

    public void setRateDate(Date rateDate) {
        RateDate = rateDate;
    }
}
