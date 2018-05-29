package com.example.bahroel.adidasshoeshop.Fragment;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.example.bahroel.adidasshoeshop.Adapter.KatalogAdapter;
import com.example.bahroel.adidasshoeshop.Adapter.RilisAdapter;
import com.example.bahroel.adidasshoeshop.Api.ApiInterface;
import com.example.bahroel.adidasshoeshop.Api.ApiRequest;
import com.example.bahroel.adidasshoeshop.Behavior.BottomNavigationBehavior;
import com.example.bahroel.adidasshoeshop.MainActivity;
import com.example.bahroel.adidasshoeshop.Model.Produk;
import com.example.bahroel.adidasshoeshop.R;
import com.example.bahroel.adidasshoeshop.Response.ProdukResponse;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KatalogFragment extends Fragment {
    RecyclerView katalog;
    ArrayList<Produk> produkArrayList= new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View viewFrag1 = inflater.inflate(R.layout.katalog_fragment, container, false);

        BottomNavigationView navigation = (BottomNavigationView) viewFrag1.findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.action_home).setChecked(true);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        katalog = (RecyclerView)viewFrag1.findViewById(R.id.rv_katalog);
        ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
        Call<ProdukResponse> call = request.getProdukJSON();
        call.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                ProdukResponse jsonresponse = response.body();
                produkArrayList = new ArrayList<>(Arrays.asList(jsonresponse.getProduks()));
                Log.d(MainActivity.class.getSimpleName(),"nilai produk : " + produkArrayList.get(0).getNama() );

                katalog.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
                katalog.setLayoutManager(layoutManager);
                katalog.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
                katalog.setItemAnimator(new DefaultItemAnimator());
                KatalogAdapter adapter = new KatalogAdapter(getActivity(), produkArrayList);
                katalog.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {

            }
        });

        return viewFrag1;
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

                    return true;
                case R.id.action_sort:

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
