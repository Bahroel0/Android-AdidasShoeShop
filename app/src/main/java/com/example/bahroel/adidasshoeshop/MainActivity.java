package com.example.bahroel.adidasshoeshop;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bahroel.adidasshoeshop.App.Prefs;
import com.example.bahroel.adidasshoeshop.Behavior.BottomNavigationBehavior;
import com.example.bahroel.adidasshoeshop.Fragment.HomeFragment;
import com.example.bahroel.adidasshoeshop.Model.UserLogged;
import com.example.bahroel.adidasshoeshop.Realm.RealmController;
import com.example.bahroel.adidasshoeshop.User.DaftarActivity;
import com.example.bahroel.adidasshoeshop.User.GantiPasswordActivity;
import com.example.bahroel.adidasshoeshop.User.LoginActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private Realm realm;
    ImageView imgToogle;
    TextView tv_username;
    LinearLayout lnr_user, daftar, masuk, lnr_user_logged, btn_ubah_password, btn_riwayat, btn_keluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgToogle = findViewById(R.id.imgMenu);
        lnr_user = findViewById(R.id.lnr_user);
        lnr_user_logged = findViewById(R.id.lnr_user_logged);
        daftar = findViewById(R.id.lnr_daftar);
        masuk = findViewById(R.id.lnr_masuk);
        tv_username = findViewById(R.id.tv_username);
        btn_ubah_password = findViewById(R.id.btn_ubah_password);
        btn_riwayat = findViewById(R.id.btn_riwayat);
        btn_keluar = findViewById(R.id.btn_keluar);
        lnr_user.setVisibility(View.GONE);
        lnr_user_logged.setVisibility(View.GONE);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);

        try{
            realm = RealmController.with(MainActivity.this).getRealm();
            RealmResults<UserLogged> result = realm.where(UserLogged.class).findAll();
            realm.beginTransaction();
            tv_username.setText(result.get(0).getUsername());
            realm.commitTransaction();

            RealmController.with(MainActivity.this).refresh();
        }catch (NullPointerException e){
            tv_username.setText("");
        }

        this.realm = RealmController.with(this).getRealm();
        if (!Prefs.with(getApplicationContext()).getPreLoad()) {
            setRealmData();
        }

        imgToogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    realm = RealmController.getInstance().getRealm();
                    final RealmResults<UserLogged> userLoggeds = realm.where(UserLogged.class).findAll();
                    if(userLoggeds.get(0).getRemember_token()==null){
                        if(lnr_user.getVisibility() == View.GONE){
                            imgToogle.setImageResource(R.drawable.ic_keyboard_backspace);
                            lnr_user.setVisibility(View.VISIBLE);
                        }else{
                            imgToogle.setImageResource(R.drawable.ic_menu);
                            lnr_user.setVisibility(View.GONE);
                        }
                    }else{
                        if(lnr_user_logged.getVisibility() == View.GONE){
                            imgToogle.setImageResource(R.drawable.ic_keyboard_backspace);
                            lnr_user_logged.setVisibility(View.VISIBLE);
                        }else{
                            imgToogle.setImageResource(R.drawable.ic_menu);
                            lnr_user_logged.setVisibility(View.GONE);
                        }
                    }
                }catch (NullPointerException e){
                    if(lnr_user.getVisibility() == View.GONE){
                        imgToogle.setImageResource(R.drawable.ic_keyboard_backspace);
                        lnr_user.setVisibility(View.VISIBLE);
                    }else{
                        imgToogle.setImageResource(R.drawable.ic_menu);
                        lnr_user.setVisibility(View.GONE);
                    }
                }
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DaftarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btn_ubah_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GantiPasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btn_riwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealmResults<UserLogged> result = realm.where(UserLogged.class).findAll();
                realm.beginTransaction();
                result.get(0).setId(0);
                result.get(0).setApi_token(null);
                result.get(0).setRemember_token(null);
                realm.commitTransaction();

                RealmController.with(MainActivity.this).refresh();
                imgToogle.setImageResource(R.drawable.ic_menu);
                lnr_user_logged.setVisibility(View.GONE);
            }
        });




        HomeFragment home = new HomeFragment();
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.replace(R.id.fragment_main, home);
        FT.commit();

    }

    private void setRealmData() {
        UserLogged user = new UserLogged(0,null,null,null);
        realm.beginTransaction();
        realm.copyToRealm(user);
        realm.commitTransaction();
        Prefs.with(this).setPreLoad(true);
    }


}
