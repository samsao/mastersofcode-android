package com.oyeoye.merchant.presentation.profile;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.RootActivity;
import com.oyeoye.merchant.presentation.AbstractPresenter;
import com.oyeoye.merchant.presentation.RootActivityPresenter;
import com.oyeoye.merchant.presentation.SetupToolbarHandler;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;

@AutoStackable(
        component = @AutoComponent(dependencies = RootActivity.class),
        pathWithView = ProfileView.class
)
@DaggerScope(ProfilePresenter.class)
public class ProfilePresenter extends AbstractPresenter<ProfileView> implements SetupToolbarHandler {

    private final RootActivityPresenter mRootActivityPresenter;

    public ProfilePresenter(RootActivityPresenter mainActivityPresenter) {
        mRootActivityPresenter = mainActivityPresenter;
    }

    public void resetMenu(Toolbar toolbar) {
        mRootActivityPresenter.setupToolbar(getView().mToolbar);
        mRootActivityPresenter.resetMenu(this);
    }

    @Override
    public void setupToolbarMenu(ActionBar actionBar, MenuInflater menuInflater, Menu menu) {
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("TEMPLATE PROJECT");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

}
