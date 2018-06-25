package com.example.bahroel.adidasshoeshop.Model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

public class Produk implements Serializable, Comparable<Produk>{
    @SerializedName("id")
    private int id;
    @SerializedName("nama")
    private String nama;
    @SerializedName("harga")
    private int harga;
    @SerializedName("stok")
    private int stok;
    @SerializedName("ukuran")
    private int ukuran;
    @SerializedName("warna")
    private String warna;
    @SerializedName("kategori")
    private String kategori;
    @SerializedName("image_path")
    private String image_path;
    @SerializedName("rating")
    private Float rating;
    @SerializedName("deskripsi")
    private String deskripsi;

    public Produk(){

    }

    public Produk(int id,String nama, int harga, int stok, int ukuran, String warna, String kategori, String image_path, Float rating, String deskripsi) {
        this.id= id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.ukuran = ukuran;
        this.warna = warna;
        this.kategori = kategori;
        this.image_path = image_path;
        this.rating = rating;
        this.deskripsi = deskripsi;
    }

    public String getNama() {
        return nama;
    }

    public int getId() {return id;}

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getUkuran() {
        return ukuran;
    }

    public void setUkuran(int ukuran) {
        this.ukuran = ukuran;
    }

    public String getWarna() {
        return warna;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    @Override
    public int compareTo(@NonNull Produk produk) {
        int compare = rating.compareTo(produk.rating);
        if(compare == 0){
            compare = Integer.compare(harga,produk.harga);
        }
        return compare;
    }

    public static Comparator<Produk> SortByRating = new Comparator<Produk>() {
        @Override
        public int compare(Produk produk, Produk produk1) {
            int compare = produk.getRating().compareTo(produk1.getRating());
            if(compare == 0){
                compare = Integer.compare(produk.getHarga(), produk1.getHarga());
            }
            return compare;
        }
    };

    public static Comparator<Produk> SortByHargaTerendah = new Comparator<Produk>() {
        @Override
        public int compare(Produk produk, Produk produk1) {
            int compare = produk.getHarga()-produk1.getHarga();
            if(compare == 0){
                compare = Float.compare(produk.getRating(), produk1.getRating());
            }
            return compare;
        }
    };

    public static Comparator<Produk> SortByHargaTertinggi = new Comparator<Produk>() {
        @Override
        public int compare(Produk produk, Produk produk1) {
            int compare = produk1.getHarga()-produk.getHarga();
            if(compare == 0){
                compare = Float.compare(produk.getRating(), produk1.getRating());
            }
            return compare;
        }
    };


}
