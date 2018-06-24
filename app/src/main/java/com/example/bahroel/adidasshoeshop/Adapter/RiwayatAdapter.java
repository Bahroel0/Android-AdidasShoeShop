package com.example.bahroel.adidasshoeshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bahroel.adidasshoeshop.Constant;
import com.example.bahroel.adidasshoeshop.DeskripsiProdukActivity;
import com.example.bahroel.adidasshoeshop.Model.Produk;
import com.example.bahroel.adidasshoeshop.Model.Riwayat;
import com.example.bahroel.adidasshoeshop.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {
    Context context;
    ArrayList<Riwayat> riwayats = new ArrayList<>();

    public RiwayatAdapter(Context context, ArrayList<Riwayat> riwayats) {
        this.context = context;
        this.riwayats = riwayats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_riwayat,null));
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatAdapter.ViewHolder holder, final int position) {
        String str = NumberFormat.getNumberInstance(Locale.US).format(riwayats.get(position).getTotal());
        holder.harga.setText("Total Bayar : Rp. " + str);
        holder.tanggal.setText(riwayats.get(position).getTanggal());

    }

    @Override
    public int getItemCount() {
        return riwayats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tanggal, harga;

        public ViewHolder(View itemView) {
            super(itemView);
            harga = (TextView)itemView.findViewById(R.id.tv_harga_riwayat);
            tanggal = (TextView) itemView.findViewById(R.id.tv_tanggal_riwayat);
        }
    }

}
