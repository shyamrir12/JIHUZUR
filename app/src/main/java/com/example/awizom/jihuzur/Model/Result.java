package com.example.awizom.jihuzur.Model;

import com.google.gson.annotations.SerializedName;

import java.security.PublicKey;

public class Result {
    @SerializedName("Status")
    public Boolean Status;

    @SerializedName("Message")
    public String Message;

    @SerializedName("ImageUrl")
    public String ImageUrl;





    public Result(String Message, Boolean Status,String ImageUrl) {

        this.Message = Message;
        this.Status = Status;
        this.ImageUrl = ImageUrl;

    }

    public Boolean getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

}
