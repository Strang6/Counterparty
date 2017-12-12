package com.strang6.counterparty.database.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Strang6 on 12.12.2017.
 */

public class LatLngConverter {
    @TypeConverter
    public static LatLng toLatLng(String str) {
        String latLng [] = str.split(";");
        return new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
    }

    @TypeConverter
    public static String toString(LatLng latLng) {
        return Double.toString(latLng.latitude) + ";" + Double.toString(latLng.longitude);
    }
}