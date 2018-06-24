package com.example.bahroel.adidasshoeshop.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bahroel.adidasshoeshop.App.Prefs;
import com.example.bahroel.adidasshoeshop.CartActivity;
import com.example.bahroel.adidasshoeshop.Constant;
import com.example.bahroel.adidasshoeshop.Model.ProdukCart;
import com.example.bahroel.adidasshoeshop.R;
import com.example.bahroel.adidasshoeshop.Realm.RealmController;
import com.example.bahroel.adidasshoeshop.Realm.RealmRecyclerViewAdapter;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;

import java.text.NumberFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class ProdukCartAdapter extends RealmRecyclerViewAdapter<ProdukCart> {
    final Context context;
    private Realm realm;
    private LayoutInflater inflater;
    int total = 0;

    BrokenView brokenView;
    BrokenTouchListener brokenTouchListener;

    public ProdukCartAdapter(Context context) {
        this.context = context;
    }
    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk_cart, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        realm = RealmController.getInstance().getRealm();

        final ProdukCart produk = getItem(position);
        final ProdukCartAdapter.CardViewHolder holder = (ProdukCartAdapter.CardViewHolder) viewHolder;

        Glide.with(context)
                .load(Constant.BASE_URL + produk.getImg_produk())
                .override(500,500)
                .into(holder.img_produk);
        holder.nama.setText(produk.getNama());
        holder.kategori.setText(produk.getKategori());
        holder.jumlah.setText("Jumlah  : "+produk.getJumlah());
        String str = NumberFormat.getNumberInstance(Locale.US).format(produk.getHarga());
        holder.harga.setText("Rp. "+str);
        holder.ukuran.setText("Ukuran   : "+ produk.getUkuran());

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.card.setOnTouchListener(CartActivity.brokenTouchListener);

                Handler handler = null;
                handler = new Handler();
                handler.postDelayed(new Runnable(){
                    public void run(){
                        RealmResults<ProdukCart> results = realm.where(ProdukCart.class).findAll();
                        // Get the book title to show it in toast message
                        ProdukCart produkCart = results.get(position);
                        String name = produkCart.getNama();

                        realm.beginTransaction();
                        results.remove(position);
                        realm.commitTransaction();

                        notifyDataSetChanged();

                        total = 0;
                        for(int i =0; i< results.size(); i++){
                            total += results.get(i).getHarga();
                        }
                        String str = NumberFormat.getNumberInstance(Locale.US).format(total);
                        CartActivity.total_bayar.setText("Rp. "+str);

                        Toast.makeText(context, name + " Berhasil dihapus dari keranjang", Toast.LENGTH_SHORT).show();
                    }
                }, 2600);
                return false;
            }
        });

    }

    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public TextView nama, kategori, jumlah, ukuran, harga;
        public ImageView img_produk;

        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_view);
            nama = (TextView) itemView.findViewById(R.id.tv_nama_produk_list_produk);
            kategori = (TextView) itemView.findViewById(R.id.tv_kategori_produk_list);
            jumlah = (TextView) itemView.findViewById(R.id.tv_jumlah_produk_list);
            ukuran = (TextView) itemView.findViewById(R.id.tv_ukuran_produk_list);
            harga = (TextView) itemView.findViewById(R.id.tv_harga_produk_list);
            img_produk = itemView.findViewById(R.id.img_produk_cart);
        }
    }
}
