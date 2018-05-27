package com.example.bahroel.adidasshoeshop.Model;

public class Produk {
    private String nama;
    private int harga;
    private int stok;
    private int ukuran;
    private String warna;
    private String kategori;
    private String image_path;
    private Float rating;
    private String deskripsi;

    public Produk(String nama, int harga, int stok, int ukuran, String warna, String kategori, String image_path, Float rating, String deskripsi) {
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
}
