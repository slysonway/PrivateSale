package com.kfp.privatesale.data.db.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kfp.privatesale.data.db.AppDatabase;
import com.kfp.privatesale.data.db.dao.CustomerEventJoinDAO;
import com.kfp.privatesale.data.db.entity.Customer;
import com.kfp.privatesale.data.db.entity.CustomerEventJoin;

import java.util.List;

public class CustomerEventJoinRepository {
    private CustomerEventJoinDAO customerEventJoinDAO;

    public CustomerEventJoinRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        customerEventJoinDAO = database.customerEventJoinDAO();
    }

    public LiveData<List<Customer>> getCustomerByEventId(String idEvent) {
        return customerEventJoinDAO.getUserByEvent(idEvent);
    }

    public void insert(CustomerEventJoin customerEventJoin) {
        new InsertAsyncTask(customerEventJoinDAO).execute(customerEventJoin);
    }

    public static class InsertAsyncTask extends AsyncTask<CustomerEventJoin, Void, Void> {
        private CustomerEventJoinDAO customerEventJoinDAO;

        public InsertAsyncTask(CustomerEventJoinDAO customerEventJoinDAO) {
            this.customerEventJoinDAO = customerEventJoinDAO;
        }

        @Override
        protected Void doInBackground(CustomerEventJoin... customerEventJoins) {
            for (CustomerEventJoin customerEventJoin : customerEventJoins) {
                customerEventJoinDAO.insert(customerEventJoin);
            }
            return null;
        }
    }
}
