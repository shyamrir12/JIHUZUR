package com.example.awizom.jihuzur.Model;

public class Discount {

    private int DiscountID;
    private String DiscountName;
    private String DiscountType;
    private double Discount;

    public int getDiscountID() {
        return DiscountID;
    }

    public void setDiscountID(int discountID) {
        DiscountID = discountID;
    }

    public String getDiscountName() {
        return DiscountName;
    }

    public void setDiscountName(String discountName) {
        DiscountName = discountName;
    }

    public String getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(String discountType) {
        DiscountType = discountType;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }
}
