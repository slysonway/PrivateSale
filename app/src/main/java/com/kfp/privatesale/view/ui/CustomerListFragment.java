package com.kfp.privatesale.view.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kfp.privatesale.R;
import com.kfp.privatesale.service.model.Customer;
import com.kfp.privatesale.view.adapter.CustomerAdapter;

public class CustomerListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private CustomerAdapter adapter;
    private String eventId;

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
            Query query = db.collection("customers").whereArrayContains("events", eventId);
            adapter = new CustomerAdapter(query, mListener);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startQuery();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopQuery();
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Customer customer);
    }
}