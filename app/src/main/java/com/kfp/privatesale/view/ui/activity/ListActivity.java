package com.kfp.privatesale.view.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.kfp.privatesale.utils.ConstantField;
import com.kfp.privatesale.R;
import com.kfp.privatesale.data.db.entity.Customer;
import com.kfp.privatesale.data.db.entity.Event;
import com.kfp.privatesale.utils.CustomerProcess;
import com.kfp.privatesale.view.ui.fragment.CustomerListFragment;
import com.kfp.privatesale.view.ui.fragment.EventListFragment;

public class ListActivity extends AppCompatActivity implements EventListFragment.OnListFragmentInteractionListerner, CustomerListFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        launchFragment(new EventListFragment(), false).commit();
    }

    @Override
    public void onEventListFragmentInteraction(Event event) {
        Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
        setTitle(getString(R.string.event_title));
        launchFragment(new CustomerListFragment(event.getId()), true).commit();
    }

    @Override
    public void onFragmentCustomerListInteraction(Customer customer) {
        Toast.makeText(this, customer.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ListActivity.this, CustomerActivity.class);
        intent.putExtra(ConstantField.SCANNED_CODE, customer.getId());
        intent.putExtra(ConstantField.CUSTOMER_PROCESS, CustomerProcess.CONSULTATION);
        startActivity(intent);
    }

    private FragmentTransaction launchFragment(Fragment fragment, Boolean isBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.list_frame, fragment);
        if (isBackStack) {
            ft.addToBackStack(null);
        }
        return ft;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }


}
