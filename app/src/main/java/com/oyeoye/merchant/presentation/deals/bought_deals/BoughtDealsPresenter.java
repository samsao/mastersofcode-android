package com.oyeoye.merchant.presentation.deals.bought_deals;

import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.business.api.entity.Transaction;
import com.oyeoye.merchant.presentation.AbstractPresenter;
import com.oyeoye.merchant.presentation.main.MainPresenter;

import java.util.List;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;

@AutoStackable(
        component = @AutoComponent(dependencies = MainPresenter.class),
        pathWithView = BoughtDealsView.class
)
@DaggerScope(BoughtDealsPresenter.class)
public class BoughtDealsPresenter extends AbstractPresenter<BoughtDealsView> {

    private final MainPresenter mMainPresenter;

    public BoughtDealsPresenter(MainPresenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    public void fetchDeals() {
        mMainPresenter.getDealManager().getTransactions(new ApiCallback<List<Transaction>>() {
            @Override
            public void onSuccess(List<Transaction> deals) {
                getView().setDeals(deals);
            }

            @Override
            public void onFailure(Throwable error) {
                int i = 0;
            }
        });
    }

}
