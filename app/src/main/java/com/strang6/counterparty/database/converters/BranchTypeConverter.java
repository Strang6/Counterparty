package com.strang6.counterparty.database.converters;

import android.arch.persistence.room.TypeConverter;

import com.strang6.counterparty.Counterparty;

/**
 * Created by Strang6 on 10.12.2017.
 */

public class BranchTypeConverter {
    @TypeConverter
    public static Counterparty.BranchType toBranchType(String branchType) {
        return Counterparty.BranchType.valueOf(branchType);
    }

    @TypeConverter
    public static String toString(Counterparty.BranchType branchType) {
        return branchType.name();
    }
}
