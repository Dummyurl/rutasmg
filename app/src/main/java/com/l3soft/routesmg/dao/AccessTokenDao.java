package com.l3soft.routesmg.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.l3soft.routesmg.entity.AccessToken;

@Dao
public interface AccessTokenDao {
    @Query("SELECT * FROM accessToken")
    AccessToken loadAccessToken();

    @Insert
    void insertAccessToken(AccessToken accessToken);

    @Update
    void updateAccessToken(AccessToken accessToken);

    @Delete
    void deleteAccessToken(AccessToken accessToken);


}
