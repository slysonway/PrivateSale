package com.kfp.privatesale.view.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.kfp.privatesale.Preferences;
import com.kfp.privatesale.R;
import com.kfp.privatesale.service.model.Event;
import com.kfp.privatesale.viewmodel.EventViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    private final static String TAG = AboutActivity.class.getSimpleName();

    private TextView versionText;
    private Preferences preferences;
    private Spinner spinner;
    private EventViewModel eventViewModel;
    private List<Event> eventList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.about_title);
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        eventList = new ArrayList<>();
        preferences = new Preferences(this);
        versionText = findViewById(R.id.about_version);
        spinner = findViewById(R.id.about_spinner);
        initSpinnerData();

    }

    private void initSpinnerData() {
        final ArrayAdapter<Event> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String date = new SimpleDateFormat("YYYY-MM-dd").format(new Date());
        eventViewModel.eventByDate(date).observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                for (Event event : events) {
                    adapter.add(event);
                }
                Log.d(TAG, preferences.getEvent().toString());
                Event event = preferences.getEvent();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getItem(i).getId().equals(event.getId())) {
                        spinner.setSelection(i);
                        break;
                    }
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getCount() > 0) {
                    preferences.setEvent(adapter.getItem(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);
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

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
