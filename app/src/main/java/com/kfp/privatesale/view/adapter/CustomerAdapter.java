package com.kfp.privatesale.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kfp.privatesale.R;
import com.kfp.privatesale.service.model.Customer;
import com.kfp.privatesale.view.ui.fragment.CustomerListFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> implements EventListener<QuerySnapshot> {

    private final List<Customer> mValues;
    private final CustomerListFragment.OnFragmentInteractionListener mListener;
    private Query query;
    private ListenerRegistration registration;

    public CustomerAdapter(Query query, CustomerListFragment.OnFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
        this.query = query;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_customer_title, parent, false);
        return  new ViewHolder(view);
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
        mValues.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mCustomer = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getId());
        holder.mFirstName.setText(mValues.get(position).getFirstname());
        holder.mLastName.setText(mValues.get(position).getLastname());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onFragmentCustomerListInteraction(holder.mCustomer);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            return; //TODO handle better exception
        }

        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:
                    mValues.add(dc.getNewIndex(), dc.getDocument().toObject(Customer.class));
                    notifyItemInserted(dc.getNewIndex());
                    break;
                case REMOVED:
                    mValues.remove(dc.getOldIndex());
                    notifyItemRemoved(dc.getOldIndex());
                    break;
                case MODIFIED:
                    if (dc.getOldIndex() == dc.getNewIndex()) {
                        mValues.set(dc.getNewIndex(), dc.getDocument().toObject(Customer.class));
                        notifyItemChanged(dc.getNewIndex());
                    } else {
                        mValues.remove(dc.getOldIndex());
                        mValues.add(dc.getNewIndex(), dc.getDocument().toObject(Customer.class));
                        notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                    }
                    break;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mFirstName;
        public final TextView mLastName;
        public Customer mCustomer;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.customer_id);
            mFirstName = (TextView) view.findViewById(R.id.customer_firstname);
            mLastName = (TextView) view.findViewById(R.id.customer_lastname);
        }
    }
}
