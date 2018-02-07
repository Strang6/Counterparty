package com.strang6.counterparty.dagger.screen;

import android.content.Context;

import com.strang6.counterparty.database.CounterpartyDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Strang6 on 07.02.2018.
 */

@Module
public class DatabaseModule {
    @Provides
    @ScreenScope
    CounterpartyDatabase provideDatabase(Context context) {
        return CounterpartyDatabase.getDatabase(context);
    }
}
