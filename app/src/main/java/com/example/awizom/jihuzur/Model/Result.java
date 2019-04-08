package com.example.awizom.jihuzur.Model;

import com.google.gson.annotations.SerializedName;

import java.security.PublicKey;

public class Result {
    @SerializedName("Status")
    public Boolean Status;

    @SerializedName("Message")
    public String Message;

    @SerializedName("Image")
    public String Image;

    @SerializedName("ID")
    public String ID;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Email")
    public String Email;

    @SerializedName("Address")
    public String Address;








    public Result(String Message, Boolean Status,String Image,String ID,String Name,String Email,String Address) {

        this.Message = Message;
        this.Status = Status;
        this.Image = Image;
        this.ID = ID;
        this.Name = Name;
        this.Email = Email;
        this.Address = Address;

    }

    public Boolean getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
