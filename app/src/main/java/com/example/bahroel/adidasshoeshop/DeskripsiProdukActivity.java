package com.example.bahroel.adidasshoeshop;

import android.content.Intent;
import android.graphics.Color;
import android.media.Rating;
import android.opengl.Visibility;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bahroel.adidasshoeshop.Api.ApiInterface;
import com.example.bahroel.adidasshoeshop.Api.ApiRequest;
import com.example.bahroel.adidasshoeshop.Model.Produk;
import com.example.bahroel.adidasshoeshop.Model.ProdukCart;
import com.example.bahroel.adidasshoeshop.Realm.RealmController;
import com.example.bahroel.adidasshoeshop.Response.ProdukDetailResponse;
import com.example.bahroel.adidasshoeshop.Response.ProdukResponse;
import com.example.bahroel.adidasshoeshop.Response.UserResponse;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.NumberFormat;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import nl.dionsegijn.steppertouch.StepperTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeskripsiProdukActivity extends AppCompatActivity {
    StepperTouch stepperTouch;
    ImageView back, image, img_warna;
    RelativeLayout bg_avi;
    TextView kategori, upName, downName , price, color, size, description;
    RatingBar rating;
    ScrollView scrollView;
    int id_produk, jumlah;
    LinearLayout stockHabis;
    AVLoadingIndicatorView avi;
    LinearLayout btnAddCart;
    private Realm realm;
    Produk produk = new Produk();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_produk);

        bg_avi = findViewById(R.id.bg_avi);
        bg_avi.setVisibility(View.VISIBLE);
        btnAddCart = findViewById(R.id.btn_add_to_cart);
        img_warna = findViewById(R.id.iv_detail_warna);
        stockHabis = findViewById(R.id.stockHabis);
        scrollView = findViewById(R.id.scrollDescripton);
        scrollView.setVisibility(View.GONE);
        image = findViewById(R.id.iv_detail_produk);
        kategori = findViewById(R.id.tv_detail_kategori);
        upName = findViewById(R.id.tv_detail_nama);
        downName = findViewById(R.id.tv_detail_nama_down);
        price = findViewById(R.id.tv_detail_harga);
        color = findViewById(R.id.tv_detail_warna);
        size = findViewById(R.id.tv_size_detail);
        rating = findViewById(R.id.ratingbar);
        description = findViewById(R.id.tv_des_detail);
        avi = findViewById(R.id.avi_progress_dialog);
        stepperTouch = findViewById(R.id.stepper);
        stepperTouch.stepper.setMin(0);




        back = findViewById(R.id.imgMenu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeskripsiProdukActivity.this, MainActivity.class);
                intent.putExtra("from",0);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        this.realm = RealmController.with(this).getRealm();

        Bundle bundle = getIntent().getExtras();
        id_produk = bundle.getInt("id_produk");
        avi.smoothToShow();
        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                avi.hide();
                bg_avi.setVisibility(View.GONE);
            }
        }, 2500);
        ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
        Call<ProdukDetailResponse> call = request.getDetailProdukJSON(id_produk);
        call.enqueue(new Callback<ProdukDetailResponse>() {
            @Override
            public void onResponse(Call<ProdukDetailResponse> call, Response<ProdukDetailResponse> response) {
                ProdukDetailResponse jsonresponse = response.body();

                produk.setId(jsonresponse.getProduks().getId());
                produk.setDeskripsi(jsonresponse.getProduks().getDeskripsi());
                produk.setHarga(jsonresponse.getProduks().getHarga());
                produk.setImage_path(jsonresponse.getProduks().getImage_path());
                produk.setKategori(jsonresponse.getProduks().getKategori());
                produk.setNama(jsonresponse.getProduks().getNama());
                produk.setRating(jsonresponse.getProduks().getRating());
                produk.setStok(jsonresponse.getProduks().getStok());
                produk.setUkuran(jsonresponse.getProduks().getUkuran());
                produk.setWarna(jsonresponse.getProduks().getWarna());

                Glide.with(DeskripsiProdukActivity.this)
                        .load(Constant.BASE_URL + jsonresponse.getProduks().getImage_path())
                        .override(1200,1500)
                        .into(image);
                upName.setText(jsonresponse.getProduks().getNama());
                downName.setText(jsonresponse.getProduks().getNama());
                rating.setRating(jsonresponse.getProduks().getRating());
                kategori.setText(jsonresponse.getProduks().getKategori());
                color.setText("Warna : " + jsonresponse.getProduks().getWarna());
                size.setText("Size " + jsonresponse.getProduks().getUkuran());
                description.setText(jsonresponse.getProduks().getDeskripsi());
                String str = NumberFormat.getNumberInstance(Locale.US).format(jsonresponse.getProduks().getHarga());
                price.setText("Rp. "+str);
                stepperTouch.stepper.setMax(jsonresponse.getProduks().getStok());

                if(jsonresponse.getProduks().getWarna().equals("Merah")){
                    img_warna.setImageResource(R.drawable.ic_warna_merah);
                }else if (jsonresponse.getProduks().getWarna().equals("Abu-abu")){
                    img_warna.setImageResource(R.drawable.ic_warna_abu);
                }else if (jsonresponse.getProduks().getWarna().equals("Putih")){
                    img_warna.setImageResource(R.drawable.ic_warna_putih);
                }else if (jsonresponse.getProduks().getWarna().equals("Hitam")){
                    img_warna.setImageResource(R.drawable.ic_warna_hitam);
                }

                if(jsonresponse.getProduks().getStok() > 0){
                    stockHabis.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ProdukDetailResponse> call, Throwable t) {

            }
        });
        scrollView.setVisibility(View.VISIBLE);

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(stepperTouch.stepper.getValue() > 0){
                    final ProdukCart produkCart = new ProdukCart();
                    produkCart.setId(RealmController.getInstance().getAllProdukCart().size() + System.currentTimeMillis());
                    produkCart.setHarga(produk.getHarga());
                    produkCart.setId_produk(produk.getId());
                    produkCart.setKategori(produk.getKategori());
                    produkCart.setNama(produk.getNama());
                    produkCart.setUkuran(produk.getUkuran());
                    produkCart.setImg_produk(produk.getImage_path());
                    produkCart.setJumlah(stepperTouch.stepper.getValue());

                    SweetAlertDialog dialog = new SweetAlertDialog(DeskripsiProdukActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Ingin menambahkan ke keranjang ?")
                            .setConfirmText("Ya")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog
                                            .setTitleText("Sukses!")
                                            .setContentText("Barang telah ditambahkan ke keranjang")
                                            .setCancelText(null)
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                    realm.beginTransaction();
                                    realm.copyToRealm(produkCart);
                                    realm.commitTransaction();
                                }
                            });
                    dialog.getProgressHelper().setBarColor(Color.parseColor("#0089D0"));
                    dialog.show();
                }else{
                    Toast.makeText(DeskripsiProdukActivity.this, "Jumlah produk tidak boleh 0", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
