package com.l3soft.routesmg.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ElOskar101 on 12/04/2018.
 */

@Entity
public class Place {

    @PrimaryKey(autoGenerate = true)
    private String id;

    @ColumnInfo()
    private double coordx;

    @ColumnInfo()
    private double coordy;

    @ColumnInfo()
    private String name;

    @ColumnInfo()
    private String description;

    @ColumnInfo()
    @SerializedName("route_id")
    private String routeID;

    @ColumnInfo()
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCoordx() {
        return coordx;
    }

    public void setCoordx(double coordx) {
        this.coordx = coordx;
    }

    public double getCoordy() {
        return coordy;
    }

    public void setCoordy(double coordy) {
        this.coordy = coordy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }
}
