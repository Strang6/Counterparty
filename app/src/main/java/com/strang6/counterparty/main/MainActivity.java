package com.strang6.counterparty.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.strang6.counterparty.ApiServices.DaDataService;
import com.strang6.counterparty.App;
import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.database.CounterpartyDatabase;
import com.strang6.counterparty.details.DetailsActivity;
import com.strang6.counterparty.Logger;
import com.strang6.counterparty.R;
import com.strang6.counterparty.resent.RecentActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainViewModel.CounterpartyListener {

    private AutoCompleteTextView counterpartyTextView;
    private CounterpartyAutoCompleteAdapter adapter;
    private boolean isSetText;

    @Inject
    CounterpartyDatabase database;
    @Inject
    DaDataService daDataService;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("MainActivity.onCreate");
        setContentView(R.layout.activity_main);

        counterpartyTextView = findViewById(R.id.counterpartyTextView);
        adapter = new CounterpartyAutoCompleteAdapter(this, R.layout.counterparty_item, new ArrayList<Counterparty>());
        counterpartyTextView.setAdapter(adapter);

        App.get().getScreenComponent().inject(this);

        MainViewModel.Factory factory = new MainViewModel.Factory(database, daDataService);
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        counterpartyTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isSetText = true;
                Counterparty counterparty = (Counterparty) adapterView.getItemAtPosition(i);
                counterpartyTextView.setText(counterparty.getName());
                viewModel.onCounterpartyClick(counterparty);
                isSetText = false;
            }
        });

        counterpartyTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Logger.d("MainActivity.onTextChanged");
                if (!isSetText) {
                    viewModel.onTextChanged(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d("MainActivity.onResume");
        viewModel.setCounterpartyListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("MainActivity.onPause");
        viewModel.resetCounterpartyListener();
    }

    public void onRecentButtonClick(View view) {
        Logger.d("MainActivity.onRecentButtonClick");
        Intent intent = new Intent(this, RecentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemAdd(int id) {
        Logger.d("MainActivity.onItemAdd; id = " + id);
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void onFindCounterparties(List<Counterparty> counterparties) {
        Logger.d("MainActivity.onFindCounterparties");
        if (counterparties != null) {
            adapter.setResult(counterparties);
        } else {
            Toast.makeText(this, "Не удалось выполнить запрос", Toast.LENGTH_SHORT).show();
        }
    }
}
