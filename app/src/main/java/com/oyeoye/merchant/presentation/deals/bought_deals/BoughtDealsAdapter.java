package com.oyeoye.merchant.presentation.deals.bought_deals;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.business.api.entity.Deal;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class BoughtDealsAdapter extends RecyclerView.Adapter<BoughtDealsAdapter.ViewHolder> {

    // TODO
    private List<Deal> mDeals;

    public BoughtDealsAdapter() {
        mDeals = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bought_deal, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Deal deal = mDeals.get(position);
        holder.setup(deal);
    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }

    public void setList(List<Deal> deals) {
        mDeals = deals;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setup(final Deal deal) {
            // TODO
        }
    }
}
