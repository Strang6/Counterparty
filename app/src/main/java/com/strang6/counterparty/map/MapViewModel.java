package com.strang6.counterparty.map;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.strang6.counterparty.AddressCoordinates;
import com.strang6.counterparty.ApiServices.GeocodingService;
import com.strang6.counterparty.Logger;
import com.strang6.counterparty.RecentCounterparty;
import com.strang6.counterparty.database.CounterpartyDatabase;

/**
 * Created by Strang6 on 12.12.2017.
 */

public class MapViewModel extends AndroidViewModel {
    private CounterpartyDatabase database;
    private LoadCoordinatesListener listener;
    private int id;
    private LatLng latLng;
    private boolean isLoad;

    public MapViewModel(@NonNull Application application) {
        super(application);
        Logger.d("MapViewModel.MapViewModel");
        database = CounterpartyDatabase.getDatabase(application);
        isLoad = false;
    }

    public void setId(int id) {
        Logger.d("MapViewModel.setId(id = " + id + ")");
        if (this.id != id) {
            this.id = id;
            new LoadCoordinatesTask(database).execute(id);
        }
    }

    public void setLoadCoordinatesListener(LoadCoordinatesListener listener) {
        Logger.d("MapViewModel.setLoadCoordinatesListener: isLoad = " + isLoad);
        this.listener = listener;
        if (isLoad)
            listener.onLoadCoordinates(latLng);
    }

    private class LoadCoordinatesTask extends AsyncTask<Integer, Void, LatLng> {
        private CounterpartyDatabase database;

        private LoadCoordinatesTask(CounterpartyDatabase database) {
            this.database = database;
        }

        @Override
        protected LatLng doInBackground(Integer... id) {
            Logger.d("MapViewModel.LoadCoordinatesTask.doInBackground");
            LatLng latLng = database.getAddressCoordinatesDAO().getCoordinatesById(id[0].toString());
            if (latLng == null) {
                RecentCounterparty recentCounterparty = database.getRecentCounterpartyDAO().getItemById(id[0].toString());
                latLng = new GeocodingService().findLatLng(recentCounterparty.getCounterparty().getAddress());
                if (latLng != null) {
                    AddressCoordinates coordinates = new AddressCoordinates(id[0], latLng);
                    database.getAddressCoordinatesDAO().addCoordinates(coordinates);
                }
            }
            return latLng;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            Logger.d("MapViewModel.LoadCoordinatesTask.onPostExecute");
            MapViewModel.this.latLng = latLng;
            isLoad = true;
            if (listener != null)
                listener.onLoadCoordinates(latLng);
        }
    }

    public interface LoadCoordinatesListener {
        void onLoadCoordinates(LatLng latLng);
    }
}
