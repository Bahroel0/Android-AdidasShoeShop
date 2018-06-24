package com.example.bahroel.adidasshoeshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bahroel.adidasshoeshop.Constant;
import com.example.bahroel.adidasshoeshop.DeskripsiProdukActivity;
import com.example.bahroel.adidasshoeshop.MainActivity;
import com.example.bahroel.adidasshoeshop.Model.Produk;
import com.example.bahroel.adidasshoeshop.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RilisAdapter extends RecyclerView.Adapter<RilisAdapter.ViewHolder>{
    Context context;
    ArrayList<Produk> produkList = new ArrayList<>();

    public RilisAdapter(Context context, ArrayList<Produk> produkList) {
        this.context = context;
        this.produkList = produkList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_produk,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context)
                .load(Constant.BASE_URL + produkList.get(position).getImage_path())
                .override(500,500)
                .into(holder.foto);
        if(produkList.get(position).getWarna().equals("Merah")){
            holder.warna.setImageResource(R.drawable.ic_warna_merah);
        }else if (produkList.get(position).getWarna().equals("Abu-abu")){
            holder.warna.setImageResource(R.drawable.ic_warna_abu);
        }else if (produkList.get(position).getWarna().equals("Putih")){
            holder.warna.setImageResource(R.drawable.ic_warna_putih);
        }else if (produkList.get(position).getWarna().equals("Hitam")){
            holder.warna.setImageResource(R.drawable.ic_warna_hitam);
        }
        holder.nama.setText(produkList.get(position).getNama());
        holder.kategori.setText(produkList.get(position).getKategori());
        String str = NumberFormat.getNumberInstance(Locale.US).format(produkList.get(position).getHarga());
        holder.harga.setText("Rp. "+str);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DeskripsiProdukActivity.class);
                intent.putExtra("id_produk", produkList.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        ImageView warna;
        TextView nama, kategori, harga;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);

            foto = (ImageView) itemView.findViewById(R.id.iv_foto_produk);
            warna = (ImageView) itemView.findViewById(R.id.iv_warna_produk);
            nama = (TextView)itemView.findViewById(R.id.tv_nama_produk);
            kategori =(TextView)itemView.findViewById(R.id.tv_kategori_produk);
            harga = (TextView)itemView.findViewById(R.id.tv_harga_produk);
            cardView = itemView.findViewById(R.id.cv_produk);
        }
    }
}
