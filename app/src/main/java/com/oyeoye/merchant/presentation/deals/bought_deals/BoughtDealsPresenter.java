package com.oyeoye.merchant.presentation.deals.bought_deals;

import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.presentation.AbstractPresenter;
import com.oyeoye.merchant.presentation.main.MainPresenter;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;

@AutoStackable(
        component = @AutoComponent(dependencies = MainPresenter.class),
        pathWithView = BoughtDealsView.class
)
@DaggerScope(BoughtDealsPresenter.class)
public class BoughtDealsPresenter extends AbstractPresenter<BoughtDealsView> {



}
