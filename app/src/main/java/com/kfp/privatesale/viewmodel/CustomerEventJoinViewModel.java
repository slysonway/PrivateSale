package com.kfp.privatesale.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kfp.privatesale.data.db.entity.Customer;
import com.kfp.privatesale.data.db.entity.CustomerEventJoin;
import com.kfp.privatesale.data.db.entity.Event;
import com.kfp.privatesale.data.db.repository.CustomerEventJoinRepository;

import java.util.List;

public class CustomerEventJoinViewModel extends AndroidViewModel {
    private CustomerEventJoinRepository customerEventJoinRepository;

    public CustomerEventJoinViewModel(@NonNull Application application) {
        super(application);
        customerEventJoinRepository = new CustomerEventJoinRepository(application);
    }

    public LiveData<List<Customer>> customerByEvent(String idEvent) {
        return customerEventJoinRepository.getCustomerByEventId(idEvent);
    }

    public LiveData<List<Event>> eventByCustomer(String idCustomer) {
        return customerEventJoinRepository.getEventByCustomerId(idCustomer);
    }

    public void insert(CustomerEventJoin customerEventJoin) {
        customerEventJoinRepository.insert(customerEventJoin);
    }
}
