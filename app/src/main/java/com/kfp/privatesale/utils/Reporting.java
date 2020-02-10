package com.kfp.privatesale.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Date;

public class Reporting {

    private static Reporting INSTANCE = null;
    private FirebaseAnalytics firebaseAnalytics;

    public Reporting(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }


    public static synchronized Reporting getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Reporting(context);
        }
        return INSTANCE;
    }

    public void sendScannedCode(String code) {
        Bundle bundle = new Bundle();
        bundle.putString("scanned_code", code);
        bundle.putString("scanned_date", new Date().toString());
        firebaseAnalytics.logEvent("scan_perform", bundle);
    }

    public void sendCustomerState(String customerId, Boolean isAuthorized, String eventId) {
        Bundle bundle = new Bundle();
        bundle.putString("customer_id", customerId);
        bundle.putString("is_authorized", isAuthorized.toString());
        bundle.putString("event_id", eventId);
        firebaseAnalytics.logEvent("customer_state", bundle);
    }
}
