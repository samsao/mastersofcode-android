package com.oyeoye.merchant.presentation.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.RootActivity;
import com.oyeoye.merchant.presentation.AbstractPresenter;
import com.oyeoye.merchant.presentation.RootActivityPresenter;
import com.oyeoye.merchant.presentation.SetupToolbarHandler;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import timber.log.Timber;

@AutoStackable(
        component = @AutoComponent(dependencies = RootActivity.class),
        pathWithView = RegistrationView.class
)
@DaggerScope(RegistrationPresenter.class)
public class RegistrationPresenter extends AbstractPresenter<RegistrationView> implements SetupToolbarHandler,
    RootActivityPresenter.ActivityResultListener {

    private static final int PLACE_PICKER_REQUEST = 352;
    private final RootActivityPresenter mRootActivityPresenter;

    public RegistrationPresenter(RootActivityPresenter mainActivityPresenter) {
        mRootActivityPresenter = mainActivityPresenter;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        mRootActivityPresenter.addActivityResultListener(this);
    }

    @Override
    public void dropView(RegistrationView view) {
        super.dropView(view);
        mRootActivityPresenter.removeActivityResultListener(this);
    }

    public void resetMenu(Toolbar toolbar) {
        mRootActivityPresenter.setupToolbar(toolbar);
        mRootActivityPresenter.resetMenu(this);
    }

    @Override
    public void setupToolbarMenu(ActionBar actionBar, MenuInflater menuInflater, Menu menu) {
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Register");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getContext());
                String toastMsg = String.format("Place: %s", place.getName());
                showToast(toastMsg);
            }
        }
    }

    public void pickPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            mRootActivityPresenter.startActivityForResult(builder.build(getContext()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Timber.e("GooglePlayServicesRepairableException");
        } catch (GooglePlayServicesNotAvailableException e) {
            Timber.e("GooglePlayServicesNotAvailableException");
        }
    }
}
