package com.example.awizom.jihuzur.Model;

import java.util.Date;

public class Complaint {
    public int ComplaintID ;

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

    public Object getComplaintDate() {
        return ComplaintDate;
    }

    public void setComplaintDate(Object complaintDate) {
        ComplaintDate = complaintDate;
    }

    public int getTotalReply() {
        return TotalReply;
    }

    public void setTotalReply(int totalReply) {
        TotalReply = totalReply;
    }

    public String CustomerID ;
    public String Complaint ;
    public boolean Active;
    public String Status ;
    public Object ComplaintDate;
    public int TotalReply;
}
