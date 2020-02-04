package com.kfp.privatesale.service.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "customer_event_join",
        primaryKeys = {"idCustomer", "idEvent"},
        foreignKeys = {
            @ForeignKey(entity = Customer.class,
                        parentColumns = "id",
                        childColumns = "idCustomer"),
            @ForeignKey(entity = Event.class,
                        parentColumns = "id",
                        childColumns = "idEvent")
        })
public class CustomerEventJoin {
    @NonNull
    public String idCustomer;
    @NonNull
    public String idEvent;

    public CustomerEventJoin(@NonNull String idCustomer, @NonNull String idEvent) {
        this.idCustomer = idCustomer;
        this.idEvent = idEvent;
    }

    @NonNull
    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(@NonNull String idCustomer) {
        this.idCustomer = idCustomer;
    }

    @NonNull
    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(@NonNull String idEvent) {
        this.idEvent = idEvent;
    }
}

