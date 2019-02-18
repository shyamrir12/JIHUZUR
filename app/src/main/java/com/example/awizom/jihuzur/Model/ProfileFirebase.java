package com.example.awizom.jihuzur.Model;

public class ProfileFirebase {
    private  String id;
    private double lat;
    private double lang;
    private boolean busystatus;

    public ProfileFirebase() {
    }

    public ProfileFirebase(String id, double lat, double lang, boolean busystatus) {
        this.id = id;
        this.lat = lat;
        this.lang = lang;
        this.busystatus = busystatus;
    }

    public String getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLang() {
        return lang;
    }

    public boolean isBusystatus() {
        return busystatus;
    }
}
