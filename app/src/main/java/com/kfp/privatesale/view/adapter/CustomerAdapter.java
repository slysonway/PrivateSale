package com.kfp.privatesale.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kfp.privatesale.R;
import com.kfp.privatesale.service.model.Customer;
import com.kfp.privatesale.view.ui.fragment.CustomerListFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private List<Customer> mValues;
    private final CustomerListFragment.OnFragmentInteractionListener mListener;
//    private Query query;
//    private ListenerRegistration registration;

    public CustomerAdapter(CustomerListFragment.OnFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
    }

    public void setCustomers(List<Customer> customers) {
        mValues = customers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_customer_title, parent, false);
        return  new ViewHolder(view);
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
