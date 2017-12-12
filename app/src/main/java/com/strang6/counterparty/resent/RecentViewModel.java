package com.strang6.counterparty.resent;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.strang6.counterparty.Logger;
import com.strang6.counterparty.RecentCounterparty;
import com.strang6.counterparty.database.CounterpartyDatabase;

import java.util.Date;
import java.util.List;

/**
 * Created by Strang6 on 09.12.2017.
 */

public class RecentViewModel extends AndroidViewModel {
    private LiveData<List<RecentCounterparty>> data;
    private CounterpartyDatabase database;

    public RecentViewModel(@NonNull Application application) {
        super(application);
        Logger.d("RecentViewModel.RecentViewModel");
        database = CounterpartyDatabase.getDatabase(application);
        data = database.getRecentCounterpartyDAO().getAllRecentCounterparties();
    }

    public LiveData<List<RecentCounterparty>> getData() {
        Logger.d("RecentViewModel.getData");
        return data;
    }

    public void onSwiped(RecentCounterparty recentCounterparty) {
        Logger.d("RecentViewModel.onSwiped");
        new DeleteAsyncTask(database).execute(recentCounterparty);
    }

    public void onFavoriteChanged(RecentCounterparty recentCounterparty, boolean isFavorite) {
        Logger.d("RecentViewModel.onFavoriteChanged");
        recentCounterparty.setFavorite(isFavorite);
        new UpdateAsyncTask(database).execute(recentCounterparty);
    }

    public void onRecentCounterpartyClick(RecentCounterparty recentCounterparty) {
        Logger.d("RecentViewModel.onRecentCounterpartyClick");
        recentCounterparty.setUploadDate(new Date());
        new UpdateAsyncTask(database).execute(recentCounterparty);
    }

    private static class DeleteAsyncTask extends AsyncTask<RecentCounterparty, Void, Void> {
        private  CounterpartyDatabase database;

        private DeleteAsyncTask(CounterpartyDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(RecentCounterparty... recentCounterparties) {
            Logger.d("RecentViewModel.DeleteAsyncTask.doInBackground");
            database.getAddressCoordinatesDAO().deleteAddressById(Integer.toString(recentCounterparties[0].getId()));
            database.getRecentCounterpartyDAO().deleteRecentCounterparty(recentCounterparties[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<RecentCounterparty, Void, Void> {
        private  CounterpartyDatabase database;

        public UpdateAsyncTask(CounterpartyDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(RecentCounterparty... recentCounterparties) {
            Logger.d("RecentViewModel.UpdateAsyncTask.doInBackground");
            database.getRecentCounterpartyDAO().updateItem(recentCounterparties[0]);
            return null;
        }
    }
}
