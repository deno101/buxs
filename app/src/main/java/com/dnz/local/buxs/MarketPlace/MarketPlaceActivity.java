package com.dnz.local.buxs.MarketPlace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.dnz.local.buxs.R;
import com.dnz.local.buxs.net.StrRequestGetMP;


import java.util.ArrayList;

public class MarketPlaceActivity extends AppCompatActivity {

    public static final String TAG = "MarketPlaceActivity";
    public final String URL = "http://165.22.222.126:443/getMP/";
    public String imgurl = "http://165.22.222.126:443/img/?path=";
    public ArrayList<Bitmap> thumbnail = new ArrayList<>();
    public ArrayList<String> itemName = new ArrayList<>();
    public ArrayList<Integer> price = new ArrayList<>();
    public RecyclerView recyclerView;
    public RequestQueue requestQueue;
    public ProgressDialog progressDialog = null;
    public RecyclerViewAdapter viewAdapter;
    private StrRequestGetMP strRequestGetMP = new StrRequestGetMP(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);

        Window window = this.getWindow();

        // set status bar for sdk > lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network net = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, net);
        requestQueue.start();

        // Create progress bar showing content loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();

        // Initialize recycler view with adapter/Layout
        initRecyclerView();
        // Make http request with length 10
        strRequestGetMP.makeRequest(10);
    }

    public void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ...called");
        recyclerView = findViewById(R.id.recyclerview_market_place);
        viewAdapter = new RecyclerViewAdapter(this, thumbnail, itemName, price);
        recyclerView.setAdapter(viewAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Add scroll listener to detect end
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // check if reach end of recycler view
                if(!recyclerView.canScrollVertically(1)){
                    // TODO: make new get requests to server
                    strRequestGetMP.makeRequest(10);
                }
            }
        });
    }

    public void makeToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}


