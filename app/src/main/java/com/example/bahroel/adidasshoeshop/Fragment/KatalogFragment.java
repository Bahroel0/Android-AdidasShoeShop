package com.example.bahroel.adidasshoeshop.Fragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bahroel.adidasshoeshop.Adapter.KatalogAdapter;
import com.example.bahroel.adidasshoeshop.Adapter.RilisAdapter;
import com.example.bahroel.adidasshoeshop.Api.ApiInterface;
import com.example.bahroel.adidasshoeshop.Api.ApiRequest;
import com.example.bahroel.adidasshoeshop.Behavior.BottomNavigationBehavior;
import com.example.bahroel.adidasshoeshop.MainActivity;
import com.example.bahroel.adidasshoeshop.Model.Produk;
import com.example.bahroel.adidasshoeshop.R;
import com.example.bahroel.adidasshoeshop.Response.ProdukResponse;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KatalogFragment extends Fragment {
    RecyclerView katalog;
    int last_page;
    int page = 2;
    public static ArrayList<Produk> produkArrayList= new ArrayList<>();
    LinearLayout lnr_filter, lnr_sort, lnr_no_produk;
    boolean userScrolled = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static RelativeLayout bottomLayout;
    private KatalogAdapter adapter;
    private static LinearLayoutManager layoutManager;
    Button btn_sorting_sekarang,btn_filter_sekarang;
    String kategori, searchkey;

    RadioGroup rg_sorting;
    // filter
    TextView tv_harga_min_filter,tv_harga_max_filter,tv_ukuran_filter;
    CheckBox cb_merah,cb_abu ,cb_hitam,cb_putih;
    RangeSeekBar<Integer> seekbar_harga_filter;
    ImageView iv_minus_ukuran_filter,iv_plus_ukuran_filter;

    public int MIN_PRICE, MAX_PRICE, SHOE_SIZE=40;
    public boolean sekbartouch = false;
    public ArrayList<String> WARNA= new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View viewFrag1 = inflater.inflate(R.layout.katalog_fragment, container, false);

        kategori = getArguments().getString("kategori");

        BottomNavigationView navigation = (BottomNavigationView) viewFrag1.findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.action_home).setChecked(true);
        final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        bottomLayout = (RelativeLayout) viewFrag1.findViewById(R.id.loadItemsLayout_recyclerView);

        katalog = (RecyclerView)viewFrag1.findViewById(R.id.rv_katalog);
        lnr_filter = viewFrag1.findViewById(R.id.lnr_filter);
        lnr_sort = viewFrag1.findViewById(R.id.lnr_sorting);
        lnr_no_produk = viewFrag1.findViewById(R.id.lnr_no_produk);
        btn_sorting_sekarang = viewFrag1.findViewById(R.id.btn_sorting_sekarang);
        btn_filter_sekarang = viewFrag1.findViewById(R.id.btn_filter_sekarang);
        rg_sorting = viewFrag1.findViewById(R.id.rg_sorting);

        //filter identified
        tv_harga_min_filter = viewFrag1.findViewById(R.id.tv_harga_min_filter);
        tv_harga_max_filter = viewFrag1.findViewById(R.id.tv_harga_max_filter);
        tv_ukuran_filter = viewFrag1.findViewById(R.id.tv_ukuran_filter);
        seekbar_harga_filter = viewFrag1.findViewById(R.id.seekbar_harga_filter);
        iv_minus_ukuran_filter = viewFrag1.findViewById(R.id.iv_minus_ukuran_filter);
        iv_plus_ukuran_filter = viewFrag1.findViewById(R.id.iv_plus_ukuran_filter);
        cb_merah = viewFrag1.findViewById(R.id.cb_merah);
        cb_abu = viewFrag1.findViewById(R.id.cb_abu);
        cb_putih = viewFrag1.findViewById(R.id.cb_putih);
        cb_hitam = viewFrag1.findViewById(R.id.cb_hitam);


        seekbar_harga_filter.setRangeValues(0,5000000);
        seekbar_harga_filter.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                sekbartouch = true;
                MIN_PRICE = minValue;
                MAX_PRICE = maxValue;
                String str = NumberFormat.getNumberInstance(Locale.US).format(minValue);
                String str1 = NumberFormat.getNumberInstance(Locale.US).format(maxValue);
                tv_harga_min_filter.setText("Rp. "+ str);
                tv_harga_max_filter.setText("Rp. "+ str1);

            }
        });

        if(kategori.equals("semua")){
            ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
            Call<ProdukResponse> call = request.getProdukJSON(1);
            call.enqueue(new Callback<ProdukResponse>() {
                @Override
                public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                    ProdukResponse jsonresponse = response.body();
                    last_page = jsonresponse.getLast_page();
                    produkArrayList = new ArrayList<>(Arrays.asList(jsonresponse.getProduks()));
                    Log.d(MainActivity.class.getSimpleName(),"nilai produk : " + produkArrayList.get(0).getNama() );

                    katalog.setHasFixedSize(true);
                    layoutManager = new GridLayoutManager(getActivity(), 3);
                    katalog.setLayoutManager(layoutManager);
                    katalog.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
                    katalog.setItemAnimator(new DefaultItemAnimator());
                    adapter = new KatalogAdapter(getActivity(), produkArrayList);
                    katalog.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ProdukResponse> call, Throwable t) {

                }
            });
        }else if(kategori.equals("wanita")){
            ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
            Call<ProdukResponse> call = request.getKategoriProdukJSON("Wanita",1);
            call.enqueue(new Callback<ProdukResponse>() {
                @Override
                public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                    ProdukResponse jsonresponse = response.body();
                    last_page = jsonresponse.getLast_page();
                    produkArrayList = new ArrayList<>(Arrays.asList(jsonresponse.getProduks()));
//                    Log.d(MainActivity.class.getSimpleName(),"nilai produk : " + produkArrayList.get(0).getNama() );

                    katalog.setHasFixedSize(true);
                    layoutManager = new GridLayoutManager(getActivity(), 3);
                    katalog.setLayoutManager(layoutManager);
                    katalog.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
                    katalog.setItemAnimator(new DefaultItemAnimator());
                    adapter = new KatalogAdapter(getActivity(), produkArrayList);
                    katalog.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ProdukResponse> call, Throwable t) {

                }
            });
        }else if(kategori.equals("pria")){
            ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
            Call<ProdukResponse> call = request.getKategoriProdukJSON("Pria",1);
            call.enqueue(new Callback<ProdukResponse>() {
                @Override
                public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                    ProdukResponse jsonresponse = response.body();
                    last_page = jsonresponse.getLast_page();
                    produkArrayList = new ArrayList<>(Arrays.asList(jsonresponse.getProduks()));
//                    Log.d(MainActivity.class.getSimpleName(),"nilai produk : " + produkArrayList.get(0).getNama() );

                    katalog.setHasFixedSize(true);
                    layoutManager = new GridLayoutManager(getActivity(), 3);
                    katalog.setLayoutManager(layoutManager);
                    katalog.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
                    katalog.setItemAnimator(new DefaultItemAnimator());
                    adapter = new KatalogAdapter(getActivity(), produkArrayList);
                    katalog.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ProdukResponse> call, Throwable t) {

                }
            });
        }else if(kategori.equals("anak")){
            ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
            Call<ProdukResponse> call = request.getKategoriProdukJSON("Anak-Anak",1);
            call.enqueue(new Callback<ProdukResponse>() {
                @Override
                public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                    ProdukResponse jsonresponse = response.body();
                    last_page = jsonresponse.getLast_page();
                    produkArrayList = new ArrayList<>(Arrays.asList(jsonresponse.getProduks()));
//                    Log.d(MainActivity.class.getSimpleName(),"nilai produk : " + produkArrayList.get(0).getNama() );

                    katalog.setHasFixedSize(true);
                    layoutManager = new GridLayoutManager(getActivity(), 3);
                    katalog.setLayoutManager(layoutManager);
                    katalog.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
                    katalog.setItemAnimator(new DefaultItemAnimator());
                    adapter = new KatalogAdapter(getActivity(), produkArrayList);
                    katalog.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ProdukResponse> call, Throwable t) {

                }
            });
        }else if(kategori.equals("Search")){
            searchkey = getArguments().getString("searchkey").toLowerCase();
            final Dialog dialog = new Dialog(getActivity());
            dialog.getWindow();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_search);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
            Call<ProdukResponse> call = request.getSearchJSON(""+searchkey,1);
            call.enqueue(new Callback<ProdukResponse>() {
                @Override
                public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                    ProdukResponse jsonresponse = response.body();
                    last_page = jsonresponse.getLast_page();
                    produkArrayList = new ArrayList<>(Arrays.asList(jsonresponse.getProduks()));

                    if (produkArrayList.size()==0){
                        // dialog handler
                        Handler handler = null;
                        handler = new Handler();
                        handler.postDelayed(new Runnable(){
                            public void run(){
                                dialog.dismiss();
                            }
                        }, 1000);

                        // exception
                        lnr_no_produk.setVisibility(View.VISIBLE);

                    }else{
                        Handler handler = null;
                        handler = new Handler();
                        handler.postDelayed(new Runnable(){
                            public void run(){
                                dialog.dismiss();
                            }
                        }, 1000);
                        // exception
//                        Toast.makeText(getActivity(), "Pencarian " + searchkey + " ditemukan", Toast.LENGTH_SHORT).show();
                    }

                    katalog.setHasFixedSize(true);
                    layoutManager = new GridLayoutManager(getActivity(), 3);
                    katalog.setLayoutManager(layoutManager);
                    katalog.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
                    katalog.setItemAnimator(new DefaultItemAnimator());
                    adapter = new KatalogAdapter(getActivity(), produkArrayList);
                    katalog.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ProdukResponse> call, Throwable t) {

                }
            });
        }


        // impelement scroll listener
        katalog.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                if (userScrolled && page <= last_page) {
                    userScrolled = false;

                    updateRecyclerView(last_page);
                }
            }

        });


        return viewFrag1;
    }

    private void updateRecyclerView(final int last){

        if(page <= last_page){
            bottomLayout.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(kategori.equals("semua")){
                    ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
                    Call<ProdukResponse> call = request.getProdukJSON(page);
                    call.enqueue(new Callback<ProdukResponse>() {
                        @Override
                        public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                            ProdukResponse jsonresponse = response.body();
                            int the_last = jsonresponse.getCurrentPage();
                            if(jsonresponse.getProduks() == null || the_last > last_page){
//                                Toast.makeText(getActivity(), "No more data", Toast.LENGTH_SHORT).show();
                            }else{
                                produkArrayList.addAll(Arrays.asList(jsonresponse.getProduks()));
                            }
                            adapter.notifyDataSetChanged();

                            // After adding new data hide the view.
                            bottomLayout.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<ProdukResponse> call, Throwable t) {

                        }
                    });
                }else if(kategori.equals("wanita")){
                    ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
                    Call<ProdukResponse> call = request.getKategoriProdukJSON("Wanita", page);
                    call.enqueue(new Callback<ProdukResponse>() {
                        @Override
                        public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                            ProdukResponse jsonresponse = response.body();
                            int the_last = jsonresponse.getCurrentPage();
                            if(jsonresponse.getProduks() == null || the_last > last_page){
                                Toast.makeText(getActivity(), "No more data", Toast.LENGTH_SHORT).show();
                            }else{
                                produkArrayList.addAll(Arrays.asList(jsonresponse.getProduks()));
                            }
                            adapter.notifyDataSetChanged();

                            // After adding new data hide the view.
                            bottomLayout.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<ProdukResponse> call, Throwable t) {

                        }
                    });
                }else if(kategori.equals("pria")){
                    ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
                    Call<ProdukResponse> call = request.getKategoriProdukJSON("Pria", page);
                    call.enqueue(new Callback<ProdukResponse>() {
                        @Override
                        public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                            ProdukResponse jsonresponse = response.body();
                            int the_last = jsonresponse.getCurrentPage();
                            if(jsonresponse.getProduks() == null || the_last > last_page){
                                Toast.makeText(getActivity(), "No more data", Toast.LENGTH_SHORT).show();
                            }else{
                                produkArrayList.addAll(Arrays.asList(jsonresponse.getProduks()));
                            }
                            adapter.notifyDataSetChanged();

                            // After adding new data hide the view.
                            bottomLayout.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<ProdukResponse> call, Throwable t) {

                        }
                    });
                }else if(kategori.equals("anak")){
                    ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
                    Call<ProdukResponse> call = request.getKategoriProdukJSON("Anak-Anak", page);
                    call.enqueue(new Callback<ProdukResponse>() {
                        @Override
                        public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                            ProdukResponse jsonresponse = response.body();
                            int the_last = jsonresponse.getCurrentPage();
                            if(jsonresponse.getProduks() == null || the_last > last_page){
                                Toast.makeText(getActivity(), "No more data", Toast.LENGTH_SHORT).show();
                            }else{
                                produkArrayList.addAll(Arrays.asList(jsonresponse.getProduks()));
                            }
                            adapter.notifyDataSetChanged();

                            // After adding new data hide the view.
                            bottomLayout.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<ProdukResponse> call, Throwable t) {

                        }
                    });
                }else if(kategori.equals("Search")){
                    ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
                    Call<ProdukResponse> call = request.getSearchJSON(""+searchkey, page);
                    call.enqueue(new Callback<ProdukResponse>() {
                        @Override
                        public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                            ProdukResponse jsonresponse = response.body();
                            int the_last = jsonresponse.getCurrentPage();
                            if(jsonresponse.getProduks() == null || the_last > last_page){
                                Toast.makeText(getActivity(), "No more data", Toast.LENGTH_SHORT).show();
                            }else{
                                produkArrayList.addAll(Arrays.asList(jsonresponse.getProduks()));
                            }
                            adapter.notifyDataSetChanged();

                            // After adding new data hide the view.
                            bottomLayout.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<ProdukResponse> call, Throwable t) {

                        }
                    });
                }
                bottomLayout.setVisibility(View.GONE);
                page += 1;
            }
        }, 5000);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    HomeFragment home = new HomeFragment();
                    FragmentManager FM = getActivity().getSupportFragmentManager();
                    FragmentTransaction FT = FM.beginTransaction();
                    FT.replace(R.id.fragment_main, home);
                    FT.commit();
                    return true;
                case R.id.action_filter:
                    if(lnr_filter.getVisibility() == View.VISIBLE){
                        lnr_filter.setVisibility(View.GONE);
                        lnr_sort.setVisibility(View.GONE);
                        lnr_sort.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down));
                        lnr_filter.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down));
                    }else{
                        lnr_filter.setVisibility(View.VISIBLE);
                        lnr_sort.setVisibility(View.GONE);
                        lnr_filter.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up));

                        iv_minus_ukuran_filter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(tv_ukuran_filter.getText().toString().equals("-")){
                                    tv_ukuran_filter.setText(String.valueOf(SHOE_SIZE));
                                }else
                                if(Integer.valueOf(tv_ukuran_filter.getText().toString()) > 39){
                                    SHOE_SIZE -= 1;
                                    tv_ukuran_filter.setText(String.valueOf(SHOE_SIZE));
                                }
                            }
                        });

                        iv_plus_ukuran_filter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(tv_ukuran_filter.getText().toString().equals("-")){
                                    tv_ukuran_filter.setText(String.valueOf(SHOE_SIZE));
                                }else
                                if(Integer.valueOf(tv_ukuran_filter.getText().toString()) < 42){
                                    SHOE_SIZE += 1;
                                    tv_ukuran_filter.setText(String.valueOf(SHOE_SIZE));
                                }
                            }
                        });



                        btn_filter_sekarang.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(cb_merah.isChecked()){
                                    WARNA.add("Merah");
                                }
                                if(cb_abu.isChecked()){
                                    WARNA.add("Abu-abu");
                                }
                                if(cb_hitam.isChecked()){
                                    WARNA.add("Hitam");
                                }
                                if(cb_putih.isChecked()){
                                    WARNA.add("Putih");
                                }

                                if(!(tv_ukuran_filter.getText().toString().equals("-"))) {
                                    Iterator<Produk> produk = produkArrayList.iterator();
                                    while(produk.hasNext()){
                                        Produk p = produk.next();
                                        if (p.getUkuran() != Integer.valueOf(tv_ukuran_filter.getText().toString())){
                                            produk.remove();
                                        }
                                    }
                                }

                                if(sekbartouch){
                                    Iterator<Produk> produk = produkArrayList.iterator();
                                    while(produk.hasNext()){
                                        Produk p = produk.next();
                                        if(!(p.getHarga() >= MIN_PRICE && p.getHarga() <= MAX_PRICE)){
                                            produk.remove();
                                        }
                                    }

                                }

                                if(WARNA.size() != 0){
                                    Iterator<Produk> produk = produkArrayList.iterator();
                                    while(produk.hasNext()){
                                        boolean checkwarna = false;
                                        Produk p = produk.next();
                                        for(int j=0; j< WARNA.size(); j++){
                                            if(p.getWarna().equals(WARNA.get(j))){
                                                checkwarna = true;
                                            }
                                        }
                                        if(!checkwarna){
                                            produk.remove();
                                        }

                                    }
                                }

                                adapter.notifyDataSetChanged();

                                lnr_filter.setVisibility(View.GONE);
                                lnr_sort.setVisibility(View.GONE);
                                lnr_sort.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down));
                                lnr_filter.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down));
                            }
                        });

                    }

                    return true;
                case R.id.action_sort:
                    if(lnr_sort.getVisibility() == View.VISIBLE){
                        lnr_sort.setVisibility(View.GONE);
                        lnr_filter.setVisibility(View.GONE);
                        lnr_filter.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down));
                        lnr_sort.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down));
                    }else{
                        lnr_sort.setVisibility(View.VISIBLE);
                        lnr_filter.setVisibility(View.GONE);
                        lnr_sort.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up));

                        btn_sorting_sekarang.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(View view) {
                                if(rg_sorting.getCheckedRadioButtonId() == R.id.sorting_rating) {
                                    Collections.sort(produkArrayList, Produk.SortByRating);
                                    adapter.notifyDataSetChanged();
                                    lnr_sort.setVisibility(View.GONE);
                                    lnr_filter.setVisibility(View.GONE);
                                    lnr_filter.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down));
                                    lnr_sort.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down));
                                }else if(rg_sorting.getCheckedRadioButtonId() == R.id.sorting_terendah) {
                                    Collections.sort(produkArrayList, Produk.SortByHargaTerendah);
                                    adapter.notifyDataSetChanged();
                                    lnr_sort.setVisibility(View.GONE);
                                    lnr_filter.setVisibility(View.GONE);
                                    lnr_filter.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down));
                                    lnr_sort.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down));
                                }else if(rg_sorting.getCheckedRadioButtonId() == R.id.sorting_tertinggi) {
                                    Collections.sort(produkArrayList, Produk.SortByHargaTertinggi);
                                    adapter.notifyDataSetChanged();
                                    lnr_sort.setVisibility(View.GONE);
                                    lnr_filter.setVisibility(View.GONE);
                                    lnr_filter.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down));
                                    lnr_sort.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down));
                                }
                            }
                        });


                    }
                    return true;
            }
            return false;
        }
    };

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
