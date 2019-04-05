package com.example.awizom.jihuzur.Model;

public class DiscountView {

    public int DiscountID ;
    public String DiscountName;
    public String DiscountType;
    public Double Discount;

    public Double getDiscount() {
        return Discount;
    }

    public void setDiscount(Double discount) {
        Discount = discount;
    }

    public String Photo ;
    public String Category ;
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



    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }



}
