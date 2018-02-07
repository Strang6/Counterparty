package com.strang6.counterparty.ApiServices;

import android.util.Base64;

import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.Logger;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Strang6 on 21.11.2017.
 */

public class DaDataService {
    private DaDataApi daDataApi;

    static {
        System.loadLibrary("keys");
    }

    private native String getDadataKey();

    public DaDataService(DaDataApi daDataApi) {
        Logger.d("DaDataService.DaDataService");
        this.daDataApi = daDataApi;
    }

    public List<Counterparty> findCounterparties(String query) {
        Logger.d("DaDataService.findCounterparties(query = " + query + ")");

        String key = new String(Base64.decode(getDadataKey(), Base64.DEFAULT));
        Call<List<Counterparty>> call = daDataApi.party("Token " + key, new DaDataBody(query));
        List<Counterparty> result = null;
        try {
            Response response = call.execute();
            result = (List<Counterparty>) response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
