package com.strang6.counterparty.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.strang6.counterparty.ApiServices.DaDataService;
import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.Logger;
import com.strang6.counterparty.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Strang6 on 06.11.2017.
 */

public class CounterpartyAutoCompleteAdapter extends BaseAdapter implements Filterable {
    private final Context context;
    private List<Counterparty> result;

    public CounterpartyAutoCompleteAdapter(Context context) {
        this.context = context;
        this.result = new ArrayList<>();
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
                    .inflate(R.layout.counterparty_item, viewGroup, false);
        }
        Counterparty counterparty = getItem(i);
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        nameTextView.setText(counterparty.getName());
        TextView innTextView = view.findViewById(R.id.innTextView);
        innTextView.setText(counterparty.getInn());
        return view;
    }

    @Override
    public Filter getFilter() {
        return new CounterpartyFilter();
    }

    private class CounterpartyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Logger.d("CounterpartyAutoCompleteAdapter.CounterpartyFilter.performFiltering");
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                List<Counterparty> counterparties = new DaDataService().findCounterparties(constraint.toString());
                filterResults.values = counterparties;
                filterResults.count = counterparties.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            Logger.d("CounterpartyAutoCompleteAdapter.CounterpartyFilter.publishResults");
            if (filterResults != null && filterResults.count > 0) {
                result = (List<Counterparty>) filterResults.values;
                notifyDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        }
    }
}
