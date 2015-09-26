package com.oyeoye.merchant.presentation.deals.my_deals;

import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.presentation.AbstractPresenter;
import com.oyeoye.merchant.presentation.main.MainPresenter;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;

@AutoStackable(
        component = @AutoComponent(dependencies = MainPresenter.class),
        pathWithView = MyDealsView.class
)
@DaggerScope(MyDealsPresenter.class)
public class MyDealsPresenter extends AbstractPresenter<MyDealsView>  {


}
