package com.example.bahroel.adidasshoeshop.Response;

import com.example.bahroel.adidasshoeshop.Model.User;
import com.google.gson.annotations.SerializedName;

public class UserDetailResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("user")
    private User user;

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }
}
