package com.example.bahroel.adidasshoeshop.Response;

import com.example.bahroel.adidasshoeshop.Model.Produk;
import com.google.gson.annotations.SerializedName;

public class ProdukResponse {
    @SerializedName("success")
    private String success;
    @SerializedName("produk")
    private Produk[] produks;

    public String getSuccess() {
        return success;
    }

    public Produk[] getProduks() {
        return produks;
    }
}
