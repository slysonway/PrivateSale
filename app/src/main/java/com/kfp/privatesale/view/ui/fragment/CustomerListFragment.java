package com.kfp.privatesale.view.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kfp.privatesale.R;
import com.kfp.privatesale.service.model.Customer;
import com.kfp.privatesale.service.model.CustomerEventJoin;
import com.kfp.privatesale.view.adapter.CustomerAdapter;
import com.kfp.privatesale.viewmodel.CustomerEventJoinViewModel;
import com.kfp.privatesale.viewmodel.CustomerViewModel;

import java.util.List;

public class CustomerListFragment extends Fragment implements EventListener<QuerySnapshot> {

    private OnFragmentInteractionListener mListener;
    private CustomerAdapter adapter;
    private String eventId;
    private Query query;
    private ListenerRegistration registration;
    private CustomerViewModel customerViewModel;
    private CustomerEventJoinViewModel customerEventJoinViewModel;

    public CustomerListFragment(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            query = db.collection("customers").whereArrayContains("events", eventId);
            adapter = new CustomerAdapter(mListener);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(adapter);
        }
        customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        customerEventJoinViewModel = ViewModelProviders.of(this).get(CustomerEventJoinViewModel.class);
        customerEventJoinViewModel.customerByEvent(eventId).observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                adapter.setCustomers(customers);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        startQuery();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopQuery();
    }

    @Override
    public void onAttach(@NonNull  Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            return; //TODO handle better exception
        }

        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:
                    Customer customer = dc.getDocument().toObject(Customer.class);
                    customerViewModel.insert(customer);
                    customerEventJoinViewModel.insert(new CustomerEventJoin(customer.getId(), eventId));
                    break;
                case REMOVED:
//                    mValues.remove(dc.getOldIndex());
//                    notifyItemRemoved(dc.getOldIndex());
                    break;
                case MODIFIED:
                    if (dc.getOldIndex() == dc.getNewIndex()) {
                        customerViewModel.update(dc.getDocument().toObject(Customer.class));
                    } else {
                        customerViewModel.update(dc.getDocument().toObject(Customer.class));
                    }
                    break;
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentCustomerListInteraction(Customer customer);
    }
}
