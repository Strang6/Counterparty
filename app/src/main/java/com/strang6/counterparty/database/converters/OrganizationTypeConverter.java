package com.strang6.counterparty.database.converters;

import android.arch.persistence.room.TypeConverter;

import com.strang6.counterparty.Counterparty;

/**
 * Created by Strang6 on 10.12.2017.
 */

public class OrganizationTypeConverter {
    @TypeConverter
    public static Counterparty.OrganizationType toOrganizationType(String organizationType) {
        return Counterparty.OrganizationType.valueOf(organizationType);
    }

    @TypeConverter
    public static String toString(Counterparty.OrganizationType organizationType) {
        return organizationType.name();
    }
}
