package com.example.awizom.jihuzur.Model;

import java.util.Date;

public class Reply {
    private int ReplyID;
    private String Reply;
    private int ReviewID;
    private boolean Active;
    private String ReplyDate;


    public int getReplyID() {
        return ReplyID;
    }

    public void setReplyID(int replyID) {
        ReplyID = replyID;
    }

    public String getReply() {
        return Reply;
    }

    public void setReply(String reply) {
        Reply = reply;
    }

    public int getReviewID() {
        return ReviewID;
    }

    public void setReviewID(int reviewID) {
        ReviewID = reviewID;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public String getReplyDate() {
        return ReplyDate;
    }

    public void setReplyDate(String replyDate) {
        ReplyDate = replyDate;
    }
}
