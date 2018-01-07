package com.strang6.counterparty.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.R;

import java.util.List;

/**
 * Created by Strang6 on 06.11.2017.
 */

public class CounterpartyAutoCompleteAdapter extends ArrayAdapter<Counterparty> {
    private final Context context;
    private List<Counterparty> result;
    private int layoutResourceId;

    public CounterpartyAutoCompleteAdapter(Context context, int layoutResourceId, List<Counterparty> result) {
        super(context, layoutResourceId, result);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.result = result;
    }

    public void setResult(List<Counterparty> result) {
        this.result = result;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Counterparty getItem(int i) {
        return result.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater
                    .from(context)
                    .inflate(layoutResourceId, viewGroup, false);
        }
        Counterparty counterparty = getItem(i);
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        nameTextView.setText(counterparty.getName());
        TextView innTextView = view.findViewById(R.id.innTextView);
        innTextView.setText(counterparty.getInn());
        return view;
    }
}
