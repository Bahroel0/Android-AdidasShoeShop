package com.example.bahroel.adidasshoeshop.Api;

import com.example.bahroel.adidasshoeshop.Response.ProdukResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("api/getAllProduk")
    Call<ProdukResponse> getProdukJSON();
}
