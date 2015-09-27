package com.oyeoye.merchant.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.RootActivity;
import com.oyeoye.merchant.business.PreferenceManager;
import com.oyeoye.merchant.service.RegistrationIntentService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoExpose;
import mortar.Presenter;
import mortar.bundler.BundleService;

@AutoExpose(RootActivity.class)
@DaggerScope(RootActivity.class)
public class RootActivityPresenter extends Presenter<RootActivityPresenter.Activity> {

    private PreferenceManager mPreferenceManager;
    private SetupToolbarHandler mSetupToolbarHandler;
    private List<ActivityResultListener> mActivityResultListeners;

    @Inject
    public RootActivityPresenter(PreferenceManager preferenceManager) {
        mActivityResultListeners = new ArrayList<>();
        mPreferenceManager = preferenceManager;
    }

    @Override
    protected BundleService extractBundleService(RootActivityPresenter.Activity activity) {
        return BundleService.getBundleService(activity.getContext());
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        if (TextUtils.isEmpty(mPreferenceManager.getGcmToken())) {
            // Start IntentService to register this application with GCM.
            Intent intentGoogleService = new Intent(getView().getContext(), RegistrationIntentService.class);
            ((RootActivity) getView()).startService(intentGoogleService);
        }
    }

    public RootActivity getActivity() {
        return (RootActivity) getView();
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

    /**
     * Set the status bar color
     *
     * @param color
     */
    public void setStatusBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((RootActivity) getView()).getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColorWithAlpha(((RootActivity) getView()).getResources().getColor(color), 0.75f));
        }
    }

    /**
     * Alpha is a value between 0 and 1
     *
     * @param alpha
     * @return
     */
    public int getColorWithAlpha(int color, float alpha) {
        return ((int) (alpha * 255.0f) << 24) | (color & 0x00ffffff);
    }

    public interface Activity {
        Context getContext();
    }

    public interface ActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
