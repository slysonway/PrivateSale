package com.kfp.privatesale.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kfp.privatesale.service.model.Event;

import java.util.List;

@Dao
public interface EventDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Event event);

    @Query("SELECT * FROM event")
    LiveData<List<Event>> getAllEvent();

    @Update
    void update(Event event);

}
