package com.example.bahroel.adidasshoeshop.Fragment;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KatalogFragment extends Fragment {
    RecyclerView katalog;
    int last_page;
    int page = 2;
    ArrayList<Produk> produkArrayList= new ArrayList<>();
    LinearLayout lnr_filter;
    boolean userScrolled = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static RelativeLayout bottomLayout;
    private KatalogAdapter adapter;
    private static LinearLayoutManager layoutManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View viewFrag1 = inflater.inflate(R.layout.katalog_fragment, container, false);

        BottomNavigationView navigation = (BottomNavigationView) viewFrag1.findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.action_home).setChecked(true);
        final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        bottomLayout = (RelativeLayout) viewFrag1.findViewById(R.id.loadItemsLayout_recyclerView);

        katalog = (RecyclerView)viewFrag1.findViewById(R.id.rv_katalog);
        lnr_filter = viewFrag1.findViewById(R.id.lnr_filter);

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
                layoutManager = new GridLayoutManager(getActivity(), 2);
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
                ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
                Call<ProdukResponse> call = request.getProdukJSON(page);
                call.enqueue(new Callback<ProdukResponse>() {
                    @Override
                    public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                        ProdukResponse jsonresponse = response.body();
                        int the_last = jsonresponse.getCurrentPage();
                        if(jsonresponse.getProduks() == null || the_last == last_page){
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
                        lnr_filter.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down));
                    }else{
                        lnr_filter.setVisibility(View.VISIBLE);
                        lnr_filter.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up));
                    }

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
