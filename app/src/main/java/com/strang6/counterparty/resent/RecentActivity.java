package com.strang6.counterparty.resent;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.strang6.counterparty.App;
import com.strang6.counterparty.Logger;
import com.strang6.counterparty.database.CounterpartyDatabase;
import com.strang6.counterparty.details.DetailsActivity;
import com.strang6.counterparty.R;
import com.strang6.counterparty.RecentCounterparty;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecentActivity extends AppCompatActivity implements RecentViewModel.DataChangeListener{

    private final int DETAILS_REQ = 1;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecentViewModel viewModel;

    @Inject
    CounterpartyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("RecentActivity.onCreate");
        setContentView(R.layout.activity_recent);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        adapter = new RecyclerViewAdapter(new ArrayList<RecentCounterparty>(),
                new OnViewClickListener(), new OnFavoriteClickListener());
        recyclerView.setAdapter(adapter);

        App.get().getScreenComponent().inject(this);
        RecentViewModel.Factory factory = new RecentViewModel
                .Factory(database);
        viewModel = ViewModelProviders.of(this, factory).get(RecentViewModel.class);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Logger.d("RecentActivity: onSwiped");
                RecentCounterparty recentCounterparty = (RecentCounterparty) viewHolder.itemView.getTag();
                viewModel.onSwiped(recentCounterparty);
            }
        });

        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        Logger.d("RecentActivity.onResume");
        super.onResume();
        viewModel.setDataChangeListener(this);
    }

    @Override
    protected void onPause() {
        Logger.d("RecentActivity.onPause");
        super.onPause();
        viewModel.resetDataChangeListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Logger.d("RecentActivity.onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                viewModel.onQueryTextChange(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                viewModel.onQueryTextChange(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onDataChange(List<RecentCounterparty> data) {
        Logger.d("RecentActivity.onDataChange");
        adapter.setData(data);
    }


    class OnViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Logger.d("RecentActivity: OnViewClick");
            RecentCounterparty recentCounterparty = (RecentCounterparty) view.getTag();
            viewModel.onRecentCounterpartyClick(recentCounterparty);
            Intent intent = new Intent(RecentActivity.this, DetailsActivity.class);
            intent.putExtra("id", recentCounterparty.getId());
            startActivityForResult(intent, DETAILS_REQ);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d("RecentActivity: onActivityResult(" + requestCode + "," + resultCode + ")");
        if (requestCode == DETAILS_REQ) {
            if (resultCode == DetailsActivity.RESULT_CHANGE) {
                int id = data.getExtras().getInt(DetailsActivity.ID);
                viewModel.onFavoriteChanged(id);
            }
        }
    }

    class OnFavoriteClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Logger.d("RecentActivity: OnFavoriteClick");
            CheckBox checkBox = (CheckBox) view;
            RecentCounterparty recentCounterparty = (RecentCounterparty) checkBox.getTag();
            viewModel.onFavoriteChanged(recentCounterparty, checkBox.isChecked());
        }
    }
}
