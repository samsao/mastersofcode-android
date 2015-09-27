package com.oyeoye.merchant.presentation.deals.bought_deals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.business.api.Constants;
import com.oyeoye.merchant.business.api.entity.Deal;
import com.oyeoye.merchant.business.api.entity.Transaction;
import com.oyeoye.merchant.presentation.util.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BoughtDealsAdapter extends RecyclerView.Adapter<BoughtDealsAdapter.ViewHolder> {

    private List<Transaction> mTransactions;

    public BoughtDealsAdapter() {
        mTransactions = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bought_deal, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = mTransactions.get(position);
        holder.setup(transaction);
    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

    public void setList(List<Transaction> deals) {
        mTransactions = deals;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        @Bind(R.id.list_item_bought_deal_image)
        public AspectRatioImageView mImage;
        @Bind(R.id.list_item_bought_deal_title)
        public TextView mDealTitle;
        @Bind(R.id.list_item_bought_deal_description)
        public TextView mDealDescription;
        @Bind(R.id.list_item_bought_deal_price)
        public TextView mDealPrice;
        @Bind(R.id.list_item_bought_deal_status)
        public TextView mStatus;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void setup(final Transaction transaction) {
            final Deal deal = transaction.getDeal();
            Picasso.with(mContext).load(Constants.API_HOSTNAME + "/" + deal.getImage()).into(mImage);
            mDealTitle.setText(deal.getTitle());
            mDealDescription.setText(deal.getDescription());
            mDealPrice.setText("1 bought for $" + new DecimalFormat("#.00").format(deal.getPrice()));
            if (transaction.getStatus() == 0) {
                mStatus.setVisibility(View.VISIBLE);
                mStatus.setText("This offer has to be picked up");
            } else {
                mStatus.setVisibility(View.GONE);
            }
        }
    }
}
