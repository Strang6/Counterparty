package com.strang6.counterparty.ApiServices;

import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Strang6 on 10.01.2018.
 */

public interface GeocodingApi {
    @GET("geocode/json")
    Call<LatLng> geocode(@Query("address") String address, @Query("key") String key);
}
