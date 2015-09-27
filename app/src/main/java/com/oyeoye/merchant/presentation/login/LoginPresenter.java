package com.oyeoye.merchant.presentation.login;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.RootActivity;
import com.oyeoye.merchant.business.UserManager;
import com.oyeoye.merchant.business.api.entity.User;
import com.oyeoye.merchant.presentation.AbstractPresenter;
import com.oyeoye.merchant.presentation.RootActivityPresenter;
import com.oyeoye.merchant.presentation.SetupToolbarHandler;
import com.oyeoye.merchant.presentation.main.stackable.MainStackable;

import architect.Navigator;
import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import autodagger.AutoExpose;

@AutoStackable(
        component = @AutoComponent(dependencies = RootActivity.class),
        pathWithView = LoginView.class
)
@DaggerScope(LoginPresenter.class)
@AutoExpose(LoginPresenter.class)
public class LoginPresenter extends AbstractPresenter<LoginView> implements SetupToolbarHandler {

    private final RootActivityPresenter activityPresenter;
    private final UserManager mUserManager;

    private AuthCallback authCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession digitsSession, String phone) {
            if (!hasView()) return;
            loginWithPhone(phone);
        }

        @Override
        public void failure(DigitsException e) {
            if (!hasView()) return;
        }
    };

    public LoginPresenter(RootActivityPresenter activityPresenter, UserManager userManager) {
        this.activityPresenter = activityPresenter;
        this.mUserManager = userManager;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        getView().configure(authCallback);
    }

    private void loginWithPhone(final String phone) {
        getView().showLoading();

        mUserManager.login(phone, new ApiCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (!hasView() || user == null) return;
                getView().hideLoading();

                Navigator.get(getView()).push(new MainStackable());
            }

            @Override
            public void onFailure(Throwable error) {
                if (!hasView()) return;
                getView().hideLoading();
            }
        });
    }

    @Override
    public void setupToolbarMenu(ActionBar actionBar, MenuInflater menuInflater, Menu menu) {
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Oye Oye!");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onViewAttachedToWindow() {
        activityPresenter.setupToolbar(getView().toolbar);
        activityPresenter.resetMenu(this);
    }
}
