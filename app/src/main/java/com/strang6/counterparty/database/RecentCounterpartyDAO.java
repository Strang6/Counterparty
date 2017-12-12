package com.strang6.counterparty.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.strang6.counterparty.RecentCounterparty;
import com.strang6.counterparty.database.converters.BranchTypeConverter;
import com.strang6.counterparty.database.converters.DateConverter;
import com.strang6.counterparty.database.converters.OrganizationTypeConverter;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Strang6 on 10.12.2017.
 */

@Dao
@TypeConverters({BranchTypeConverter.class, OrganizationTypeConverter.class, DateConverter.class})
public interface RecentCounterpartyDAO {
    @Query("SELECT * FROM RecentCounterparty")
    LiveData<List<RecentCounterparty>> getAllRecentCounterparties();

    @Query("SELECT * FROM RecentCounterparty WHERE id = :id")
    RecentCounterparty getItemById(String id);

    @Query("SELECT * FROM RecentCounterparty WHERE name = :name AND inn = :inn AND kpp = :kpp")
    RecentCounterparty getItemByNameInnKpp(String name, String inn, String kpp);

    @Insert(onConflict = REPLACE)
    void  addRecentCounterparty(RecentCounterparty recentCounterparty);

    @Update
    void updateItem(RecentCounterparty recentCounterparty);

    @Delete
    void deleteRecentCounterparty(RecentCounterparty recentCounterparty);
}
