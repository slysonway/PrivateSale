package com.kfp.privatesale.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kfp.privatesale.service.model.Customer;
import com.kfp.privatesale.service.model.CustomerEventJoin;

import java.util.List;

@Dao
public interface CustomerEventJoinDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CustomerEventJoin customerEventJoin);

    @Query("SELECT * FROM customer INNER JOIN customer_event_join ON customer.id=customer_event_join.idCustomer WHERE customer_event_join.idEvent=:idEvent")
    LiveData<List<Customer>> getUserByEvent(String idEvent);
}
