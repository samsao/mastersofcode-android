package com.samsao.projecttemplate.presentation;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.samsao.projecttemplate.DaggerScope;
import com.samsao.projecttemplate.RootActivity;

import javax.inject.Inject;

import autodagger.AutoExpose;
import mortar.Presenter;
import mortar.bundler.BundleService;

/**
 * @author jliang
 * @since 2015-08-07
 */
@AutoExpose(RootActivity.class)
@DaggerScope(RootActivity.class)
public class RootActivityPresenter extends Presenter<RootActivityPresenter.Activity> {

    private SetupToolbarHandler mSetupToolbarHandler;

    @Inject
    public RootActivityPresenter() {

    }

    @Override
    protected BundleService extractBundleService(RootActivityPresenter.Activity activity) {
        return BundleService.getBundleService(activity.getContext());
    }


    public void setupToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            ((RootActivity) getView()).setSupportActionBar(toolbar);
        }
    }

    /**
     * Set the toolbar title
     *
     * @param title
     */
    public void setToolbarTitle(String title) {
        if (title.isEmpty()) {
            ((RootActivity) getView()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            ((RootActivity) getView()).getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((RootActivity) getView()).getSupportActionBar().setTitle(title.toUpperCase());
        }
    }

    public void resetMenu(SetupToolbarHandler setupToolbarPresenter) {
        mSetupToolbarHandler = setupToolbarPresenter;
        ((RootActivity) getView()).invalidateOptionsMenu();
    }

    public void onCreateOptionsMenu(MenuInflater menuInflater, Menu menu) {
        ActionBar actionBar = ((RootActivity) getView()).getSupportActionBar();
        if (actionBar != null && mSetupToolbarHandler != null) {
            mSetupToolbarHandler.setupToolbarMenu(actionBar, menuInflater, menu);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                if (mSetupToolbarHandler != null) {
                    return mSetupToolbarHandler.onOptionsItemSelected(item);
                } else {
                    return false;
                }
        }
    }

    public interface Activity {
        Context getContext();
    }
}
