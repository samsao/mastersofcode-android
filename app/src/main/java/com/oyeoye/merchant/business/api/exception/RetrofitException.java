package com.oyeoye.merchant.business.api.exception;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;

import retrofit.RetrofitError;

public abstract class RetrofitException extends Throwable {
    private String mMessage;

    @Override
    public String getMessage() {
        return mMessage;
    }

    public void setMessage(RetrofitError retrofitError) {
        String message = getErrorMessage(retrofitError);
        if (!TextUtils.isEmpty(message)) {
            mMessage = message;
        }
    }

    /**
     * Get a request error message
     *
     * @param error
     * @return
     */
    private String getErrorMessage(RetrofitError error) {
        return null;
    }

    /**
     * Convert an input stream to a string
     *
     * @param is
     * @return
     */
    private String convertResponseStreamToString(InputStream is) {
        int k;
        StringBuffer sb = new StringBuffer();
        try {
            while ((k = is.read()) != -1) {
                sb.append((char) k);
            }
        } catch (IOException e) {
            return null;
        }
        return sb.toString();
    }
}
