package com.strang6.counterparty.ApiServices;

/**
 * Created by Strang6 on 09.01.2018.
 */

public class DaDataBody {
    private String query;

    public DaDataBody(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
