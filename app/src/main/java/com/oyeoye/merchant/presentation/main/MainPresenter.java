package com.oyeoye.merchant.presentation.main;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.oyeoye.merchant.AppDependencies;
import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.R;
import com.oyeoye.merchant.RootActivity;
import com.oyeoye.merchant.business.DealManager;
import com.oyeoye.merchant.presentation.AbstractPresenter;
import com.oyeoye.merchant.presentation.RootActivityPresenter;
import com.oyeoye.merchant.presentation.SetupToolbarHandler;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import autodagger.AutoExpose;

@AutoStackable(
        component = @AutoComponent(dependencies = RootActivity.class, superinterfaces = AppDependencies.class),
        pathWithView = MainView.class
)
@DaggerScope(MainPresenter.class)
@AutoExpose(MainPresenter.class)
public class MainPresenter extends AbstractPresenter<MainView> implements SetupToolbarHandler {

    private final RootActivityPresenter mRootActivityPresenter;
    private final DealManager mDealManager;

    public MainPresenter(RootActivityPresenter rootActivityPresenter, DealManager dealManager) {
        mRootActivityPresenter = rootActivityPresenter;
        mDealManager = dealManager;
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
