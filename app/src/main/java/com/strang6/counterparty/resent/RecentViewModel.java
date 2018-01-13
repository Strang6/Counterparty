package com.strang6.counterparty.resent;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.strang6.counterparty.Logger;
import com.strang6.counterparty.RecentCounterparty;
import com.strang6.counterparty.database.CounterpartyDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Strang6 on 09.12.2017.
 */

public class RecentViewModel extends AndroidViewModel {
    private List<RecentCounterparty> allData, filterData;
    private CounterpartyDatabase database;
    private DataChangeListener listener;

    public RecentViewModel(@NonNull Application application) {
        super(application);
        Logger.d("RecentViewModel.RecentViewModel");
        database = CounterpartyDatabase.getDatabase(application);
        new LoadAsyncTask(database).execute();
    }

    public List<RecentCounterparty> getData() {
        Logger.d("RecentViewModel.getData");
        return filterData;
    }

    public void setDataChangeListener(DataChangeListener listener) {
        Logger.d("RecentViewModel.setDataChangeListener");
        this.listener = listener;
        if (filterData != null)
            listener.onDataChange(filterData);
    }

    public void resetDataChangeListener() {
        Logger.d("RecentViewModel.resetDataChangeListener");
        this.listener = null;
    }

    public void onSwiped(RecentCounterparty recentCounterparty) {
        Logger.d("RecentViewModel.onSwiped");
        allData.remove(recentCounterparty);
        filterData.remove(recentCounterparty);
        if (listener != null) {
            listener.onDataChange(filterData);
        }
        new DeleteAsyncTask(database).execute(recentCounterparty);
    }

    public void onFavoriteChanged(RecentCounterparty recentCounterparty, boolean isFavorite) {
        Logger.d("RecentViewModel.onFavoriteChanged");
        recentCounterparty.setFavorite(isFavorite);
        sortFilterData();
        if (listener != null) {
            listener.onDataChange(filterData);
        }
        new UpdateAsyncTask(database).execute(recentCounterparty);
    }

    public void onRecentCounterpartyClick(RecentCounterparty recentCounterparty) {
        Logger.d("RecentViewModel.onRecentCounterpartyClick");
        recentCounterparty.setUploadDate(new Date());
        sortFilterData();
        if (listener != null) {
            listener.onDataChange(filterData);
        }
        new UpdateAsyncTask(database).execute(recentCounterparty);
    }

    public void onQueryTextChange(String filter) {
        Logger.d("RecentViewModel.onQueryTextChange");
        if (filter == null || filter.isEmpty()) {
            filterData = allData;
        } else {
            filterData = new ArrayList<>();
            for (RecentCounterparty counterparty: allData) {
                String name = counterparty.getCounterparty().getName();
                String inn = counterparty.getCounterparty().getInn();
                if (name.toLowerCase().contains(filter.toLowerCase()) ||
                        inn.contains(filter)) {
                    filterData.add(counterparty);
                }
            }
        }
        sortFilterData();
        if (listener != null) {
            listener.onDataChange(filterData);
        }
    }

    private void sortFilterData() {
        Logger.d("RecentViewModel.sortFilterData");
        Collections.sort(filterData, new Comparator<RecentCounterparty>() {
            @Override
            public int compare(RecentCounterparty first, RecentCounterparty second) {
                if (first.isFavorite() && second.isFavorite() ||
                        !first.isFavorite() && !second.isFavorite()) {
                    return second.getUploadDate().compareTo(first.getUploadDate());
                } else if (first.isFavorite()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
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

    private class LoadAsyncTask extends AsyncTask<Void, Void, List<RecentCounterparty>> {
        private  CounterpartyDatabase database;

        public LoadAsyncTask(CounterpartyDatabase database) {
            this.database = database;
        }

        @Override
        protected List<RecentCounterparty> doInBackground(Void... voids) {
            Logger.d("RecentViewModel.LoadAsyncTask.doInBackground");
            return database.getRecentCounterpartyDAO().getAllRecentCounterparties();
        }

        @Override
        protected void onPostExecute(List<RecentCounterparty> recentCounterparties) {
            Logger.d("RecentViewModel.LoadAsyncTask.onPostExecute");
            allData = filterData = recentCounterparties;
            sortFilterData();
            if (listener != null)
                listener.onDataChange(filterData);
        }
    }

    public interface DataChangeListener{
        void onDataChange(List<RecentCounterparty> data);
    }
}
