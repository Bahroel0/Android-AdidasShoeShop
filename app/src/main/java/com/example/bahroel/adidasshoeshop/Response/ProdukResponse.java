package com.example.bahroel.adidasshoeshop.Response;

import com.example.bahroel.adidasshoeshop.Model.Produk;
import com.google.gson.annotations.SerializedName;

public class ProdukResponse {
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("data")
    private Produk[] produks;
    @SerializedName("last_page")
    private int last_page;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getLast_page() {
        return last_page;
    }

    public Produk[] getProduks() {
        return produks;
    }
}
