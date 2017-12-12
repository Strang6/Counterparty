package com.strang6.counterparty.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.details.DetailsActivity;
import com.strang6.counterparty.Logger;
import com.strang6.counterparty.R;
import com.strang6.counterparty.resent.RecentActivity;

public class MainActivity extends AppCompatActivity implements MainViewModel.AddItemListener {

    private AutoCompleteTextView counterpartyTextView;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("MainActivity.onCreate");
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.setAddItemListener(this);

        counterpartyTextView = findViewById(R.id.counterpartyTextView);
        counterpartyTextView.setThreshold(4);
        counterpartyTextView.setAdapter(new CounterpartyAutoCompleteAdapter(this));
        counterpartyTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Counterparty counterparty = (Counterparty) adapterView.getItemAtPosition(i);
                counterpartyTextView.setText(counterparty.getName());
                viewModel.onCounterpartyClick(counterparty);
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
}
