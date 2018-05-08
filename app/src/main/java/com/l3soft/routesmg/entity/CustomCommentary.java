package com.l3soft.routesmg.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ElOskar101 on 30/04/2018.
 */

public class CustomCommentary {

    @SerializedName("create_at")
    private String creteAt;
    private String title;
    private String description;
    @SerializedName("commentary_img")
    private String urlImage;
    @SerializedName("bus_id")
    private String busID;
    @SerializedName("id")
    private String commentaryID;

    public String getCreteAt() {
        return creteAt;
    }

    public void setCreteAt(String creteAt) {
        this.creteAt = creteAt;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getCommentaryID() {
        return commentaryID;
    }

    public void setCommentaryID(String commentaryID) {
        this.commentaryID = commentaryID;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }

}
