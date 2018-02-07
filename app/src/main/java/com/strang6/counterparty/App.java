package com.strang6.counterparty;

import android.app.Application;

import com.strang6.counterparty.dagger.app.AppComponent;
import com.strang6.counterparty.dagger.app.AppModule;
import com.strang6.counterparty.dagger.app.DaggerAppComponent;
import com.strang6.counterparty.dagger.screen.DaggerScreenComponent;
import com.strang6.counterparty.dagger.screen.ScreenComponent;

/**
 * Created by Strang6 on 03.02.2018.
 */

public class App extends Application {
    private static App instance;
    private AppComponent appComponent;
    private ScreenComponent screenComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static App get() {
        return instance;
    }

    public ScreenComponent getScreenComponent() {
        if (screenComponent == null) {
            screenComponent = DaggerScreenComponent.builder()
                    .appComponent(appComponent)
                    .build();
        }
        return screenComponent;
    }

    public void clearScreenComponent() {
        screenComponent = null;
    }
}
