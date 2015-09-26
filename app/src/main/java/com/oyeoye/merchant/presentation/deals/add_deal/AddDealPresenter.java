package com.oyeoye.merchant.presentation.deals.add_deal;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.R;
import com.oyeoye.merchant.RootActivity;
import com.oyeoye.merchant.presentation.AbstractPresenter;
import com.oyeoye.merchant.presentation.RootActivityPresenter;
import com.oyeoye.merchant.presentation.SetupToolbarHandler;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import autodagger.AutoExpose;

@AutoStackable(
        component = @AutoComponent(dependencies = RootActivity.class),
        pathWithView = AddDealView.class
)
@DaggerScope(AddDealPresenter.class)
@AutoExpose(AddDealPresenter.class)
public class AddDealPresenter extends AbstractPresenter<AddDealView> implements SetupToolbarHandler {

    private final RootActivityPresenter mRootActivityPresenter;

    public AddDealPresenter(RootActivityPresenter mainActivityPresenter) {
        mRootActivityPresenter = mainActivityPresenter;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        getView().startCameraPreview(mRootActivityPresenter.getActivity());
    }

    public void resetMenu(Toolbar toolbar) {
        mRootActivityPresenter.setupToolbar(toolbar);
        mRootActivityPresenter.resetMenu(this);
    }

    @Override
    public void setupToolbarMenu(ActionBar actionBar, MenuInflater menuInflater, Menu menu) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.add_deal));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

}
