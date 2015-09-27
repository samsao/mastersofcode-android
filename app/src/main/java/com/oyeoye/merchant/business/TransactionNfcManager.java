package com.oyeoye.merchant.business;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.MainApplication;
import com.oyeoye.merchant.business.api.Api;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@DaggerScope(MainApplication.class)
public class TransactionNfcManager {

    private final Api api;

    @Inject
    public TransactionNfcManager(RestAdapter restAdapter) {
        api = restAdapter.create(Api.class);
    }

    public void receive(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        NdefRecord record = msg.getRecords()[0];
        String string = new String(record.getPayload());
        Timber.d("NFC RES: %s", string);

        String[] array = StringUtils.split(string, ':');
        api.validateTransaction(array[0], array[1], new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                Timber.d("Success validate transaction");
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.d("Error validate transaction: %s", error.getMessage());
            }
        });
    }
}
