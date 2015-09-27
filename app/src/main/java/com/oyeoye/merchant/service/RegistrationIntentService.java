package com.oyeoye.merchant.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.MainApplication;
import com.oyeoye.merchant.R;
import com.oyeoye.merchant.business.UserManager;

import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import timber.log.Timber;

/**
 * @author jfcartier
 * @since 15-09-26
 */
@AutoComponent(
        dependencies = MainApplication.class
)
@AutoInjector
@DaggerScope(RegistrationIntentService.class)
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    @Inject
    protected UserManager mUserManager;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RegistrationIntentServiceComponent component = DaggerRegistrationIntentServiceComponent.builder().mainApplicationComponent(((MainApplication)getApplication()).getComponent()).build();
        component.inject(this);
        try {
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                mUserManager.setUserGcmToken(token);
            }
        } catch (Exception e) {
            Timber.e("Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            mUserManager.setUserGcmToken(null);
        }
    }
}
