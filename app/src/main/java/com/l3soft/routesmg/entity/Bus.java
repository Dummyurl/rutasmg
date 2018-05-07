package com.l3soft.routesmg.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity
public class Bus {

    @PrimaryKey()
    private @NonNull String id;
    @ColumnInfo ()
    private int number;
    @ColumnInfo()
    private String description;
    @ColumnInfo()
    @SerializedName("create_at")
    private String createAt;
    @ColumnInfo()
    @SerializedName("user_id")
    private String userID;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCreateAt() {
        return createAt;
    }
    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
}
