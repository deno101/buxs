package com.dnz.local.buxs.MarketPlace;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.dnz.local.buxs.LoginActivity;
import com.dnz.local.buxs.MainActivity;
import com.dnz.local.buxs.R;
import com.dnz.local.buxs.net.MyCookieStore;
import com.dnz.local.buxs.net.PersistentCookieStore;
import com.dnz.local.buxs.net.StrRequestGetMP;
import com.dnz.local.buxs.utils.MyDrawerLayout;
import com.google.android.material.navigation.NavigationView;


import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MarketPlaceActivity extends AppCompatActivity {

    ImageView user,dotsVert;
    TextView username;

    public static final String TAG = "MarketPlaceActivity";
    public final String URL = "http://165.22.222.126:443/mplace/gdata/";
    public String imgurl = "http://165.22.222.126:443/mplace/img/?path=";
    public ArrayList<Bitmap> thumbnail = new ArrayList<>();
    public ArrayList<String> itemName = new ArrayList<>();
    public ArrayList<Integer> price = new ArrayList<>();
    public ArrayList<Integer> id = new ArrayList<>();

    public RecyclerView recyclerView;
    public RequestQueue requestQueue;
    public ProgressDialog progressDialog = null;
    public RecyclerViewAdapter viewAdapter;
    private StrRequestGetMP strRequestGetMP = new StrRequestGetMP(this);
    private MyCookieStore cookieStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);

        new MyDrawerLayout(this).initDrawerLayout();

        cookieStore = MainActivity.getCookieStore();
        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

//        // check if a 'username' cookie exists the show username
//        if (cookieStore.getAuthenticator().isAuthenticated()) {
//            username.setText(cookieStore.getAuthenticator().getUsername());
//            username.setVisibility(View.VISIBLE);
//
//            user.setImageResource(R.drawable.ic_person_authenticated);
//            user.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //Todo: show account information
//
//                }
//            });
//        }else {
//            user.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(MarketPlaceActivity.this, LoginActivity.class));
//                }
//            });
//        }
        Window window = this.getWindow();

        // set status bar for sdk > lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network net = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, net,1);
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
        recyclerView = findViewById(R.id.recyclerview_market_place);
        viewAdapter = new RecyclerViewAdapter(this, thumbnail, itemName, price, id);
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
                    strRequestGetMP.makeRequest(10);
                }
            }
        });
    }

    public void makeToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}


