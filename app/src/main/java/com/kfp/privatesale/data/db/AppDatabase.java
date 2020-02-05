package com.kfp.privatesale.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.kfp.privatesale.data.db.dao.CustomerDAO;
import com.kfp.privatesale.data.db.dao.CustomerEventJoinDAO;
import com.kfp.privatesale.data.db.dao.EventDAO;
import com.kfp.privatesale.data.db.entity.Customer;
import com.kfp.privatesale.data.db.entity.CustomerEventJoin;
import com.kfp.privatesale.data.db.entity.Event;


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
