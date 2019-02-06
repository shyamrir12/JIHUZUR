package com.example.awizom.jihuzur.Model;

import java.text.DateFormat;
import java.util.Date;

public class ComplaintCustomer {

    public int CReplyID;
    public String CReply;

    public int getCReplyID() {
        return CReplyID;
    }

    public void setCReplyID(int CReplyID) {
        this.CReplyID = CReplyID;
    }

    public String getCReply() {
        return CReply;
    }

    public void setCReply(String CReply) {
        this.CReply = CReply;
    }

    public int getComplaintID() {
        return ComplaintID;
    }

    public void setComplaintID(int complaintID) {
        ComplaintID = complaintID;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public String getCReplyDate() {
        return CReplyDate;
    }

    public void setCReplyDate(String CReplyDate) {
        this.CReplyDate = CReplyDate;
    }

    public int ComplaintID;
    public boolean Active;
    public String CReplyDate;
}