package com.kfp.privatesale.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kfp.privatesale.service.model.Event;
import com.kfp.privatesale.service.repository.EventRepository;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private EventRepository eventRepository;
    private LiveData<List<Event>> allEvents;

    public EventViewModel(@NonNull Application application) {
        super(application);
        eventRepository = new EventRepository(application);
        allEvents = eventRepository.allEvents();
    }

    public LiveData<List<Event>> allEvents() {
        return allEvents;
    }

    public LiveData<List<Event>> eventByDate(String date) {
        return eventRepository.eventByDate(date);
    }

    public void insert(Event event) {
        eventRepository.insert(event);
    }

    public void update(Event event) {
        eventRepository.update(event);
    }
}
