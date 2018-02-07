package com.strang6.counterparty.ApiServices;

import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.strang6.counterparty.Logger;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Strang6 on 04.12.2017.
 */

public class GeocodingService {
    private GeocodingApi geocodingApi;

    static {
        System.loadLibrary("keys");
    }

    private native String getGeocodingKey();

    public GeocodingService(GeocodingApi geocodingApi) {
        Logger.d("GeocodingService.GeocodingService");
        this.geocodingApi = geocodingApi;
    }

    public LatLng findLatLng(String address) {
        Logger.d("GeocodingService.findLatLng(address = " + address + ")");

        String key = new String(Base64.decode(getGeocodingKey(), Base64.DEFAULT));
        Call<LatLng> call = geocodingApi.geocode(address, key);
        LatLng result = null;
        try {
            Response response = call.execute();
            result = (LatLng) response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
