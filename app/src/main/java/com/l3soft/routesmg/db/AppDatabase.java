package com.l3soft.routesmg.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


import com.l3soft.routesmg.dao.AccessTokenDao;
import com.l3soft.routesmg.dao.BusDao;
import com.l3soft.routesmg.dao.RouteDao;
import com.l3soft.routesmg.entity.AccessToken;
import com.l3soft.routesmg.entity.Bus;
import com.l3soft.routesmg.entity.Place;
import com.l3soft.routesmg.entity.Route;
import com.l3soft.routesmg.entity.Travel;
import com.l3soft.routesmg.entity.User;


@Database(entities = {Bus.class, Travel.class, Route.class, Place.class, AccessToken.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase{

    public abstract BusDao busDao();
    public abstract AccessTokenDao accessTokenDao();
    //public abstract RouteDao  routeDao ();
}
