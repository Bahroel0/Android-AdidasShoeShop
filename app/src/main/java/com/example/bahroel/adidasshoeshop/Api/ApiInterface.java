package com.example.bahroel.adidasshoeshop.Api;

import com.example.bahroel.adidasshoeshop.Response.ProdukResponse;
import com.example.bahroel.adidasshoeshop.Response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    // user request
    @POST("api/register")
    @FormUrlEncoded
    Call<UserResponse> register(@Field("email") String email, @Field("password") String password);




    @GET("api/getAllProduk")
    Call<ProdukResponse> getProdukJSON(@Query("page") int page);
}
