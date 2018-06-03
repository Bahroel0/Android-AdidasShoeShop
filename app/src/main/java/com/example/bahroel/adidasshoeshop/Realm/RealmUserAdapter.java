package com.example.bahroel.adidasshoeshop.Realm;

import android.content.Context;

import com.example.bahroel.adidasshoeshop.Model.UserLogged;

import io.realm.RealmResults;

public class RealmUserAdapter extends RealmModelAdapter<UserLogged> {
    public RealmUserAdapter(Context context, RealmResults<UserLogged> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
