package com.strang6.counterparty.dagger.screen;

import com.strang6.counterparty.ApiServices.DaDataApi;
import com.strang6.counterparty.ApiServices.DaDataService;
import com.strang6.counterparty.ApiServices.GeocodingApi;
import com.strang6.counterparty.ApiServices.GeocodingService;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Strang6 on 07.02.2018.
 */

@Module
public class ServiceModule {
    @Provides
    @ScreenScope
    DaDataService provideDaDataService(DaDataApi daDataApi) {
        return new DaDataService(daDataApi);
    }

    @Provides
    @ScreenScope
    DaDataApi provideDaDataApi(@Named("CounterpartyRetrofit") Retrofit retrofit) {
        return retrofit.create(DaDataApi.class);
    }

    @Provides
    @ScreenScope
    GeocodingService provideGeocodingService(GeocodingApi geocodingApi) {
        return new GeocodingService(geocodingApi);
    }

    @Provides
    @ScreenScope
    GeocodingApi provideGeocodingApi(@Named("GeocodingRetrofit") Retrofit retrofit) {
        return retrofit.create(GeocodingApi.class);
    }
}
