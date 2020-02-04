package com.kfp.privatesale.view.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kfp.privatesale.Preferences;
import com.kfp.privatesale.R;
import com.kfp.privatesale.service.model.Event;
import com.kfp.privatesale.view.ui.fragment.EventListFragment;

public class AboutActivity extends AppCompatActivity implements EventListFragment.OnListFragmentInteractionListerner {

    private TextView versionText;
    private Preferences preferences;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.about_title);
        preferences = new Preferences(this);

        versionText = findViewById(R.id.about_version);
        button = findViewById(R.id.about_event_btn);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = packageInfo.versionName;
            versionText.setText(getString(R.string.about_version, version));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchEventListFragment();
                }
            });
            Event event = preferences.getEvent();
            if (event != null) {
                button.setText(getString(R.string.about_event_btn, event.getName(), event.getDate().toString()));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEventListFragmentInteraction(Event event) {
        Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        preferences.setEvent(event);
        button.setText(getString(R.string.about_event_btn, event.getName(), event.getDate().toString()));
    }

    private void launchEventListFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.event_frame, new EventListFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}
