package com.kfp.privatesale.data.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.firebase.firestore.DocumentId;
import com.google.gson.Gson;



@Entity(tableName = "event")
public class Event {
    @NonNull
    @PrimaryKey
    @DocumentId
    private String id;
    private String Name;
    private String date;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return Name;
    }

    public void setName(@NonNull String name) {
        Name = name;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public void setDate(Timestamp date) {
//        this.date = date.toDate();
//    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    static public  Event create(String serialized) {
        return new Gson().fromJson(serialized, Event.class);
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + " - " + getDate();
    }
}
