package com.example.bahroel.adidasshoeshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import com.example.bahroel.adidasshoeshop.Adapter.RilisAdapter;
import com.example.bahroel.adidasshoeshop.Api.ApiInterface;
import com.example.bahroel.adidasshoeshop.Api.ApiRequest;
import com.example.bahroel.adidasshoeshop.Model.Produk;
import com.example.bahroel.adidasshoeshop.Response.ProdukResponse;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ViewFlipper viewFlipper;
    ScrollView scrollView;
    RecyclerView rilis, laris;
    ArrayList<Produk> produkArrayList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int images[] = {R.drawable.flip1,R.drawable.flip2,R.drawable.flip3};
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        rilis = findViewById(R.id.rv_baru_rilis);
        RelativeLayout.LayoutParams parameter =  (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
        parameter.setMargins(0, 0, 0, 30); // left, top, right, bottom
        scrollView.setLayoutParams(parameter);
        viewFlipper = (ViewFlipper)findViewById(R.id.viewflipper1);
        for (int image:images){
            flipperImage(image);
        }


        ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
        Call<ProdukResponse> call = request.getProdukJSON();
        call.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                ProdukResponse jsonresponse = response.body();
                produkArrayList = new ArrayList<>(Arrays.asList(jsonresponse.getProduks()));

            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {

            }
        });


        rilis.setHasFixedSize(true);
        rilis.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
        rilis.setAdapter(new RilisAdapter(MainActivity.this,produkArrayList));



    }


    private void flipperImage(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
}
