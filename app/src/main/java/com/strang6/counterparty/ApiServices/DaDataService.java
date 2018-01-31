package com.strang6.counterparty.ApiServices;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.ApiServices.deserializers.CounterpartyListDeserializer;
import com.strang6.counterparty.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Strang6 on 21.11.2017.
 */

public class DaDataService {
    private static final String URL = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/";
    private DaDataApi daData;

    static {
        System.loadLibrary("keys");
    }

    private native String getDadataKey();

    public DaDataService() {
        Logger.d("DaDataService.DaDataService");

        OkHttpClient client = new OkHttpClient.Builder()
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Type type = new TypeToken<List<Counterparty>>() {}.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(type, new CounterpartyListDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        daData = retrofit.create(DaDataApi.class);
    }

    public List<Counterparty> findCounterparties(String query) {
        Logger.d("DaDataService.findCounterparties(query = " + query + ")");

        String key = new String(Base64.decode(getDadataKey(), Base64.DEFAULT));
        Call<List<Counterparty>> call = daData.party("Token " + key, new DaDataBody(query));
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
