package com.strang6.counterparty.ApiServices;

import com.strang6.counterparty.Counterparty;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Strang6 on 09.01.2018.
 */

public interface DaDataApi {
    @POST("party")
    @Headers({"Content-Type: application/json",
            "Accept: application/json"})
    Call<List<Counterparty>> party(@Header("Authorization") String key, @Body DaDataBody body);
}
