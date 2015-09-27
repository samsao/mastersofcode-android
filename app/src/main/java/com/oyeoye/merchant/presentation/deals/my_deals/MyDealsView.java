package com.oyeoye.merchant.presentation.deals.my_deals;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.business.api.entity.Deal;
import com.oyeoye.merchant.presentation.base.PresentedFrameLayout;
import com.oyeoye.merchant.presentation.deals.my_deals.stackable.MyDealsStackableComponent;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@AutoInjector(MyDealsPresenter.class)
public class MyDealsView extends PresentedFrameLayout<MyDealsPresenter> implements MyDealsAdapter.OnClickListener {

    @Bind(R.id.screen_my_deals_fab)
    public FloatingActionButton mFloatingActionButton;
    @Bind(R.id.screen_my_deals_recyclerview)
    protected RecyclerView mRecyclerView;

    private MyDealsAdapter mAdapter;

    public MyDealsView(Context context) {
        super(context);
        DaggerService.<MyDealsStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_my_deals, this);
        ButterKnife.bind(view);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mAdapter = new MyDealsAdapter(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.screen_my_deals_fab)
    public void onFloatingActionButtonClick() {
        presenter.addDeal();
    }

    @Override
    public void onClickDeak(Deal deal) {
        presenter.editDeal(deal);
    }
}
