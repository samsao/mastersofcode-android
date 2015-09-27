package com.oyeoye.merchant.business.api;

import com.oyeoye.merchant.business.api.entity.User;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface Api {
    @POST("/merchant/connect/reg")
    @FormUrlEncoded
    void connect(@Field("phone") String phone, @Field("gcmId") String gcmId, Callback<User> callback);
}
