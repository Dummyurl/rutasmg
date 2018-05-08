package com.l3soft.routesmg.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.l3soft.routesmg.entity.Bus;
import com.l3soft.routesmg.entity.Place;

import java.util.List;

/**
 * Created by ElOskar101 on 12/04/2018.
 */

@Dao
public interface PlaceDao {
    @Query("SELECT * FROM place")
    List<Place> loadAllPlace();

    @Insert
    void insertPlace(Place place);

    @Update
    void updatePlace(Place place);

    @Delete
    void deletePlace(Place place);
}
