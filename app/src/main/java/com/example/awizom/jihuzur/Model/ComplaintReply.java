package com.example.awizom.jihuzur.Model;

import java.util.Date;

public class ComplaintReply {
    private int ComplaintID;
    private String CustomerID;
    private String Complaint;
    private int OrderID;
    private boolean Active;
    private String Status;
    private Date ComplaintDate;

    public int getComplaintID() {
        return ComplaintID;
    }

    public void setComplaintID(int complaintID) {
        ComplaintID = complaintID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getComplaint() {
        return Complaint;
    }

    public void setComplaint(String complaint) {
        Complaint = complaint;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Date getComplaintDate() {
        return ComplaintDate;
    }

    public void setComplaintDate(Date complaintDate) {
        ComplaintDate = complaintDate;
    }
}
