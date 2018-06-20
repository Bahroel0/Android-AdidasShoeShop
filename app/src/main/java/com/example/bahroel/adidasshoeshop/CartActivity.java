package com.example.bahroel.adidasshoeshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bahroel.adidasshoeshop.Adapter.ProdukCartAdapter;
import com.example.bahroel.adidasshoeshop.Model.ProdukCart;
import com.example.bahroel.adidasshoeshop.Realm.RealmController;
import com.example.bahroel.adidasshoeshop.Realm.RealmProdukCartAdapter;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;

import java.text.NumberFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class CartActivity extends AppCompatActivity {
    ImageView back;
    ProdukCartAdapter adapter;
    public static TextView total_bayar;
    private Realm realm;
    int total =0;
    private LayoutInflater inflater;
    private RecyclerView recycler;

    public BrokenView brokenView;
    public static BrokenTouchListener brokenTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        brokenView = BrokenView.add2Window(this);
        brokenTouchListener = new BrokenTouchListener.Builder(brokenView).build();

        back = findViewById(R.id.imgMenu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                intent.putExtra("from",1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



        total_bayar =findViewById(R.id.total_bayar_cart);



        recycler = findViewById(R.id.rv_cart);
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
        for(int i =0; i< results.size(); i++){
            total += results.get(i).getHarga();
        }
        String str = NumberFormat.getNumberInstance(Locale.US).format(total);
        total_bayar.setText("Rp. "+str);

        setupRecycler();
        // refresh the realm instance
        RealmController.with(this).refresh();

        setRealmAdapter(RealmController.with(this).getAllProdukCart());



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


    public void setRealmAdapter(RealmResults<ProdukCart> warehousedestinations) {

        RealmProdukCartAdapter realmAdapter = new RealmProdukCartAdapter(this.getApplicationContext(), warehousedestinations, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }
}
