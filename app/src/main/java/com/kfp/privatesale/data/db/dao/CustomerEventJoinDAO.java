package com.kfp.privatesale.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kfp.privatesale.data.db.entity.Customer;
import com.kfp.privatesale.data.db.entity.CustomerEventJoin;
import com.kfp.privatesale.data.db.entity.Event;

import java.util.List;

@Dao
public interface CustomerEventJoinDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CustomerEventJoin customerEventJoin);

    @Query("SELECT * FROM customer INNER JOIN customer_event_join ON customer.id=customer_event_join.idCustomer WHERE customer_event_join.idEvent=:idEvent")
    LiveData<List<Customer>> getCustomerByEvent(String idEvent);

    @Query("DELETE FROM customer_event_join WHERE idCustomer=:id")
    void delete(String id);

    @Query("SELECT * FROM event INNER JOIN customer_event_join ON event.id=customer_event_join.idEvent WHERE customer_event_join.idCustomer=:idCustomer")
    LiveData<List<Event>> getEventByCustomer(String idCustomer);

}
