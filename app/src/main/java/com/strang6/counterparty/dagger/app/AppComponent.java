package com.strang6.counterparty.dagger.app;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Strang6 on 03.02.2018.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Context context();
}
