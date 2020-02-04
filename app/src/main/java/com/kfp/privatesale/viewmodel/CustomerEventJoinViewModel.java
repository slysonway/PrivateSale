package com.kfp.privatesale.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kfp.privatesale.service.model.Customer;
import com.kfp.privatesale.service.model.CustomerEventJoin;
import com.kfp.privatesale.service.repository.CustomerEventJoinRepository;

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

    public void insert(CustomerEventJoin customerEventJoin) {
        customerEventJoinRepository.insert(customerEventJoin);
    }
}
