package com.oyeoye.merchant.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.RootActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoExpose;
import mortar.Presenter;
import mortar.bundler.BundleService;

@AutoExpose(RootActivity.class)
@DaggerScope(RootActivity.class)
public class RootActivityPresenter extends Presenter<RootActivityPresenter.Activity> {

    private SetupToolbarHandler mSetupToolbarHandler;
    private List<ActivityResultListener> mActivityResultListeners;

    @Inject
    public RootActivityPresenter() {
        mActivityResultListeners = new ArrayList<>();
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

    public void startActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode, null);
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        ((RootActivity) getView()).startActivityForResult(intent, requestCode, options);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mActivityResultListeners.isEmpty()) {
            for (int i = 0; i < mActivityResultListeners.size(); i++) {
                mActivityResultListeners.get(i).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void addActivityResultListener(ActivityResultListener listener) {
        mActivityResultListeners.add(listener);
    }

    public void removeActivityResultListener(ActivityResultListener listener) {
        mActivityResultListeners.remove(listener);
    }

    public interface Activity {
        Context getContext();
    }

    public interface ActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
