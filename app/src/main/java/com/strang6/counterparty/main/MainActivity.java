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

import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.details.DetailsActivity;
import com.strang6.counterparty.Logger;
import com.strang6.counterparty.R;
import com.strang6.counterparty.resent.RecentActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainViewModel.CounterpartyListener, TextWatcher {

    private AutoCompleteTextView counterpartyTextView;
    private MainViewModel viewModel;
    private CounterpartyAutoCompleteAdapter adapter;
    private boolean isSetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("MainActivity.onCreate");
        setContentView(R.layout.activity_main);

        counterpartyTextView = findViewById(R.id.counterpartyTextView);
        counterpartyTextView.setThreshold(4);
        counterpartyTextView.addTextChangedListener(this);
        adapter = new CounterpartyAutoCompleteAdapter(this, R.layout.counterparty_item, new ArrayList<Counterparty>());
        counterpartyTextView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.setCounterpartyListener(this);
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
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Logger.d("MainActivity.onTextChanged");
        if (!isSetText) {
            viewModel.onTextChanged(charSequence);
        }
    }

    @Override
    public void onFindCounterparties(List<Counterparty> counterparties) {
        Logger.d("MainActivity.onFindCounterparties");
        adapter = new CounterpartyAutoCompleteAdapter(this, R.layout.counterparty_item, counterparties);
        counterpartyTextView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Logger.d("MainActivity.beforeTextChanged");
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Logger.d("MainActivity.afterTextChanged");
    }
}
