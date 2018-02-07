package com.strang6.counterparty.dagger.screen;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.strang6.counterparty.ApiServices.deserializers.CounterpartyListDeserializer;
import com.strang6.counterparty.ApiServices.deserializers.GeocodingDeserializer;
import com.strang6.counterparty.Counterparty;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Strang6 on 03.02.2018.
 */

@Module
public class NetworkModule {

    @Provides
    @Named("dadataUrl")
    String provideDadataUrl() {
        return "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/";
    }

    @Provides
    @Named("geocodingUrl")
    String provideGeocodingUrl() {
        return "https://maps.googleapis.com/maps/api/";
    }

    @Provides
    @ScreenScope
    @Named("CounterpartyGson")
    Gson provideCounterpartyGson() {
        Type type = new TypeToken<List<Counterparty>>() {}.getType();
        return new GsonBuilder()
                .registerTypeAdapter(type, new CounterpartyListDeserializer())
                .create();
    }

    @Provides
    @ScreenScope
    @Named("GeocodingGson")
    Gson provideGeocodingGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LatLng.class, new GeocodingDeserializer())
                .create();
    }

    @Provides
    @ScreenScope
    @Named("GeocodingRetrofit")
    Retrofit provideGeocodingRetrofit(@Named("geocodingUrl") String url, OkHttpClient client, @Named("GeocodingGson") Gson gson) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    @Provides
    @ScreenScope
    @Named("CounterpartyRetrofit")
    Retrofit provideCounterpartyRetrofit(@Named("dadataUrl") String url, OkHttpClient client,  @Named("CounterpartyGson") Gson gson) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @ScreenScope
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }
}
