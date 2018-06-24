package com.example.bahroel.adidasshoeshop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bahroel.adidasshoeshop.Adapter.ProdukCartAdapter;
import com.example.bahroel.adidasshoeshop.Api.ApiInterface;
import com.example.bahroel.adidasshoeshop.Api.ApiRequest;
import com.example.bahroel.adidasshoeshop.Model.ProdukCart;
import com.example.bahroel.adidasshoeshop.Model.UserLogged;
import com.example.bahroel.adidasshoeshop.Realm.RealmController;
import com.example.bahroel.adidasshoeshop.Realm.RealmProdukCartAdapter;
import com.example.bahroel.adidasshoeshop.Response.PaymentResponse;
import com.example.bahroel.adidasshoeshop.Response.UserResponse;
import com.example.bahroel.adidasshoeshop.User.DaftarActivity;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;

import java.text.NumberFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    ImageView back;
    ProdukCartAdapter adapter;
    public static TextView total_bayar;
    public static Realm realm;
    public static int total =0;
    private LayoutInflater inflater;
    private RecyclerView recycler;
    Button bayar;
    public String api_token;
    public static int delay;

    public BrokenView brokenView;
    public static BrokenTouchListener brokenTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        brokenView = BrokenView.add2Window(this);
        brokenTouchListener = new BrokenTouchListener.Builder(brokenView).build();

        back = findViewById(R.id.imgMenu);
        total_bayar =findViewById(R.id.total_bayar_cart);
        bayar = findViewById(R.id.btn_bayar);
        recycler = findViewById(R.id.rv_cart);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                intent.putExtra("from",1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });




        this.realm = RealmController.with(this).getRealm();

        RealmController.with(this).refresh();
        RealmResults<ProdukCart> results = realm.where(ProdukCart.class).findAll();
        for(int i=0; i<results.size(); i++){
            if(results.get(i).getJumlah()==0){
                realm.beginTransaction();
                results.remove(i);
                realm.commitTransaction();
            }
        }
        total=0;
        for(int i =0; i< results.size(); i++){
            total += results.get(i).getHarga();
        }
        String str = NumberFormat.getNumberInstance(Locale.US).format(total);
        total_bayar.setText("Rp. "+str);

        setupRecycler();
        // refresh the realm instance
        RealmController.with(this).refresh();

        setRealmAdapter(RealmController.with(this).getAllProdukCart());

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(CartActivity.this);
                dialog.getWindow();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_pembayaran);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button oke = dialog.findViewById(R.id.btn_bayar_oke);
                Button batal = dialog.findViewById(R.id.btn_bayar_batal);
                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                oke.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        final ProgressDialog progressDialog = new ProgressDialog(CartActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Proses Pembelian . . .");
                        progressDialog.show();

                        realm = RealmController.with(CartActivity.this).getRealm();
                        RealmResults<UserLogged> result = realm.where(UserLogged.class).findAll();
                        realm.beginTransaction();
                        int id_user = result.get(0).getId();
                        api_token = result.get(0).getApi_token();
                        realm.commitTransaction();
                        final ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
                        Call<PaymentResponse> call = request.addRiwayat(id_user, CartActivity.total,api_token);
                        call.enqueue(new Callback<PaymentResponse>() {
                            @Override
                            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                                if(response.body().isSuccess()){
                                    realm = RealmController.with(CartActivity.this).getRealm();
                                    final RealmResults<ProdukCart> result = realm.where(ProdukCart.class).findAll();
                                    realm.beginTransaction();
                                    delay = result.size();
                                    for(int i=0; i<result.size(); i++){
                                        int id_produk = result.get(i).getId_produk();
                                        int jumlah = result.get(i).getJumlah();
                                        result.remove(i);
                                        setRealmAdapter(result);
                                        Call<PaymentResponse> call1 = request.payment(id_produk,jumlah,api_token);
                                        call1.enqueue(new Callback<PaymentResponse>() {
                                            @Override
                                            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<PaymentResponse> call, Throwable t) {

                                            }
                                        });

                                    }

                                    Handler handler = null;
                                    handler = new Handler();
                                    handler.postDelayed(new Runnable(){
                                        public void run(){
                                            progressDialog.dismiss();
                                        }
                                    }, delay*1000);
                                    realm.commitTransaction();
                                }
                            }

                            @Override
                            public void onFailure(Call<PaymentResponse> call, Throwable t) {

                            }
                        });

                    }
                });

            }
        });


    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new ProdukCartAdapter(this);
        recycler.setAdapter(adapter);
    }


    public void setRealmAdapter(RealmResults<ProdukCart> data) {

        RealmProdukCartAdapter realmAdapter = new RealmProdukCartAdapter(this.getApplicationContext(), data, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }
}
