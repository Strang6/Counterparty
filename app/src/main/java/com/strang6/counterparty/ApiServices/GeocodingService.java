package com.strang6.counterparty.ApiServices;

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
    private final static String API_KEY = "AIzaSyB6BoBZZ2mItJ7xtF8GuVRiewo0-ZRwp9Q",
    URL = "https://maps.googleapis.com/maps/api/";
    private GeocodingApi geocoding;

    public GeocodingService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
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

        Call<LatLng> call = geocoding.geocode(address, API_KEY);
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
