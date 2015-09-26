package com.oyeoye.merchant.presentation.deals.my_deals;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.presentation.base.PresentedFrameLayout;
import com.oyeoye.merchant.presentation.deals.my_deals.stackable.MyDealsStackableComponent;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.ButterKnife;

@AutoInjector(MyDealsPresenter.class)
public class MyDealsView extends PresentedFrameLayout<MyDealsPresenter> {

    public MyDealsView(Context context) {
        super(context);
        DaggerService.<MyDealsStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_my_deals, this);
        ButterKnife.bind(view);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
