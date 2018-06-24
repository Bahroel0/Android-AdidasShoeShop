package com.example.bahroel.adidasshoeshop.Response;

import com.example.bahroel.adidasshoeshop.Model.Riwayat;
import com.google.gson.annotations.SerializedName;

public class RiwayatResponse {
    @SerializedName("success")
    private boolean sucsess;
    @SerializedName("message")
    private String message;
    @SerializedName("riwayat")
    private Riwayat[] riwayats;

    public boolean isSucsess() {
        return sucsess;
    }

    public String getMessage() {
        return message;
    }

    public Riwayat[] getRiwayats() {
        return riwayats;
    }


}
