package com.kfp.privatesale.data.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kfp.privatesale.data.db.entity.CustomerEventJoin;
import com.kfp.privatesale.data.db.repository.CustomerEventJoinRepository;
import com.kfp.privatesale.data.db.repository.CustomerRepository;
import com.kfp.privatesale.data.model.Customer;

public class CustomerService extends Service implements EventListener<QuerySnapshot> {
    CustomerRepository customerRepository;
    CustomerEventJoinRepository customerEventJoinRepository;
    private Query query;
    private ListenerRegistration registration;
    public CustomerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        query = db.collection("customers");
        customerRepository = new CustomerRepository(getApplication());
        customerEventJoinRepository = new CustomerEventJoinRepository(getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startQuery();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopQuery();
    }

    public void startQuery() {
        if (query != null && registration == null) {
            registration = query.addSnapshotListener(this);
        }
    }

    public void stopQuery() {
        if (registration != null) {
            registration.remove();
            registration = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            return; //TODO handle better exception
        }

        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:
                    com.kfp.privatesale.data.db.entity.Customer customerEntity = dc.getDocument().toObject(com.kfp.privatesale.data.db.entity.Customer.class);
                    customerRepository.insert(customerEntity);
                    Customer customer = dc.getDocument().toObject(Customer.class);
                    for (String event : customer.getEvents()) {
                        customerEventJoinRepository.insert(new CustomerEventJoin(customerEntity.getId(), event));
                    }
                    break;
                case REMOVED:
//                    mValues.remove(dc.getOldIndex());
//                    notifyItemRemoved(dc.getOldIndex());
                    break;
                case MODIFIED:
                    if (dc.getOldIndex() == dc.getNewIndex()) {
                        customerRepository.update(dc.getDocument().toObject(com.kfp.privatesale.data.db.entity.Customer.class));
                    } else {
                        customerRepository.update(dc.getDocument().toObject(com.kfp.privatesale.data.db.entity.Customer.class));
                    }
                    break;
            }
        }
    }
}
