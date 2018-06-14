package com.example.bahroel.adidasshoeshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                android.content.Intent i = new android.content.Intent(SplashScreen.this, MainActivity.class);
                i.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("from",1);
                startActivity(i);
                finish();
            }
        },3000);
    }
}
