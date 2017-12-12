package com.strang6.counterparty;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.strang6.counterparty.database.converters.DateConverter;

import java.util.Date;

/**
 * Created by Strang6 on 10.12.2017.
 */

@Entity
public class RecentCounterparty {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @Embedded
    private Counterparty counterparty;

    @TypeConverters(DateConverter.class)
    private Date uploadDate;

    private boolean isFavorite;

    public RecentCounterparty(Counterparty counterparty, Date uploadDate, boolean isFavorite) {
        this.counterparty = counterparty;
        this.uploadDate = uploadDate;
        this.isFavorite = isFavorite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Counterparty getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(Counterparty counterparty) {
        this.counterparty = counterparty;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
