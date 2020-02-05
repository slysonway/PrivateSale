package com.kfp.privatesale.view.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.zxing.Result;
import com.kfp.privatesale.utils.ConstantField;
import com.kfp.privatesale.R;
import com.kfp.privatesale.data.service.CustomerService;
import com.kfp.privatesale.data.service.EventService;
import com.kfp.privatesale.utils.CustomerProcess;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private BottomNavigationView bottomNavigationView;
    private ZXingScannerView mScannerView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Start Event Service");
        startService(new Intent(this, EventService.class));
        Log.d(TAG, "Start Customer Service");
        startService(new Intent(this, CustomerService.class));
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        bottomNavigationView = findViewById(R.id.bottom_menu);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 2);
        }

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        configureBottomMenu();
    }

    private void configureBottomMenu() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_info:
                        //Toast.makeText(MainActivity.this, "info", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                    case R.id.action_list:
                        //Toast.makeText(MainActivity.this, "list", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                        break;
                    case R.id.action_flash:
                        Toast.makeText(MainActivity.this, "flash", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Stop Event Service");
        stopService(new Intent(this, EventService.class));
        Log.d(TAG, "Stop Customer Service");
        stopService(new Intent(this, CustomerService.class));
    }

    @Override
    public void handleResult(Result rawResult) {
        //Toast.makeText(this, "Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, CustomerActivity.class);
        intent.putExtra(ConstantField.SCANNED_CODE, rawResult.getText());
        intent.putExtra(ConstantField.CUSTOMER_PROCESS, CustomerProcess.CHECKING);
        startActivity(intent);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(MainActivity.this);
            }
        }, 100);
    }
}
