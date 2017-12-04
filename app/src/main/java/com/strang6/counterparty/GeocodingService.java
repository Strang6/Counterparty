package com.strang6.counterparty;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Strang6 on 04.12.2017.
 */

public class GeocodingService {
    private final static String API_KEY = "AIzaSyB6BoBZZ2mItJ7xtF8GuVRiewo0-ZRwp9Q",
    url = "https://maps.googleapis.com/maps/api/geocode/";

    public LatLng findLatLng(String address) {
        OkHttpClient client = new OkHttpClient();
        String req = url + "json?address=";
        try {
            req = req + URLEncoder.encode(address, "UTF-8") + "&key=" + API_KEY;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder().url(req).build();
        LatLng latLng = null;
        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            latLng = jsonToLatLng(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }

    private LatLng jsonToLatLng(String json) {
        LatLng latLng = null;
        Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new GeocodingDeserializer()).create();
        latLng = gson.fromJson(json, LatLng.class);
        return latLng;
    }
}
