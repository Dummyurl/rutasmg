package com.l3soft.routesmg.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.l3soft.routesmg.entity.Bus;

import java.util.List;


/**
 * Created by ElOskar101 on 11/04/2018.
 */

@Dao
public interface  BusDao {

    @Query("SELECT * FROM bus ORDER BY bus.createAt DESC")
    List<Bus> loadAllBuses();

    @Insert
    void insertBus(Bus bus);

    @Update
    void updateBus(Bus bus);

    @Delete
    void deleteBus(Bus bus);
}
