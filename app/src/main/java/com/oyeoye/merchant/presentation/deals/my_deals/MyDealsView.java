package com.oyeoye.merchant.presentation.deals.my_deals;

import android.content.Context;
import android.graphics.Rect;
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

import java.util.List;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@AutoInjector(MyDealsPresenter.class)
public class MyDealsView extends PresentedFrameLayout<MyDealsPresenter> implements MyDealsAdapter.OnClickListener {

    public static final String TAG = "MyDealsView";

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
        setTag(TAG);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = (int) getResources().getDimension(R.dimen.spacing);
                } else {
                    super.getItemOffsets(outRect, view, parent, state);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        presenter.fetchDeals();
    }

    public MyDealsPresenter getPresenter() {
        return presenter;
    }

    @OnClick(R.id.screen_my_deals_fab)
    public void onFloatingActionButtonClick() {
        presenter.addDeal();
    }

    @Override
    public void onClickDeak(Deal deal) {
        presenter.editDeal(deal);
    }

    public void setDeals(List<Deal> deals) {
        mAdapter.setList(deals);
    }
}
