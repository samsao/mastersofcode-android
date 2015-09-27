package com.oyeoye.merchant.presentation.deals.bought_deals;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.presentation.base.PresentedFrameLayout;
import com.oyeoye.merchant.presentation.deals.bought_deals.stackable.BoughtDealsStackableComponent;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

@AutoInjector(BoughtDealsPresenter.class)
public class BoughtDealsView extends PresentedFrameLayout<BoughtDealsPresenter> {

    @Bind(R.id.screen_bought_deals_recyclerview)
    public RecyclerView mRecyclerView;

    private BoughtDealsAdapter mAdapter;

    public BoughtDealsView(Context context) {
        super(context);
        DaggerService.<BoughtDealsStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_bought_deals, this);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(view);

        mAdapter = new BoughtDealsAdapter();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
