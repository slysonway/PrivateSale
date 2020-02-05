package com.kfp.privatesale.data.db.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kfp.privatesale.data.db.AppDatabase;
import com.kfp.privatesale.data.db.dao.CustomerDAO;
import com.kfp.privatesale.data.db.entity.Customer;

import java.util.List;

public class CustomerRepository {
    private CustomerDAO customerDAO;
    private LiveData<List<Customer>> allCustomers;

    public CustomerRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        customerDAO = database.customerDAO();
        allCustomers = customerDAO.getAllCustomer();
    }

    public LiveData<List<Customer>> allCustomers() {
        return allCustomers;
    }

    public void insert(Customer customer) {
        new InsertAsyncTask(customerDAO).execute(customer);
    }

    public void update(Customer Customer) {
        new UpdateAsyncTask(customerDAO).execute(Customer);
    }

    public LiveData<Customer> getCustomerById(String id) {
        return customerDAO.getCustomerById(id);
    }

    public static class InsertAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDAO customerDAO;

        public InsertAsyncTask(CustomerDAO customerDAO) {
            this.customerDAO = customerDAO;
        }

        @Override
        protected Void doInBackground(Customer... customers) {
            for (Customer customer : customers) {
                customerDAO.insert(customer);
            }
            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDAO customerDAO;

        public UpdateAsyncTask(CustomerDAO customerDAO) {
            this.customerDAO = customerDAO;
        }

        @Override
        protected Void doInBackground(Customer... customers) {
            for (Customer customer : customers) {
                customerDAO.update(customer);
            }
            return null;
        }
    }
}
