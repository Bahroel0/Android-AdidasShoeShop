package com.example.bahroel.adidasshoeshop.Api;

import com.example.bahroel.adidasshoeshop.Response.PaymentResponse;
import com.example.bahroel.adidasshoeshop.Response.ProdukDetailResponse;
import com.example.bahroel.adidasshoeshop.Response.ProdukResponse;
import com.example.bahroel.adidasshoeshop.Response.RiwayatResponse;
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
    Call<UserResponse> register(@Field("name") String name,@Field("email") String email, @Field("password") String password);

    @POST("api/login")
    @FormUrlEncoded
    Call<UserResponse> login(@Field("email") String email, @Field("password") String password);

    @POST("api/update")
    @FormUrlEncoded
    Call<UserResponse> update(@Field("id") int id, @Field("password") String password, @Field("api_token") String api_token);


    // produk request
    @GET("api/getAllProduk")
    Call<ProdukResponse> getProdukJSON(@Query("page") int page);
    @GET("api/getKategoriProduk")
    Call<ProdukResponse> getKategoriProdukJSON(@Query("kategori") String kategori, @Query("page") int page);
    @GET("api/find")
    Call<ProdukResponse> getSearchJSON(@Query("searchkey") String searchkey, @Query("page") int page);
    @GET("api/getDetailProduk")
    Call<ProdukDetailResponse> getDetailProdukJSON(@Query("id_produk") int id);

    // payment

    @POST("api/payment")
    @FormUrlEncoded
    // this is for update stock  of product that had been buy
    Call<PaymentResponse> payment(@Field("id_produk") int id, @Field("jumlah") int jumlah, @Field("api_token") String api_token);

    @POST("api/riwayat/add")
    @FormUrlEncoded
    Call<PaymentResponse> addRiwayat(@Field("id_user") int id_user, @Field("total") int total, @Field("api_token") String api_token);

    @GET("api/riwayat")
    Call<RiwayatResponse> getRiwayat(@Query("id_user") int id_user, @Query("api_token") String api_token);

}
