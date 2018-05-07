package com.l3soft.routesmg.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by ElOskar101 on 12/04/2018.
 */
@Entity
public class Commentary {
    @PrimaryKey(autoGenerate = true)
    private String  id;

    @ColumnInfo()
    private String Description;

    @ColumnInfo()
    private String title;

    @ColumnInfo(name = "commentary_img")
    private int commentaryImg;

    @ColumnInfo(name = "bus_id")
    int busId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommentaryImg() {
        return commentaryImg;
    }

    public void setCommentaryImg(int commentaryImg) {
        this.commentaryImg = commentaryImg;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }
}
