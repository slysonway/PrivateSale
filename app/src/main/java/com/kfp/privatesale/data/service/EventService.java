package com.kfp.privatesale.data.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kfp.privatesale.data.db.entity.Event;
import com.kfp.privatesale.data.db.repository.EventRepository;

public class EventService extends Service implements EventListener<QuerySnapshot> {
    EventRepository eventRepository;
    private Query query;
    private ListenerRegistration registration;
    public EventService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        query = db.collection("events");
        eventRepository = new EventRepository(getApplication());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopQuery();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startQuery();
        return super.onStartCommand(intent, flags, startId);
    }


    public void startQuery() {
        if (query != null && registration == null) {
            registration = query.addSnapshotListener(this);
        }
    }

    public void stopQuery() {
        if (registration != null) {
            registration.remove();
            registration = null;
        }
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            return; //TODO handle better exception
        }

        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:
                    eventRepository.insert(dc.getDocument().toObject(Event.class));
                    break;
                case REMOVED:
                    break;
                case MODIFIED:
                    if (dc.getOldIndex() == dc.getNewIndex()) {
                        eventRepository.update(dc.getDocument().toObject(Event.class));
                    } else {
                        eventRepository.update(dc.getDocument().toObject(Event.class));
                    }
                    break;
            }
        }
    }
}
