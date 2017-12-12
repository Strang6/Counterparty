package com.strang6.counterparty.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.google.android.gms.maps.model.LatLng;
import com.strang6.counterparty.AddressCoordinates;
import com.strang6.counterparty.database.converters.LatLngConverter;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Strang6 on 12.12.2017.
 */

@Dao
@TypeConverters(LatLngConverter.class)
public interface AddressCoordinatesDAO {
    @Query("SELECT coordinates FROM AddressCoordinates WHERE counterpartyId = :id")
    LatLng getCoordinatesById(String id);

    @Insert(onConflict = REPLACE)
    void  addCoordinates(AddressCoordinates addressCoordinates);

    @Query("DELETE FROM AddressCoordinates WHERE counterpartyId = :id")
    void deleteAddressById(String id);
}
