package com.kfp.privatesale.view.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.kfp.privatesale.R;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        TextView versionText = (TextView) findViewById(R.id.launch_version);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = packageInfo.versionName;
            versionText.setText(getString(R.string.about_version, version));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                    finish();
                }
            }
        };
        timer.start();

    }
}
