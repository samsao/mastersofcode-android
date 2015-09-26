package com.oyeoye.merchant.presentation.deals.bought_deals;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.presentation.base.PresentedFrameLayout;
import com.oyeoye.merchant.presentation.deals.bought_deals.stackable.BoughtDealsStackableComponent;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.ButterKnife;

@AutoInjector(BoughtDealsPresenter.class)
public class BoughtDealsView extends PresentedFrameLayout<BoughtDealsPresenter> {

    public BoughtDealsView(Context context) {
        super(context);
        DaggerService.<BoughtDealsStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_bought_deals, this);
        ButterKnife.bind(view);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
