package com.strang6.counterparty.database.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Strang6 on 10.12.2017.
 */

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
