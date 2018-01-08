package com.strang6.counterparty;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.strang6.counterparty.database.converters.BranchTypeConverter;
import com.strang6.counterparty.database.converters.OrganizationTypeConverter;

/**
 * Created by Strang6 on 21.11.2017.
 */

public class Counterparty implements Parcelable{
    private String name, opf, address;
    private String inn, kpp, ogrn;
    private int branchCount;

    @TypeConverters(BranchTypeConverter.class)
    private BranchType branchType;

    @TypeConverters(OrganizationTypeConverter.class)
    private OrganizationType type;

    @Ignore
    private LatLng latLng;

    public Counterparty(String name, String opf, String address, String inn, String kpp, String ogrn, int branchCount, BranchType branchType, OrganizationType type) {
        this.name = name;
        this.opf = opf;
        this.address = address;
        this.inn = inn;
        this.kpp = kpp;
        this.ogrn = ogrn;
        this.branchCount = branchCount;
        this.branchType = branchType;
        this.type = type;
    }

    @Ignore
    public Counterparty(String name, String opf, String address, String inn, String kpp, String ogrn, int branchCount, BranchType branchType, OrganizationType type, LatLng latLng) {
        this.name = name;
        this.opf = opf;
        this.address = address;
        this.inn = inn;
        this.kpp = kpp;
        this.ogrn = ogrn;
        this.branchCount = branchCount;
        this.branchType = branchType;
        this.type = type;
        this.latLng = latLng;
    }

    protected Counterparty(Parcel in) {
        name = in.readString();
        opf = in.readString();
        address = in.readString();
        inn = in.readString();
        kpp = in.readString();
        ogrn = in.readString();
        branchCount = in.readInt();
        branchType = BranchType.valueOf(in.readString());
        type = OrganizationType.valueOf(in.readString());
    }

    public static final Creator<Counterparty> CREATOR = new Creator<Counterparty>() {
        @Override
        public Counterparty createFromParcel(Parcel in) {
            return new Counterparty(in);
        }

        @Override
        public Counterparty[] newArray(int size) {
            return new Counterparty[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Наименование компании: ").append(name)
                .append("; ОПФ: ").append(opf)
                .append("; Адрес: ").append(address)
                .append("; ИНН: ").append(inn)
                .append("; КПП: ").append(kpp)
                .append("; ОГРН: ").append(ogrn);
        if (latLng != null) {
            stringBuilder.append(": lat: ").append(latLng.latitude)
                    .append("; lng: ").append(latLng.longitude);
        }
        else {
            stringBuilder.append("; latLng: ").append("null");
        }
        return stringBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpf() {
        return opf;
    }

    public void setOpf(String opf) {
        this.opf = opf;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public int getBranchCount() {
        return branchCount;
    }

    public void setBranchCount(int branchCount) {
        this.branchCount = branchCount;
    }

    public BranchType getBranchType() {
        return branchType;
    }

    public void setBranchType(BranchType branchType) {
        this.branchType = branchType;
    }

    public OrganizationType getType() {
        return type;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(opf);
        parcel.writeString(address);
        parcel.writeString(inn);
        parcel.writeString(kpp);
        parcel.writeString(ogrn);
        parcel.writeInt(branchCount);
        parcel.writeString(branchType.name());
        parcel.writeString(type.name());
    }

    public enum BranchType {
        MAIN, BRANCH, NULL

    }

    public enum OrganizationType {
        LEGAL, INDIVIDUAL
    }
}
