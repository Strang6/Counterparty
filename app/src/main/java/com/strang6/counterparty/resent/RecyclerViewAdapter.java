package com.strang6.counterparty.resent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.strang6.counterparty.Logger;
import com.strang6.counterparty.R;
import com.strang6.counterparty.RecentCounterparty;

import java.util.List;

/**
 * Created by Strang6 on 09.12.2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<RecentCounterparty> data;
    private View.OnClickListener onViewClickListener, onFavoriteClickListener;

    public RecyclerViewAdapter(List<RecentCounterparty> data, View.OnClickListener onViewClickListener,
                               View.OnClickListener onFavoriteClickListener) {
        this.data = data;
        this.onViewClickListener = onViewClickListener;
        this.onFavoriteClickListener = onFavoriteClickListener;
    }

    public void setData(List<RecentCounterparty> data) {
        Logger.d("RecyclerViewAdapter.setData");
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_counterparty_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecentCounterparty recentCounterparty = data.get(position);
        holder.nameTextView.setText(recentCounterparty.getCounterparty().getName());
        holder.innTextView.setText(recentCounterparty.getCounterparty().getInn());
        holder.favoriteCheckBox.setChecked(recentCounterparty.isFavorite());
        holder.favoriteCheckBox.setOnClickListener(onFavoriteClickListener);
        holder.favoriteCheckBox.setTag(recentCounterparty);
        holder.itemView.setTag(recentCounterparty);
        holder.itemView.setOnClickListener(onViewClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, innTextView;
        private CheckBox favoriteCheckBox;

        ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            innTextView = view.findViewById(R.id.innTextView);
            favoriteCheckBox = view.findViewById(R.id.favoriteCheckBox);
        }
    }
}
