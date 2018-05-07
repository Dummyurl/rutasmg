package com.l3soft.routesmg.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.l3soft.routesmg.entity.Commentary;

/**
 * Created by ElOskar101 on 12/04/2018.
 */
@Dao
public interface CommentaryDao {

    @Query("SELECT * FROM commentary")
    Commentary[] loadAllComments();

    @Insert
    void inserCommentary(Commentary comments);

    @Update
    void updateBus(Commentary... comments);

    @Delete
    Void deleteCommentary(Commentary... comments);
}
