package com.kfp.privatesale.service;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.kfp.privatesale.service.dao.CustomerDAO;
import com.kfp.privatesale.service.dao.CustomerEventJoinDAO;
import com.kfp.privatesale.service.dao.EventDAO;
import com.kfp.privatesale.service.model.Customer;
import com.kfp.privatesale.service.model.CustomerEventJoin;
import com.kfp.privatesale.service.model.Event;

@Database(entities = {Event.class, Customer.class, CustomerEventJoin.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase INSTANCE;

    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "ps_database").build();
        }
        return INSTANCE;
    }

    public abstract EventDAO eventDAO();
    public abstract CustomerDAO customerDAO();
    public abstract CustomerEventJoinDAO customerEventJoinDAO();
}
