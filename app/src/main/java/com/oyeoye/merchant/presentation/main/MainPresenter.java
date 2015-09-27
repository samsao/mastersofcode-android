package com.oyeoye.merchant.presentation.main;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.oyeoye.merchant.AppDependencies;
import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.R;
import com.oyeoye.merchant.RootActivity;
import com.oyeoye.merchant.business.DealManager;
import com.oyeoye.merchant.business.UserManager;
import com.oyeoye.merchant.business.api.entity.User;
import com.oyeoye.merchant.presentation.AbstractPresenter;
import com.oyeoye.merchant.presentation.RootActivityPresenter;
import com.oyeoye.merchant.presentation.SetupToolbarHandler;
import com.oyeoye.merchant.presentation.deals.my_deals.MyDealsView;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import autodagger.AutoExpose;
import timber.log.Timber;

@AutoStackable(
        component = @AutoComponent(dependencies = RootActivity.class, superinterfaces = AppDependencies.class),
        pathWithView = MainView.class
)
@DaggerScope(MainPresenter.class)
@AutoExpose(MainPresenter.class)
public class MainPresenter extends AbstractPresenter<MainView> implements SetupToolbarHandler {

    private final RootActivityPresenter mRootActivityPresenter;
    private final DealManager mDealManager;
    private final UserManager mUserManager;

    public MainPresenter(RootActivityPresenter rootActivityPresenter, DealManager dealManager, UserManager userManager) {
        mRootActivityPresenter = rootActivityPresenter;
        mDealManager = dealManager;
        mUserManager = userManager;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        // FIXME because Twitter Digits does not work
        if (!mUserManager.isLoggedIn()) {
            getView().showLoadingView(true);
            mUserManager.login("5148888888", new ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    Timber.i("logging success");
                    getView().showLoadingView(false);
                    View view = getView().findViewWithTag(MyDealsView.TAG);
                    if (view != null) {
                        ((MyDealsView)view).getPresenter().fetchDeals();
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    Timber.e("logging failure: trouble");
                    getView().showLoadingView(false);
                }
            });
        }
    }

    public void resetMenu(Toolbar toolbar) {
        mRootActivityPresenter.setupToolbar(toolbar);
        mRootActivityPresenter.resetMenu(this);
    }

    @Override
    public void setupToolbarMenu(ActionBar actionBar, MenuInflater menuInflater, Menu menu) {
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.app_name));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public DealManager getDealManager() {
        return mDealManager;
    }
}
