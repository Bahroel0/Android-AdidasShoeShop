package com.example.bahroel.adidasshoeshop.Model;

import com.google.gson.annotations.SerializedName;

public class Riwayat {
    @SerializedName("total")
    private int total;
    @SerializedName("created_at")
    private String tanggal;

    public int getTotal() {
        return total;
    }

    public String getTanggal() {
        return tanggal;
    }
}
