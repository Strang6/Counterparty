package com.strang6.counterparty.details;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.strang6.counterparty.Logger;
import com.strang6.counterparty.RecentCounterparty;
import com.strang6.counterparty.database.CounterpartyDatabase;

/**
 * Created by Strang6 on 11.12.2017.
 */

public class DetailsViewModel extends ViewModel {
    private CounterpartyDatabase database;
    private RecentCounterparty recentCounterparty;
    private LoadDataListener loadDataListener;
    private int id;

    public DetailsViewModel(CounterpartyDatabase database) {
        Logger.d("DetailsViewModel.DetailsViewModel");
        this.database = database;
    }

    public RecentCounterparty getRecentCounterparty() {
        Logger.d("DetailsViewModel.getRecentCounterparty");
        return recentCounterparty;
    }

    public void setId(int id) {
        Logger.d("DetailsViewModel.setId(int id = " + id + ")");
        if (this.id != id) {
            this.id = id;
            new GetItemAsyncTask(database).execute(id);
        }
    }

    public void setLoadDataListener(LoadDataListener listener) {
        Logger.d("DetailsViewModel.setLoadDataListener");
        loadDataListener = listener;
        if (recentCounterparty != null)
            loadDataListener.onLoadData(recentCounterparty);
    }

    public void resetLoadDataListener() {
        Logger.d("DetailsViewModel.resetLoadDataListener");
        loadDataListener = null;
    }

    public void onFavoriteChanged(boolean isFavorite) {
        Logger.d("DetailsViewModel.onFavoriteChanged");
        recentCounterparty.setFavorite(isFavorite);
        new UpdateAsyncTask(database).execute(recentCounterparty);
    }

    private static class UpdateAsyncTask extends AsyncTask<RecentCounterparty, Void, Void> {
        private  CounterpartyDatabase database;

        private UpdateAsyncTask(CounterpartyDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(RecentCounterparty... recentCounterparties) {
            Logger.d("DetailsViewModel.UpdateAsyncTask.doInBackground");
            database.getRecentCounterpartyDAO().updateItem(recentCounterparties[0]);
            return null;
        }
    }

    private class GetItemAsyncTask extends AsyncTask<Integer, Void, RecentCounterparty> {
        private  CounterpartyDatabase database;

        private GetItemAsyncTask(CounterpartyDatabase database) {
            this.database = database;
        }

        @Override
        protected RecentCounterparty doInBackground(Integer... id) {
            Logger.d("DetailsViewModel.GetItemAsyncTask.doInBackground : id[0] = " + id[0]);
            return database.getRecentCounterpartyDAO().getItemById(Integer.toString(id[0]));
        }

        @Override
        protected void onPostExecute(RecentCounterparty recentCounterparty) {
            Logger.d("DetailsViewModel.GetItemAsyncTask.onPostExecute: recentCounterparty = " +
                    recentCounterparty.getCounterparty().toString());
            DetailsViewModel.this.recentCounterparty = recentCounterparty;
            loadDataListener.onLoadData(recentCounterparty);
        }
    }

    public interface LoadDataListener {
        void onLoadData(RecentCounterparty recentCounterparty);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private CounterpartyDatabase database;

        public Factory(CounterpartyDatabase database) {
            this.database = database;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new DetailsViewModel(database);
        }
    }
}
