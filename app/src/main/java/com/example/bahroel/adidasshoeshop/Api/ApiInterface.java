package com.example.bahroel.adidasshoeshop.Api;

import com.example.bahroel.adidasshoeshop.Response.ProdukResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("api/getAllProduk")
    Call<ProdukResponse> getProdukJSON(@Query("page") int page);
}
