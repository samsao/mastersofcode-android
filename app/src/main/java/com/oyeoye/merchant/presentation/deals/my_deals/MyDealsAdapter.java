package com.oyeoye.merchant.presentation.deals.my_deals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.business.api.Constants;
import com.oyeoye.merchant.business.api.entity.Deal;
import com.oyeoye.merchant.presentation.util.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
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
        return new ViewHolder(parent.getContext(), itemView, mOnClickListener);
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
        private Context mContext;
        private OnClickListener mOnClickListener;
        private View mItemView;

        @Bind(R.id.list_item_deal_image)
        public AspectRatioImageView mImage;
        @Bind(R.id.list_item_deal_discount_percentage)
        public TextView mDiscountPercentage;
        @Bind(R.id.list_item_deal_title)
        public TextView mDealTitle;
        @Bind(R.id.list_item_deal_description)
        public TextView mDealDescription;
        @Bind(R.id.list_item_deal_price)
        public TextView mDealPrice;
        @Bind(R.id.list_item_deal_quantity_icon)
        public ImageView mDealQuantityIcon;
        @Bind(R.id.list_item_deal_quantity)
        public TextView mDealQuantity;


        public ViewHolder(Context context, View itemView, OnClickListener onClickListener) {
            super(itemView);
            mContext = context;
            mItemView = itemView;
            this.mOnClickListener = onClickListener;
            ButterKnife.bind(this, itemView);
        }

        public void setup(final Deal deal) {
            Picasso.with(mContext).load(Constants.API_HOSTNAME + "/" + deal.getImage()).into(mImage);
            mDiscountPercentage.setText(Integer.toString((int) (100.0 - (deal.getPrice() / deal.getOriginalPrice()) * 100.0)) + "% off");
            mDealTitle.setText(deal.getTitle());
            mDealDescription.setText(deal.getDescription());
            mDealPrice.setText(new DecimalFormat("#.00").format(deal.getPrice()));
            mDealQuantityIcon.setImageDrawable(mContext.getResources().getDrawable(getQuantityIconId(deal.getQuantity())));
            mDealQuantity.setText(deal.getQuantity().toString());

            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClickDeak(deal);
                    }
                }
            });
        }

        private int getQuantityIconId(int quantity) {
            switch (quantity) {
                case 1:
                    return R.drawable.ic_filter_1_black_24dp;
                case 2:
                    return R.drawable.ic_filter_2_black_24dp;
                case 3:
                    return R.drawable.ic_filter_3_black_24dp;
                case 4:
                    return R.drawable.ic_filter_4_black_24dp;
                case 5:
                    return R.drawable.ic_filter_5_black_24dp;
                case 6:
                    return R.drawable.ic_filter_6_black_24dp;
                case 7:
                    return R.drawable.ic_filter_7_black_24dp;
                case 8:
                    return R.drawable.ic_filter_8_black_24dp;
                case 9:
                    return R.drawable.ic_filter_9_black_24dp;
                default:
                    return R.drawable.ic_filter_9_plus_black_24dp;
            }
        }
    }

    public interface OnClickListener {
        void onClickDeak(Deal deal);
    }
}
