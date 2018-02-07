package com.strang6.counterparty.dagger.screen;

import com.strang6.counterparty.dagger.app.AppComponent;
import com.strang6.counterparty.details.DetailsActivity;
import com.strang6.counterparty.main.MainActivity;
import com.strang6.counterparty.map.MapActivity;
import com.strang6.counterparty.resent.RecentActivity;

import dagger.Component;

/**
 * Created by Strang6 on 07.02.2018.
 */

@Component(dependencies = {AppComponent.class}, modules = {ServiceModule.class, NetworkModule.class, DatabaseModule.class})
@ScreenScope
public interface ScreenComponent {
    void inject(MainActivity mainActivity);
    void inject(DetailsActivity detailsActivity);
    void inject(MapActivity mapActivity);
    void inject(RecentActivity recentActivity);
}
