package com.kfp.privatesale.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kfp.privatesale.ConstantField;
import com.kfp.privatesale.R;
import com.kfp.privatesale.data.db.entity.Customer;
import com.kfp.privatesale.viewmodel.CustomerViewModel;

public class CustomerActivity extends AppCompatActivity {

    public static final String TAG = CustomerActivity.class.getSimpleName();
    private String customerId;
    private Customer customer;
    private TextView name;
    private TextView firstname;
    private TextView email;
    private TextView status;
    private TextView infoTag;
    private CustomerViewModel customerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        customerId = intent.getStringExtra(ConstantField.SCANNED_CODE);

        name = findViewById(R.id.customer_info_name);
        firstname = findViewById(R.id.customer_info_firstname);
        email = findViewById(R.id.customer_info_email);
        status = findViewById(R.id.customer_info_status);
        infoTag = findViewById(R.id.customer_info_tag_unauthorized);
        customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCustomer();
    }

    private void loadCustomer() {
        customerViewModel.customerById(customerId).observe(this, new Observer<Customer>() {
            @Override
            public void onChanged(Customer customer) {
                setCustomer(customer);
            }
        });
    }

    private void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            name.setText(this.customer.getLastname());
            firstname.setText(this.customer.getFirstname());
            email.setText(this.customer.getMail());
            status.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            infoTag.setVisibility(View.GONE);
        } else {
            status.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            Log.d(TAG, "Current data: null");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
