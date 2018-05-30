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

import com.example.bahroel.adidasshoeshop.Behavior.BottomNavigationBehavior;
import com.example.bahroel.adidasshoeshop.Fragment.HomeFragment;
import com.example.bahroel.adidasshoeshop.User.DaftarActivity;
import com.example.bahroel.adidasshoeshop.User.LoginActivity;

public class MainActivity extends AppCompatActivity {

    ImageView imgToogle;
    LinearLayout lnr_user, daftar, masuk, lnr_user_logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgToogle = findViewById(R.id.imgMenu);
        lnr_user = findViewById(R.id.lnr_user);
        lnr_user_logged = findViewById(R.id.lnr_user_logged);
        daftar = findViewById(R.id.lnr_daftar);
        masuk = findViewById(R.id.lnr_masuk);
        lnr_user.setVisibility(View.GONE);
        lnr_user_logged.setVisibility(View.GONE);

        imgToogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lnr_user_logged.getVisibility() == View.GONE){
                    imgToogle.setImageResource(R.drawable.ic_keyboard_backspace);
                    lnr_user_logged.setVisibility(View.VISIBLE);
                }else{
                    imgToogle.setImageResource(R.drawable.ic_menu);
                    lnr_user_logged.setVisibility(View.GONE);
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




        HomeFragment home = new HomeFragment();
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.replace(R.id.fragment_main, home);
        FT.commit();

    }




}
