package com.example.bahroel.adidasshoeshop.Fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.bahroel.adidasshoeshop.Adapter.RilisAdapter;
import com.example.bahroel.adidasshoeshop.Api.ApiInterface;
import com.example.bahroel.adidasshoeshop.Api.ApiRequest;
import com.example.bahroel.adidasshoeshop.MainActivity;
import com.example.bahroel.adidasshoeshop.Model.Produk;
import com.example.bahroel.adidasshoeshop.R;
import com.example.bahroel.adidasshoeshop.Response.ProdukResponse;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment{
    ViewFlipper viewFlipper;
    ScrollView scrollView;
    RecyclerView rilis, laris;
    ArrayList<Produk> produkArrayList= new ArrayList<>();
    Context context;
    TextView tvLihatSemua;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View viewFrag1 = inflater.inflate(R.layout.home_fragment, container, false);


        int images[] = {R.drawable.flip4,R.drawable.flip1,R.drawable.flip2,R.drawable.flip3};
        scrollView = (ScrollView)viewFrag1.findViewById(R.id.scrollView);
        rilis = (RecyclerView) viewFrag1.findViewById(R.id.rv_baru_rilis);
        laris = (RecyclerView) viewFrag1.findViewById(R.id.rv_produk_terlaris);
        tvLihatSemua = (TextView)viewFrag1.findViewById(R.id.txt1);

        tvLihatSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KatalogFragment katalogFragment = new KatalogFragment();
                FragmentManager FM = getActivity().getSupportFragmentManager();
                FragmentTransaction FT = FM.beginTransaction();
                FT.replace(R.id.fragment_main, katalogFragment);
                FT.commit();
            }
        });

        viewFlipper = (ViewFlipper)viewFrag1.findViewById(R.id.viewflipper1);
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
                Log.d(MainActivity.class.getSimpleName(),"nilai produk : " + produkArrayList.get(0).getNama() );

                rilis.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
                rilis.setLayoutManager(layoutManager);
                RilisAdapter adapter = new RilisAdapter(getActivity(), produkArrayList);
                rilis.setAdapter(adapter);

                laris.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
                laris.setLayoutManager(layoutManager1);
                RilisAdapter adapter1 = new RilisAdapter(getActivity(), produkArrayList);
                laris.setAdapter(adapter1);
            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {

            }
        });


        return viewFrag1;
    }

    private void flipperImage(int image) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(getActivity(),android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getActivity(),android.R.anim.slide_out_right);
    }
}
