package com.example.awizom.jihuzur.Model;

import java.text.DateFormat;
import java.util.Date;

public class Review {
    public int ReviewID ;
    public String Review;

    public int getReviewID() {
        return ReviewID;
    }

    public void setReviewID(int reviewID) {
        ReviewID = reviewID;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
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

    public String getReviewDate() {
        return ReviewDate;
    }

    public void setReviewDate(String reviewDate) {
        ReviewDate = reviewDate;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public int getTotalReply() {
        return TotalReply;
    }

    public void setTotalReply(int totalReply) {
        TotalReply = totalReply;
    }

    public int OrderID ;
    public boolean Active;
    public String ReviewDate;
    public int Rate;
    public int TotalReply;

}
