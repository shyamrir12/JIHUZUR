package com.example.awizom.jihuzur.Model;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem{

private LatLng position;
private String title;
private  String snippet;
private int iconpicture;
private User user;

public ClusterMarker(LatLng position, String title, String snippet, int iconpicture)
{
    this.position=position;
    this.title=title;
    this.snippet=snippet;
    this.iconpicture=iconpicture;
    this.user=user;
    }

    public ClusterMarker()
    {

    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public int getIconpicture() {
        return iconpicture;
    }

    public void setIconpicture(int iconpicture) {
        this.iconpicture = iconpicture;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
