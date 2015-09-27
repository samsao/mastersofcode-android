package com.oyeoye.merchant.presentation.deals.add_deal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.R;
import com.oyeoye.merchant.RootActivity;
import com.oyeoye.merchant.business.DealManager;
import com.oyeoye.merchant.business.api.entity.Deal;
import com.oyeoye.merchant.business.camera.PhotoUtil;
import com.oyeoye.merchant.presentation.AbstractPresenter;
import com.oyeoye.merchant.presentation.RootActivityPresenter;
import com.oyeoye.merchant.presentation.SetupToolbarHandler;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import timber.log.Timber;

@AutoStackable(
        component = @AutoComponent(dependencies = RootActivity.class),
        pathWithView = AddDealView.class
)
@DaggerScope(AddDealPresenter.class)
public class AddDealPresenter extends AbstractPresenter<AddDealView> implements SetupToolbarHandler {

    private final PhotoUtil mPhotoUtil;
    private final RootActivityPresenter mRootActivityPresenter;
    private final DealManager mDealManager;

    public AddDealPresenter(RootActivityPresenter mainActivityPresenter, PhotoUtil photoUtil, DealManager dealManager) {
        mRootActivityPresenter = mainActivityPresenter;
        mPhotoUtil = photoUtil;
        mDealManager = dealManager;
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
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
            default:
                return false;
        }
    }

    public Activity getActivity() {
        return mRootActivityPresenter.getActivity();
    }

    public void addDeal(Deal deal) {
        getView().showLoadingView(true);
        mDealManager.addDeal(deal, mPhotoUtil.getImagePath(), new ApiCallback<Deal>() {
            @Override
            public void onSuccess(Deal deal) {
                getView().showLoadingView(false);
                goBack();
            }

            @Override
            public void onFailure(Throwable error) {
                getView().showLoadingView(false);
                showToast("Error adding deal");
                goBack();
            }
        });
    }

    public void saveImage(Bitmap bitmap) {
        mPhotoUtil.saveImage(bitmap, new PhotoUtil.SaveImageCallback() {
            @Override
            public void onSuccess(String destFilePath) {
                Timber.i("image saved");
            }

            @Override
            public void onFailure() {
                Timber.e("image saving failure");
            }
        });
    }
}
