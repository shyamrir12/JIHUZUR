package com.example.awizom.jihuzur.Model;

public class DiscountModel {

    public Integer DiscountID;
    public String  DiscountName;
    public String DiscountType;
    public String Discount;
    public String Photo;
    public Object Category;

    public Integer getDiscountID() {
        return DiscountID;
    }

    public void setDiscountID(Integer discountID) {
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

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public Object getCategory() {
        return Category;
    }

    public void setCategory(Object category) {
        Category = category;
    }
}
