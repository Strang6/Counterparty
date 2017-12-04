package com.strang6.counterparty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView counterpartyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counterpartyTextView = findViewById(R.id.counterpartyTextView);
        counterpartyTextView.setThreshold(4);
        counterpartyTextView.setAdapter(new CounterpartyAutoCompleteAdapter(this));
        counterpartyTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Counterparty counterparty = (Counterparty) adapterView.getItemAtPosition(i);
                counterpartyTextView.setText(counterparty.getName());
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("counterparty", counterparty);
                startActivity(intent);
            }
        });

    }
}
