package com.strang6.counterparty.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.strang6.counterparty.AddressCoordinates;
import com.strang6.counterparty.ApiServices.DaDataService;
import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.Logger;
import com.strang6.counterparty.RecentCounterparty;
import com.strang6.counterparty.database.CounterpartyDatabase;
import com.strang6.counterparty.database.RecentCounterpartyDAO;

import java.util.Date;
import java.util.List;

/**
 * Created by Strang6 on 10.12.2017.
 */

public class MainViewModel extends AndroidViewModel {
    private CounterpartyDatabase database;
    private CounterpartyListener listener;
    private List<Counterparty> result;
    private Handler handler;
    private final int TEXT_CHANGED = 0;
    private final int delay = 1000;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Logger.d("MainViewModel.MainViewModel");
        database = CounterpartyDatabase.getDatabase(application);
    }

    public void setCounterpartyListener(CounterpartyListener counterpartyListener) {
        Logger.d("MainViewModel.setCounterpartyListener");
        this.listener = counterpartyListener;
        if (result != null)
            listener.onFindCounterparties(result);
    }

    public void resetCounterpartyListener() {
        Logger.d("MainViewModel.resetCounterpartyListener");
        listener = null;
    }

    public void onCounterpartyClick(Counterparty counterparty) {
        Logger.d("MainViewModel.onCounterpartyClick");
        new AddAsyncTask(database).execute(counterparty);
    }

    public void onTextChanged(CharSequence charSequence) {
        Logger.d("MainViewModel.onTextChanged: " + charSequence);
        if (charSequence.length() > 3) {
            if (handler == null) {
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Logger.d("MainViewModel.Handler.handleMessage, obj = " + msg.obj);
                        if (msg.what == TEXT_CHANGED) {
                            new FindCounterpartiesAsyncTask().execute((String) msg.obj);
                        }
                    }
                };
            } else {
                handler.removeMessages(TEXT_CHANGED);
            }
            Message msg = handler.obtainMessage(TEXT_CHANGED, charSequence.toString());
            Logger.d("MainViewModel.onTextChanged: handler.sendMessageDelayed, obj = " + msg.obj);
            handler.sendMessageDelayed(msg, delay);
        } else {
            if (handler != null) {
                handler.removeMessages(TEXT_CHANGED);
            }
        }
    }

    private class AddAsyncTask extends AsyncTask<Counterparty, Void, Void> {
        private CounterpartyDatabase database;

        private AddAsyncTask(CounterpartyDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(Counterparty... counterparties) {
            Logger.d("MainViewModel.AddAsyncTask.doInBackground");

            Counterparty counterparty = counterparties[0];
            String name = counterparty.getName();
            String inn = counterparty.getInn();
            String kpp = counterparty.getKpp();

            RecentCounterpartyDAO dao = database.getRecentCounterpartyDAO();
            RecentCounterparty recentCounterparty = dao.getItemByNameInnKpp(name, inn, kpp);

            if (recentCounterparty != null) {
                recentCounterparty.setUploadDate(new Date());
                dao.updateItem(recentCounterparty);
            } else {
                recentCounterparty = new RecentCounterparty(counterparty, new Date(), false);
                dao.addRecentCounterparty(recentCounterparty);
                recentCounterparty = dao.getItemByNameInnKpp(name, inn, kpp);
                if (counterparty.getLatLng() != null) {
                    AddressCoordinates coordinates = new AddressCoordinates(recentCounterparty.getId(), counterparty.getLatLng());
                    database.getAddressCoordinatesDAO().addCoordinates(coordinates);
                }
            }

            if (listener != null) {
                listener.onItemAdd(recentCounterparty.getId());
            }
            return null;
        }
    }

    private class FindCounterpartiesAsyncTask extends AsyncTask<String, Void, List<Counterparty>> {

        @Override
        protected List<Counterparty> doInBackground(String... strings) {
            Logger.d("MainViewModel.FindCounterpartiesAsyncTask.doInBackground");
            return new DaDataService().findCounterparties(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Counterparty> counterparties) {
            Logger.d("MainViewModel.FindCounterpartiesAsyncTask.onPostExecute");
            result = counterparties;
            if (listener != null)
                listener.onFindCounterparties(counterparties);
        }
    }

    public interface CounterpartyListener {
        void onItemAdd(int id);
        void onFindCounterparties(List<Counterparty> counterparties);
    }
}
