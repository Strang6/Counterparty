package com.strang6.counterparty.ApiServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.ApiServices.deserializers.CounterpartyListDeserializer;
import com.strang6.counterparty.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Strang6 on 21.11.2017.
 */

public class DaDataService {
    private static final String url = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/party",
            token = "84de1dfbc2cd5f109e9e4f3f8a283c1f64dfda33";

    public List<Counterparty> findCounterparties(String query) {
        Logger.d("DaDataService.findCounterparties(query = " + query + ")");

        String content = "{ \"query\": \"" + query + "\" }";
        MediaType contentType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(contentType, content);
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Authorization", "Token " + token)
                .post(body)
                .build();
        String result = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonToList(result);
    }

    private List<Counterparty> jsonToList(String json) {
        Logger.d("DaDataService.jsonToList(json = " + json + ")");

        Type type = new TypeToken<List<Counterparty>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(type, new CounterpartyListDeserializer())
                .create();
        return gson.fromJson(json, type);
    }
}
