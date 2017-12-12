package com.strang6.counterparty.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.Logger;
import com.strang6.counterparty.R;
import com.strang6.counterparty.RecentCounterparty;
import com.strang6.counterparty.map.MapActivity;

public class DetailsActivity extends AppCompatActivity implements DetailsViewModel.LoadDataListener {

    private TextView nameTextView, opfTextView, addressTextView, innTextView, kppTextView,
            ogrnTextView, branchCountTextView, branchTypeTextView, typeTextView;
    private Button mapButton;
    private DetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.d("DetailsActivity.onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        nameTextView = findViewById(R.id.name);
        opfTextView = findViewById(R.id.opf);
        addressTextView = findViewById(R.id.address);
        innTextView = findViewById(R.id.inn);
        kppTextView = findViewById(R.id.kpp);
        ogrnTextView = findViewById(R.id.ogrn);
        branchCountTextView = findViewById(R.id.branchCount);
        branchTypeTextView = findViewById(R.id.branchType);
        typeTextView = findViewById(R.id.type);

        mapButton = findViewById(R.id.mapButton);
        final int id = getIntent().getExtras().getInt("id");
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, MapActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        viewModel.setId(id);
        viewModel.setLoadDataListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Logger.d("DetailsActivity.onOptionsItemSelected");

        if (item.getItemId() ==  R.id.favorite_check) {
            item.setChecked(!item.isChecked());
            invalidateOptionsMenu();
            viewModel.onFavoriteChanged(item.isChecked());
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Logger.d("DetailsActivity.onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Logger.d("DetailsActivity.onPrepareOptionsMenu");

        RecentCounterparty recentCounterparty = viewModel.getRecentCounterparty();
        if (recentCounterparty != null) {
            MenuItem item = menu.findItem(R.id.favorite_check);
            item.setChecked(recentCounterparty.isFavorite());
            if (item.isChecked()) {
                item.setIcon(R.drawable.btn_star_on);
                item.setTitle(R.string.favorite_delete);
            } else {
                item.setIcon(R.drawable.btn_star_off);
                item.setTitle(R.string.favorite_add);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        Logger.d("DetailsActivity.onStop");

        super.onStop();
        viewModel.resetLoadDataListener();
    }

    @Override
    public void onLoadData(RecentCounterparty recentCounterparty) {
        Logger.d("DetailsActivity.onLoadData");

        Counterparty counterparty = recentCounterparty.getCounterparty();
        nameTextView.setText(counterparty.getName());
        opfTextView.setText(counterparty.getOpf());
        addressTextView.setText(counterparty.getAddress());
        innTextView.setText(counterparty.getInn());
        kppTextView.setText(counterparty.getKpp());
        ogrnTextView.setText(counterparty.getOgrn());
        branchCountTextView.setText(Integer.toString(counterparty.getBranchCount()));
        branchTypeTextView.setText(counterparty.getBranchType() == Counterparty.BranchType.MAIN ? "головная организация" : "филиал");
        typeTextView.setText(counterparty.getType() == Counterparty.OrganizationType.LEGAL ? "юридическое лицо" : "индивидуальный предприниматель");

        invalidateOptionsMenu();
    }

    public void onSendClick(View view) {
        Logger.d("DetailsActivity.onSendClick");

        String text = viewModel.getRecentCounterparty().getCounterparty().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        startActivity(intent);
    }
}
