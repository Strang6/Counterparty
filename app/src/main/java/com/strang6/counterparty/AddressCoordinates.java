package com.strang6.counterparty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.android.gms.maps.model.LatLng;
import com.strang6.counterparty.database.converters.LatLngConverter;

/**
 * Created by Strang6 on 12.12.2017.
 */

@Entity(foreignKeys = @ForeignKey(entity = RecentCounterparty.class, parentColumns = "id", childColumns = "counterpartyId"))
public class AddressCoordinates {
    @PrimaryKey
    private int counterpartyId;

    @TypeConverters(LatLngConverter.class)
    private LatLng coordinates;

    public AddressCoordinates(int counterpartyId, LatLng coordinates) {
        this.counterpartyId = counterpartyId;
        this.coordinates = coordinates;
    }

    public int getCounterpartyId() {
        return counterpartyId;
    }

    public void setCounterpartyId(int counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }
}
