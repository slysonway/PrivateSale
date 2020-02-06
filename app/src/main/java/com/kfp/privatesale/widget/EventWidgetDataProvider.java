package com.kfp.privatesale.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import androidx.lifecycle.Observer;

import com.kfp.privatesale.R;
import com.kfp.privatesale.data.db.entity.Event;
import com.kfp.privatesale.data.db.repository.EventRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//IT's Like adapter in normal View
public class EventWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Event> mValues = new ArrayList<>();
    private EventRepository eventRepository;

    public EventWidgetDataProvider(Context context, Intent intent, EventRepository eventRepository) {
        this.context = context;
        this.eventRepository = eventRepository;
    }

    @Override
    public void onCreate() {
        eventRepository.eventByDate(new SimpleDateFormat("YYYY-MM-dd").format(new Date())).observeForever(new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                mValues = events;
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetsIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, EventWidget.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetsIds, R.id.widget_list);
                EventWidget.updateAppWidget(context, appWidgetManager, appWidgetsIds);
            }
        });
    }

    @Override
    public void onDataSetChanged() {
       //initData();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Event event = mValues.get(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.fragment_event);
        remoteViews.setTextViewText(R.id.event_name, event.toString());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
