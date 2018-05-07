package com.l3soft.routesmg.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

/**
 * Created by ElOskar101 on 12/04/2018.
 */

@Entity
public class User {

    @PrimaryKey()
    private String id;
    @ColumnInfo()
    private String realm;
    @ColumnInfo()
    private String username;
    @ColumnInfo()
    private String email;
    @ColumnInfo(name = "email_verified")
    private boolean emailVerfied;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerfied() {
        return emailVerfied;
    }

    public void setEmailVerfied(boolean emailVerfied) {
        this.emailVerfied = emailVerfied;
    }
}
