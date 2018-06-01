package com.example.bahroel.adidasshoeshop.Model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User {
    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("email")
    private String email;
    @SerializedName("api_token")
    private String api_token;
    @SerializedName("remember_token")
    private String remember_token;

    public User(int id, String email, String api_token, String remember_token) {
        this.id = id;
        this.email = email;
        this.api_token = api_token;
        this.remember_token = remember_token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }
}
