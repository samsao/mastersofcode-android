package com.oyeoye.merchant.business.api;

import android.content.Context;
import android.net.ConnectivityManager;

import com.oyeoye.merchant.business.api.exception.NetworkNotConnectedException;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;

import retrofit.client.Request;
import retrofit.client.Response;

public class CustomOkClient extends retrofit.client.OkClient {

    private Context mContext;

    public CustomOkClient(OkHttpClient client, Context context) {
        super(client);
        mContext = context;
    }

    @Override
    public Response execute(Request request) throws IOException {
        android.net.NetworkInfo network = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (network == null || !network.isConnected()) {
            throw new NetworkNotConnectedException();
        }
        return super.execute(request);
    }
}
