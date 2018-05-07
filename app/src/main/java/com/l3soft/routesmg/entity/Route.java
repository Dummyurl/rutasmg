package com.l3soft.routesmg.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

/**
 * Created by ElOskar101 on 12/04/2018.
 */

@Entity
public class Route {

    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false)
    private String id;
    @ColumnInfo(name = "travel_id")
    private String travelID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTravelID() {
        return travelID;
    }

    public void setTravelID(String travelID) {
        this.travelID = travelID;
    }
}
