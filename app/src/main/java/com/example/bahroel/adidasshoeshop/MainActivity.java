package com.example.bahroel.adidasshoeshop;

import android.app.Fragment;
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
import android.widget.RelativeLayout;

import com.example.bahroel.adidasshoeshop.Behavior.BottomNavigationBehavior;
import com.example.bahroel.adidasshoeshop.Fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        HomeFragment home = new HomeFragment();
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.replace(R.id.fragment_main, home);
        FT.commit();

    }




}
