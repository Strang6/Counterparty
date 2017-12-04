package com.strang6.counterparty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private TextView name, opf, address, inn, kpp, ogrn, branchCount, branchType, type;
    private Counterparty counterparty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        name = findViewById(R.id.name);
        opf = findViewById(R.id.opf);
        address = findViewById(R.id.address);
        inn = findViewById(R.id.inn);
        kpp = findViewById(R.id.kpp);
        ogrn = findViewById(R.id.ogrn);
        branchCount = findViewById(R.id.branchCount);
        branchType = findViewById(R.id.branchType);
        type = findViewById(R.id.type);

        counterparty = getIntent().getExtras().getParcelable("counterparty");

        name.setText(counterparty.getName());
        opf.setText(counterparty.getOpf());
        address.setText(counterparty.getAddress());
        inn.setText(counterparty.getInn());
        kpp.setText(counterparty.getKpp());
        ogrn.setText(counterparty.getOgrn());
        branchCount.setText(Integer.toString(counterparty.getBranchCount()));
        branchType.setText(counterparty.getBranchType() == Counterparty.BranchType.MAIN ? "головная организация" : "филиал");
        type.setText(counterparty.getType() == Counterparty.Type.LEGAL ? "юридическое лицо" : "индивидуальный предприниматель");

        Button mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, MapActivity.class);
                intent.putExtra("address", counterparty.getAddress());
                startActivity(intent);
            }
        });
    }
}
