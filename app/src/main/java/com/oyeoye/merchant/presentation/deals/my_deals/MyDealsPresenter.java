package com.oyeoye.merchant.presentation.deals.my_deals;

import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.business.api.entity.Deal;
import com.oyeoye.merchant.presentation.AbstractPresenter;
import com.oyeoye.merchant.presentation.deals.add_deal.stackable.AddDealStackable;
import com.oyeoye.merchant.presentation.main.MainPresenter;

import java.util.List;

import architect.Navigator;
import architect.robot.AutoStackable;
import autodagger.AutoComponent;

@AutoStackable(
        component = @AutoComponent(dependencies = MainPresenter.class),
        pathWithView = MyDealsView.class
)
@DaggerScope(MyDealsPresenter.class)
public class MyDealsPresenter extends AbstractPresenter<MyDealsView>  {

    private final MainPresenter mMainPresenter;

    public MyDealsPresenter(MainPresenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    public void fetchDeals() {
        mMainPresenter.getDealManager().getDeals(new ApiCallback<List<Deal>>() {
            @Override
            public void onSuccess(List<Deal> deals) {
                getView().setDeals(deals);
            }

            @Override
            public void onFailure(Throwable error) {
            }
        });
    }

    public void addDeal() {
        Navigator.get(getView()).push(new AddDealStackable());
    }

    public void editDeal(Deal deal) {
        // TODO
    }
}
