package com.strang6.counterparty.ApiServices;

import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strang6.counterparty.ApiServices.deserializers.GeocodingDeserializer;
import com.strang6.counterparty.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Strang6 on 04.12.2017.
 */

public class GeocodingService {
    private final static String URL = "https://maps.googleapis.com/maps/api/";
    private GeocodingApi geocoding;

    static {
        System.loadLibrary("keys");
    }

    private native String getGeocodingKey();

    public GeocodingService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LatLng.class, new GeocodingDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        geocoding = retrofit.create(GeocodingApi.class);
    }

    public LatLng findLatLng(String address) {
        Logger.d("GeocodingService.findLatLng(address = " + address + ")");

        String key = new String(Base64.decode(getGeocodingKey(), Base64.DEFAULT));
        Call<LatLng> call = geocoding.geocode(address, key);
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
