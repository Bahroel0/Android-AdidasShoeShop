package com.example.bahroel.adidasshoeshop.Realm;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import com.example.bahroel.adidasshoeshop.Model.ProdukCart;
import com.example.bahroel.adidasshoeshop.Model.User;
import com.example.bahroel.adidasshoeshop.Model.UserLogged;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    public void logout() {
        realm.beginTransaction();
        realm.clear(UserLogged.class);
        realm.commitTransaction();
    }

    public RealmResults<UserLogged> getUserLogged(){
        return realm.where(UserLogged.class).findAll();
    }




    // produk
    public RealmResults<ProdukCart> getAllProdukCart(){
        return realm.where(ProdukCart.class).findAll();
    }

    public void clearCart() {
        realm.beginTransaction();
        realm.clear(ProdukCart.class);
        realm.commitTransaction();
    }

}
