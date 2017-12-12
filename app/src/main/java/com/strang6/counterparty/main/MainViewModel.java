package com.strang6.counterparty.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.Logger;
import com.strang6.counterparty.RecentCounterparty;
import com.strang6.counterparty.database.CounterpartyDatabase;
import com.strang6.counterparty.database.RecentCounterpartyDAO;

import java.util.Date;

/**
 * Created by Strang6 on 10.12.2017.
 */

public class MainViewModel extends AndroidViewModel {
    private CounterpartyDatabase database;
    private AddItemListener addItemListener;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Logger.d("MainViewModel.MainViewModel");
        database = CounterpartyDatabase.getDatabase(application);
    }

    public void setAddItemListener(AddItemListener addItemListener) {
        Logger.d("MainViewModel.setAddItemListener");
        this.addItemListener = addItemListener;
    }

    public void onCounterpartyClick(Counterparty counterparty) {
        Logger.d("MainViewModel.onCounterpartyClick");
        new AddAsyncTask(database).execute(counterparty);
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
            if (addItemListener != null) {
                String name = recentCounterparty.getCounterparty().getName();
                String inn = recentCounterparty.getCounterparty().getInn();
                String kpp = recentCounterparty.getCounterparty().getKpp();
                recentCounterparty = database
                        .getRecentCounterpartyDAO()
                        .getItemByNameInnKpp(name, inn, kpp);
                addItemListener.onItemAdd(recentCounterparty.getId());
            }
            return null;
        }
    }

    public interface AddItemListener {
        void onItemAdd(int id);
    }
}
