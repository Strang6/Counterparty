package com.strang6.counterparty.resent;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.strang6.counterparty.Logger;
import com.strang6.counterparty.details.DetailsActivity;
import com.strang6.counterparty.R;
import com.strang6.counterparty.RecentCounterparty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecentViewModel viewModel;

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

        viewModel = ViewModelProviders.of(this).get(RecentViewModel.class);
        viewModel.getData().observe(this, new Observer<List<RecentCounterparty>>() {
            @Override
            public void onChanged(@Nullable List<RecentCounterparty> counterparties) {
                Logger.d("RecentActivity: observer onChanged");
                Collections.sort(counterparties, new Comparator<RecentCounterparty>() {
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
                adapter.setData(counterparties);
            }
        });

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

    class OnViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Logger.d("RecentActivity: OnViewClick");
            RecentCounterparty recentCounterparty = (RecentCounterparty) view.getTag();
            viewModel.onRecentCounterpartyClick(recentCounterparty);
            Intent intent = new Intent(RecentActivity.this, DetailsActivity.class);
            intent.putExtra("id", recentCounterparty.getId());
            startActivity(intent);
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
