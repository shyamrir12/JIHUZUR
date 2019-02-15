package com.example.awizom.jihuzur.Model;

import java.util.Date;

public class Complaint {
    public int ComplaintID;
    public String CustomerID;
    public String Complaint;

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

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getComplaintDate() {
        return ComplaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        ComplaintDate = complaintDate;
    }

    public int getTotalReply() {
        return TotalReply;
    }

    public void setTotalReply(int totalReply) {
        TotalReply = totalReply;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean Active;
    public boolean Status;
    public String ComplaintDate;
    public int TotalReply;
    public String Name;
}
