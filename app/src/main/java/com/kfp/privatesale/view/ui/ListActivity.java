package com.kfp.privatesale.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.kfp.privatesale.R;
import com.kfp.privatesale.service.model.Customer;
import com.kfp.privatesale.service.model.Event;

public class ListActivity extends AppCompatActivity implements EventListFragment.OnListFragmentInteractionListerner, CustomerListFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        launchFragment(new EventListFragment(), false).commit();
    }

    @Override
    public void onListFragmentInteraction(Event event) {
        Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
        launchFragment(new CustomerListFragment(event.getId()), true).commit();
    }

    @Override
    public void onFragmentInteraction(Customer customer) {
        Toast.makeText(this, customer.toString(), Toast.LENGTH_SHORT).show();
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


}
