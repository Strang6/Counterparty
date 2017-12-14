package com.strang6.counterparty.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.strang6.counterparty.ApiServices.DaDataService;
import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.Logger;
import com.strang6.counterparty.RecentCounterparty;
import com.strang6.counterparty.database.CounterpartyDatabase;
import com.strang6.counterparty.database.RecentCounterpartyDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Strang6 on 10.12.2017.
 */

public class MainViewModel extends AndroidViewModel {
    private CounterpartyDatabase database;
    private CounterpartyListener listener;
    private List<Counterparty> result;
    private String req;

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

    public void onCounterpartyClick(Counterparty counterparty) {
        Logger.d("MainViewModel.onCounterpartyClick");
        new AddAsyncTask(database).execute(counterparty);
    }

    public void onTextChanged(CharSequence charSequence) {
        Logger.d("MainViewModel.onTextChanged");
        if (charSequence.length() > 3) {
            if (req != null && req.equals(charSequence.toString())) {
                listener.onFindCounterparties(result);
            }
            else {
                req = charSequence.toString();
                new FindCounterpartiesAsyncTask().execute(req);
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
            RecentCounterpartyDAO dao = database.getRecentCounterpartyDAO();
            RecentCounterparty recentCounterparty = dao
                    .getItemByNameInnKpp(counterparties[0].getName(), counterparties[0].getInn(), counterparties[0].getKpp());
            if (recentCounterparty != null) {
                recentCounterparty.setUploadDate(new Date());
                dao.updateItem(recentCounterparty);
            } else {
                recentCounterparty = new RecentCounterparty(counterparties[0], new Date(), false);
                database.getRecentCounterpartyDAO().addRecentCounterparty(recentCounterparty);
            }
            if (listener != null) {
                String name = recentCounterparty.getCounterparty().getName();
                String inn = recentCounterparty.getCounterparty().getInn();
                String kpp = recentCounterparty.getCounterparty().getKpp();
                recentCounterparty = database
                        .getRecentCounterpartyDAO()
                        .getItemByNameInnKpp(name, inn, kpp);
                listener.onItemAdd(recentCounterparty.getId());
            }
            return null;
        }
    }

    private class FindCounterpartiesAsyncTask extends AsyncTask<String, Void, List<Counterparty>> {

        @Override
        protected List<Counterparty> doInBackground(String... strings) {
            Logger.d("MainViewModel.FindCounterpartiesAsyncTask.doInBackground");
            List<Counterparty> counterparties = new DaDataService().findCounterparties(strings[0]);
            return counterparties;
        }

        @Override
        protected void onPostExecute(List<Counterparty> counterparties) {
            Logger.d("MainViewModel.FindCounterpartiesAsyncTask.onPostExecute");
            result = counterparties;
            listener.onFindCounterparties(counterparties);
        }
    }

    public interface CounterpartyListener {
        void onItemAdd(int id);
        void onFindCounterparties(List<Counterparty> counterparties);
    }
}
