package com.example.bahroel.adidasshoeshop.Response;

import com.example.bahroel.adidasshoeshop.Model.Produk;
import com.google.gson.annotations.SerializedName;

public class ProdukDetailResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("produk")
    private Produk produks;

    public boolean isSuccess() {
        return success;
    }

    public Produk getProduks() {
        return produks;
    }
}
