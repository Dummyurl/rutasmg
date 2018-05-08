package com.l3soft.routesmg.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.l3soft.routesmg.entity.Bus;
import com.l3soft.routesmg.entity.Route;

import java.util.List;

/**
 * Created by ElOskar101 on 12/04/2018.
 */
@Dao
public interface RouteDao {
    @Query("SELECT * FROM route")
    List<Route> loadAllBuses();

    @Insert
    void insertRoute(Route route);

    @Update
    void updateRoute(Route route);

    @Delete
    void deleteRoute(Route route);
}
