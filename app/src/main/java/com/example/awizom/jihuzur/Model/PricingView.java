package com.example.awizom.jihuzur.Model;

public class PricingView {

    public int CatalogID;
    public String CatalogName;
    public int ServiceID;
    public String ServiceName ;
    public String ServiceDesc ;
    public int PricingID;
    public String PrizingDesc;
    public String PricingTerms;
    public double Amount ;
    public double PricingStart;
    public double PricingEnd ;
    public double PricingEndSlot;
    public double StartAmount ;
    public String DisplayType;
    public int Quantity;
    public String Time;
    private boolean isSelected;

    public int getCatalogID() {
        return CatalogID;
    }

    public void setCatalogID(int catalogID) {
        CatalogID = catalogID;
    }

    public String getCatalogName() {
        return CatalogName;
    }

    public void setCatalogName(String catalogName) {
        CatalogName = catalogName;
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

    public int getPricingID() {
        return PricingID;
    }

    public void setPricingID(int pricingID) {
        PricingID = pricingID;
    }

    public String getPrizingDesc() {
        return PrizingDesc;
    }

    public void setPrizingDesc(String prizingDesc) {
        PrizingDesc = prizingDesc;
    }

    public String getPricingTerms() {
        return PricingTerms;
    }

    public void setPricingTerms(String pricingTerms) {
        PricingTerms = pricingTerms;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

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

    public double getStartAmount() {
        return StartAmount;
    }

    public void setStartAmount(double startAmount) {
        StartAmount = startAmount;
    }

    public String getDisplayType() {
        return DisplayType;
    }

    public void setDisplayType(String displayType) {
        DisplayType = displayType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
