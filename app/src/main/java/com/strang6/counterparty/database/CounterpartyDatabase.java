package com.strang6.counterparty.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.strang6.counterparty.AddressCoordinates;
import com.strang6.counterparty.RecentCounterparty;

/**
 * Created by Strang6 on 10.12.2017.
 */

@Database(entities = {RecentCounterparty.class, AddressCoordinates.class}, version = 1)
public abstract class CounterpartyDatabase extends RoomDatabase{
    private static CounterpartyDatabase INSTANCE;

    public static CounterpartyDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), CounterpartyDatabase.class, "counterparty_db")
                    .build();
        }
        return INSTANCE;
    }

    public abstract RecentCounterpartyDAO getRecentCounterpartyDAO();

    public abstract AddressCoordinatesDAO getAddressCoordinatesDAO();
}
