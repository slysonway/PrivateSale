package com.kfp.privatesale.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kfp.privatesale.data.db.entity.Customer;
import com.kfp.privatesale.data.db.repository.CustomerRepository;

import java.util.List;

public class CustomerViewModel extends AndroidViewModel {
    private CustomerRepository customerRepository;
    private LiveData<List<Customer>> allCustomers;

    public CustomerViewModel(@NonNull Application application) {
        super(application);
        this.customerRepository = new CustomerRepository(application);
        allCustomers = customerRepository.allCustomers();
    }

    public LiveData<List<Customer>> allCustomers() {
        return allCustomers;
    }

    public LiveData<Customer> customerById(String id) {
        return customerRepository.getCustomerById(id);
    }

    public void insert(Customer customer) {
        customerRepository.insert(customer);
    }

    public void update(Customer customer) {
        customerRepository.update(customer);
    }
}
