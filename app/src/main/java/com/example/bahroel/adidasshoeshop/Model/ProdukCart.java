package com.example.bahroel.adidasshoeshop.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProdukCart extends RealmObject {
    @PrimaryKey
    private long id;
    private String nama;
    private String kategori;
    private int ukuran;
    private int jumlah;
    private int harga;
    private int id_produk;

    public ProdukCart() {
    }

    public ProdukCart(long id, String nama, String kategori, int ukuran, int jumlah, int harga, int id_produk) {
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        this.ukuran = ukuran;
        this.jumlah = jumlah;
        this.harga = harga;
        this.id_produk = id_produk;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getUkuran() {
        return ukuran;
    }

    public void setUkuran(int ukuran) {
        this.ukuran = ukuran;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getId_produk() {
        return id_produk;
    }

    public void setId_produk(int id_produk) {
        this.id_produk = id_produk;
    }
}
