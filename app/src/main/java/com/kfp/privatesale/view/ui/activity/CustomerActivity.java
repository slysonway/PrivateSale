package com.kfp.privatesale.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.kfp.privatesale.ConstantField;
import com.kfp.privatesale.R;
import com.kfp.privatesale.service.model.Customer;

public class CustomerActivity extends AppCompatActivity {

    public static final String TAG = CustomerActivity.class.getSimpleName();
    private String customerId;
    private Customer customer;
    private TextView name;
    private TextView firstname;
    private TextView email;
    private TextView status;
    private TextView infoTag;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCustomer();
    }

    private void loadCustomer() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("customers").document(customerId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d(TAG, "Current data:" + documentSnapshot.getData());
                    customer = documentSnapshot.toObject(Customer.class);
                    name.setText(customer.getLastname());
                    firstname.setText(customer.getFirstname());
                    email.setText(customer.getMail());
                    status.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                    infoTag.setVisibility(View.GONE);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
