package com.oyeoye.merchant.business;

import android.text.TextUtils;

import com.google.android.gms.location.places.Place;
import com.oyeoye.merchant.business.api.Api;
import com.oyeoye.merchant.business.api.entity.User;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author jfcartier
 * @since 15-09-27
 */
public class UserManager {
    private PreferenceManager mPreferenceManager;
    private Api mApiService;

    public UserManager(PreferenceManager preferenceManager, RestAdapter restAdapter) {
        mPreferenceManager = preferenceManager;
        mApiService = restAdapter.create(Api.class);
    }

    public String getUserGcmToken() {
        return mPreferenceManager.getGcmToken();
    }

    public void setUserGcmToken(String token) {
        mPreferenceManager.setGcmTokenSentToServer(TextUtils.isEmpty(token));
        mPreferenceManager.setGcmToken(token);
        updateGcmId();
    }

    public String getApiToken() {
        return mPreferenceManager.getApiToken();
    }

    public void login(String phone, final Callback<User> callback) {
        mApiService.connect(phone, getUserGcmToken(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                if (user != null) {
                    mPreferenceManager.setApiToken(user.getToken());
                }
                callback.success(user, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    public void updatePlace(Place place, final Callback<User> callback) {
        mApiService.updatePlace(place.getId(), place.getName().toString(), callback);
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(getApiToken());
    }

    public void updateGcmId() {
        mApiService.updateGcmId(getUserGcmToken(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
