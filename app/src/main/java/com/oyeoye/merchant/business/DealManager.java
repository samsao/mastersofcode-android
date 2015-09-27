package com.oyeoye.merchant.business;

import com.oyeoye.merchant.business.api.Api;
import com.oyeoye.merchant.business.api.entity.Deal;
import com.oyeoye.merchant.business.api.entity.Transaction;

import java.io.File;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.mime.TypedFile;

/**
 * @author jfcartier
 * @since 15-09-27
 */
public class DealManager {
    private Api mApiService;

    public DealManager(RestAdapter restAdapter) {
        mApiService = restAdapter.create(Api.class);
    }

    public void addDeal(Deal deal, String imagePath, Callback<Deal> callback) {
        mApiService.addDeal(deal.getTitle(),
                deal.getDescription(),
                deal.getOriginalPrice(),
                deal.getPrice(),
                deal.getQuantity(),
                new TypedFile("application/octet-stream", new File(imagePath)),
                callback);
    }

    public void getDeals(Callback<List<Deal>> callback) {
        mApiService.getDeals(callback);
    }

    public void getTransactions(Callback<List<Transaction>> callback) {
        mApiService.getTransactions(callback);
    }
}
