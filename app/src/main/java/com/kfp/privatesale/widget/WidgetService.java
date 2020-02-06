package com.kfp.privatesale.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.kfp.privatesale.data.db.repository.EventRepository;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new EventWidgetDataProvider(this, intent, new EventRepository(this));
    }
}
