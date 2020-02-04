package com.kfp.privatesale.service.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kfp.privatesale.service.AppDatabase;
import com.kfp.privatesale.service.dao.EventDAO;
import com.kfp.privatesale.service.model.Event;

import java.util.List;

public class EventRepository {
    private EventDAO eventDAO;
    private LiveData<List<Event>> allEvents;

    public EventRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        eventDAO = database.eventDAO();
        allEvents = eventDAO.getAllEvent();
    }

    public LiveData<List<Event>> allEvents() {
        return allEvents;
    }

    public void insert(Event event) {
        new InsertAsyncTask(eventDAO).execute(event);
    }

    public void update(Event event) {
        new UpdateAsyncTask(eventDAO).execute(event);
    }

    public LiveData<List<Event>> eventByDate(String date) {
        return eventDAO.getEventByDate(date);
    }

    public static class InsertAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDAO eventDAO;

        public InsertAsyncTask(EventDAO eventDAO) {
            this.eventDAO = eventDAO;
        }

        @Override
        protected Void doInBackground(Event... events) {
            for (Event event : events) {
                eventDAO.insert(event);
            }
            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDAO eventDAO;

        public UpdateAsyncTask(EventDAO eventDAO) {
            this.eventDAO = eventDAO;
        }

        @Override
        protected Void doInBackground(Event... events) {
            for (Event event : events) {
                eventDAO.update(event);
            }
            return null;
        }
    }
}
