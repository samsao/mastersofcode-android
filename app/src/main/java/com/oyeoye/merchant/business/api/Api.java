package com.oyeoye.merchant.business.api;

import com.oyeoye.merchant.business.api.entity.Deal;
import com.oyeoye.merchant.business.api.entity.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

public interface Api {
    @POST("/merchant/connect/reg")
    @FormUrlEncoded
    void connect(@Field("phone") String phone, @Field("gcmId") String gcmId, Callback<User> callback);

    @PUT("/merchant/connect")
    @FormUrlEncoded
    void updatePlace(@Field("placeId") String placeId, @Field("placeName") String placeName, Callback<User> callback);

    @PUT("/merchant/connect")
    @FormUrlEncoded
    void updateGcmId(@Field("gcmId") String gcmId, Callback<User> callback);

    @POST("/merchant/deal/add")
    @Multipart
    void addDeal(@Part("title") String title,
                 @Part("description") String description,
                 @Part("originalPrice") Float originalPrice,
                 @Part("price") Float price,
                 @Part("quantity") Integer quantity,
                 @Part("imageFile") TypedFile image,
                 Callback<Deal> callback);

    @GET("/merchant/deal")
    void getDeals(Callback<List<Deal>> callback);
}
