package com.oyeoye.merchant.business;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.MainApplication;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@DaggerScope(MainApplication.class)
public class TransactionNfcManager {

    @Inject
    public TransactionNfcManager() {
    }

    public void receive(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        NdefRecord record = msg.getRecords()[1];
        String string = new String(record.getPayload());
        Timber.d("NFC RES: %s", string);
    }
}
