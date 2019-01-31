package com.example.awizom.jihuzur.Model;

public class Pricing {


private int PricingID;
private String Description;
private String PricingTerms;
private Double Amount;
private int CatalogID;

    public double getPricingStart() {
        return PricingStart;
    }

    public void setPricingStart(double pricingStart) {
        PricingStart = pricingStart;
    }

    public double getPricingEnd() {
        return PricingEnd;
    }

    public void setPricingEnd(double pricingEnd) {
        PricingEnd = pricingEnd;
    }

    public double getPricingEndSlot() {
        return PricingEndSlot;
    }

    public void setPricingEndSlot(double pricingEndSlot) {
        PricingEndSlot = pricingEndSlot;
    }

    private double PricingStart;
    private double PricingEnd ;
    private double PricingEndSlot;

    public int getPricingID() {
        return PricingID;
    }

    public void setPricingID(int pricingID) {
        PricingID = pricingID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPricingTerms() {
        return PricingTerms;
    }

    public void setPricingTerms(String pricingTerms) {
        PricingTerms = pricingTerms;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public int getCatalogID() {
        return CatalogID;
    }

    public void setCatalogID(int catalogID) {
        CatalogID = catalogID;
    }
}
