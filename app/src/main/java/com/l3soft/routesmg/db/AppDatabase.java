package com.l3soft.routesmg.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


import com.l3soft.routesmg.dao.BusDao;
import com.l3soft.routesmg.dao.RouteDao;
import com.l3soft.routesmg.entity.Bus;



@Database(entities = {Bus.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase{

    public abstract BusDao busDao();
    //public abstract RouteDao  routeDao ();
}
