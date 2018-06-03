package com.example.bahroel.adidasshoeshop.Model;

import io.realm.RealmObject;

public class UserLogged extends RealmObject{
    private int id;
    private String username;
    private String api_token;
    private String remember_token;

    public UserLogged() {
    }

    public UserLogged(int id, String username, String api_token, String remember_token) {
        this.id = id;
        this.username = username;
        this.api_token = api_token;
        this.remember_token = remember_token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
