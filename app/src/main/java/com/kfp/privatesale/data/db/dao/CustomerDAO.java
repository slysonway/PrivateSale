package com.kfp.privatesale.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kfp.privatesale.data.db.entity.Customer;

import java.util.List;

@Dao
public interface CustomerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Customer customer);

    @Query("SELECT * FROM customer")
    LiveData<List<Customer>> getAllCustomer();

    @Update
    void update(Customer customer);

    @Query("SELECT * FROM customer WHERE id=:id")
    LiveData<Customer> getCustomerById(String id);

}
