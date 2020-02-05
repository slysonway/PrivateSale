package com.kfp.privatesale.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kfp.privatesale.data.db.entity.Event;

import java.util.List;

@Dao
public interface EventDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Event event);

    @Query("SELECT * FROM event")
    LiveData<List<Event>> getAllEvent();

    @Update
    void update(Event event);

    @Query("SELECT * FROM event WHERE date > :date ")
    LiveData<List<Event>> getEventByDate(String date);

}
