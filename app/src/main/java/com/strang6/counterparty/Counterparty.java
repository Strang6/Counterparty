package com.strang6.counterparty;

/**
 * Created by Strang6 on 21.11.2017.
 */

public class Counterparty {
    private String name, opf, address;
    private String inn, kpp, ogrn;
    private int branchCount;
    private BranchType branchType;
    private Type type;

    public Counterparty(String name) {
        this.name = name;
    }

    public Counterparty(String name, String opf, String address, String inn, String kpp, String ogrn, int branchCount, BranchType branchType, Type type) {
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum BranchType {
        MAIN, BRANCH
    }

    public enum Type {
        LEGAL, INDIVIDUAL
    }
}
