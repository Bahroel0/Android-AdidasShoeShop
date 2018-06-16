package com.example.bahroel.adidasshoeshop.Realm;

import android.content.Context;

import com.example.bahroel.adidasshoeshop.Model.ProdukCart;
import com.example.bahroel.adidasshoeshop.Model.UserLogged;

import io.realm.RealmResults;

public class RealmProdukCartAdapter extends RealmModelAdapter<ProdukCart> {
    public RealmProdukCartAdapter(Context context, RealmResults<ProdukCart> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
