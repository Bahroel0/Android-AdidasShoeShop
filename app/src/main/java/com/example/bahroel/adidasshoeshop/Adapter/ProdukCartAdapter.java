package com.example.bahroel.adidasshoeshop.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bahroel.adidasshoeshop.App.Prefs;
import com.example.bahroel.adidasshoeshop.Model.ProdukCart;
import com.example.bahroel.adidasshoeshop.R;
import com.example.bahroel.adidasshoeshop.Realm.RealmController;
import com.example.bahroel.adidasshoeshop.Realm.RealmRecyclerViewAdapter;

import io.realm.Realm;

public class ProdukCartAdapter extends RealmRecyclerViewAdapter<ProdukCart> {
    final Context context;
    private Realm realm;
    private LayoutInflater inflater;

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
            kategori = (TextView) itemView.findViewById(R.id.tv_kategori_produk_list_produk);
            nama = (TextView) itemView.findViewById(R.id.tv_nama_produk_list_produk);
            kategori = (TextView) itemView.findViewById(R.id.tv_kategori_produk_list_produk);
            img_produk = itemView.findViewById(R.id.img_produk_cart);
        }
    }
}
