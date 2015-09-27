package com.oyeoye.merchant.presentation.deals.my_deals;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.business.api.entity.Deal;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MyDealsAdapter extends RecyclerView.Adapter<MyDealsAdapter.ViewHolder> {

    private List<Deal> mDeals;
    private OnClickListener mOnClickListener;

    public MyDealsAdapter(OnClickListener onClickListener) {
        mDeals = new ArrayList<>();
        mOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_deal, parent, false);
        return new ViewHolder(itemView, mOnClickListener);
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
        private OnClickListener mOnClickListener;
        private View mItemView;

        public ViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView);
            mItemView = itemView;
            this.mOnClickListener = onClickListener;
            ButterKnife.bind(this, itemView);
        }

        public void setup(final Deal deal) {
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClickDeak(deal);
                    }
                }
            });
        }
    }

    public interface OnClickListener {
        void onClickDeak(Deal deal);
    }
}
