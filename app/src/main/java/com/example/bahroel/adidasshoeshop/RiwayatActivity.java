package com.example.bahroel.adidasshoeshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.bahroel.adidasshoeshop.Adapter.RilisAdapter;
import com.example.bahroel.adidasshoeshop.Adapter.RiwayatAdapter;
import com.example.bahroel.adidasshoeshop.Api.ApiInterface;
import com.example.bahroel.adidasshoeshop.Api.ApiRequest;
import com.example.bahroel.adidasshoeshop.Model.Riwayat;
import com.example.bahroel.adidasshoeshop.Model.UserLogged;
import com.example.bahroel.adidasshoeshop.Realm.RealmController;
import com.example.bahroel.adidasshoeshop.Response.ProdukResponse;
import com.example.bahroel.adidasshoeshop.Response.RiwayatResponse;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatActivity extends AppCompatActivity {
    ImageView back;
    RecyclerView rvRiwayat;
    private Realm realm;
    ArrayList<Riwayat> riwayatArrayList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        this.realm = RealmController.with(this).getRealm();
        back = findViewById(R.id.imgMenu);
        rvRiwayat = findViewById(R.id.rv_riwayat);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RiwayatActivity.this, MainActivity.class);
                intent.putExtra("from",1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        RealmResults<UserLogged> result = realm.where(UserLogged.class).findAll();
        realm.beginTransaction();
        int id_user = result.get(0).getId();
        String api_token = result.get(0).getApi_token();
        realm.commitTransaction();
        ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
        Call<RiwayatResponse> call = request.getRiwayat(id_user,api_token);
        call.enqueue(new Callback<RiwayatResponse>() {
            @Override
            public void onResponse(Call<RiwayatResponse> call, Response<RiwayatResponse> response) {
                if(response.body().isSucsess()){
                    riwayatArrayList = new ArrayList<>(Arrays.asList(response.body().getRiwayats()));
                    rvRiwayat.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RiwayatActivity.this);
                    rvRiwayat.setLayoutManager(layoutManager);
                    RiwayatAdapter adapter = new RiwayatAdapter(RiwayatActivity.this, riwayatArrayList);
                    rvRiwayat.setAdapter(adapter);}
            }

            @Override
            public void onFailure(Call<RiwayatResponse> call, Throwable t) {

            }
        });

    }





}
