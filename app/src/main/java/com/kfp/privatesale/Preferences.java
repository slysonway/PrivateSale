package com.kfp.privatesale;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import com.kfp.privatesale.service.model.Event;


public class Preferences {

    private Context context;
    private final SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
    }


    public final Event getEvent() {
        String key = this.context.getString(R.string.event_preference_key);
        return Event.create(this.sharedPreferences.getString(key, null));
    }

    public final void setEvent(Event event) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        String key = this.context.getString(R.string.event_preference_key);
        editor.putString(key, event.serialize());
        editor.apply();
    }
}
