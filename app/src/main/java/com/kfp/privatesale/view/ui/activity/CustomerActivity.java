package com.kfp.privatesale.view.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kfp.privatesale.data.db.entity.Event;
import com.kfp.privatesale.utils.ConstantField;
import com.kfp.privatesale.R;
import com.kfp.privatesale.data.db.entity.Customer;
import com.kfp.privatesale.utils.CustomerProcess;
import com.kfp.privatesale.utils.Preferences;
import com.kfp.privatesale.viewmodel.CustomerEventJoinViewModel;
import com.kfp.privatesale.viewmodel.CustomerViewModel;

import java.util.List;

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
    private CustomerEventJoinViewModel customerEventJoinViewModel;
    private CustomerProcess customerProcess;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            customerId = intent.getStringExtra(ConstantField.SCANNED_CODE);
            customerProcess = (CustomerProcess) intent.getSerializableExtra(ConstantField.CUSTOMER_PROCESS);
        } else {
            finish();
        }
        preferences = new Preferences(this);
        name = findViewById(R.id.customer_info_name);
        firstname = findViewById(R.id.customer_info_firstname);
        email = findViewById(R.id.customer_info_email);
        status = findViewById(R.id.customer_info_status);
        infoTag = findViewById(R.id.customer_info_tag_unauthorized);
        customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        customerEventJoinViewModel = ViewModelProviders.of(this).get(CustomerEventJoinViewModel.class);
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

    private void loadEvent() {
        customerEventJoinViewModel.eventByCustomer(customerId).observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                checkCustomerEvent(events);
            }
        });
    }

    private void setCustomer(Customer customer) {
        this.customer = customer;
        switch (customerProcess) {
            case CONSULTATION:
                //TODO get all events in spinners
                name.setText(this.customer.getLastname());
                firstname.setText(this.customer.getFirstname());
                email.setText(this.customer.getMail());
                status.setVisibility(View.GONE);
                infoTag.setVisibility(View.GONE);
                break;
            case CHECKING:
                //event preference == null then dialog
                if (preferences.getEvent() == null) {
                    AlertDialog dialog = showErrorDialog();
                    dialog.show();
                    return;
                }
                //if customer not exist then unauthorised
                if (customer == null) {
                    status.setVisibility(View.VISIBLE);
                    status.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    status.setText(R.string.unauthorized);
                    Log.d(TAG, "Current data: null");
                    return;
                }
                loadEvent();
                break;
        }
    }

    //verif if customer authorized in this event
    //events it's list of customer events
    private void checkCustomerEvent(List<Event> events) {
        for (Event event : events) {
            if (event.getId().equals(preferences.getEvent().getId())) {
                setCheckedView();
                return;
            }
        }
        setNotCheckedView();
    }
    // customer can't participate
    private void setNotCheckedView() {
        name.setText(this.customer.getLastname());
        firstname.setText(this.customer.getFirstname());
        email.setText(this.customer.getMail());
        status.setVisibility(View.VISIBLE);
        status.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        status.setText(R.string.unauthorized);
        infoTag.setVisibility(View.VISIBLE);
    }

    //customer can participate to event
    private void setCheckedView() {
        name.setText(this.customer.getLastname());
        firstname.setText(this.customer.getFirstname());
        email.setText(this.customer.getMail());
        status.setVisibility(View.VISIBLE);
        status.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        infoTag.setVisibility(View.GONE);
    }

    private AlertDialog showErrorDialog() {
            return new AlertDialog.Builder(this)
                    .setTitle(R.string.error_dialog_title)
                    .setMessage(R.string.no_event_select_dialog)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
