package com.dnz.local.buxs.MarketPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.dnz.local.buxs.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MarketPlaceActivity extends AppCompatActivity {

    private static final String TAG = "MarketPlaceActivity";
    private final String URL = "http://192.168.43.2:443/getMP/";
    private String imgurl = "http://192.168.43.2:443/img/?path=";
    private ArrayList<Bitmap> thumbnail = new ArrayList<>();
    private ArrayList<String> itemName = new ArrayList<>();
    private ArrayList<Integer> price = new ArrayList<>();
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    RecyclerViewAdapter viewAdapter;

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

        fetchContent();
    }

//    private void initList() {
//        for (int i = 0; i < 10; i++) {
//            this.thumbnail.add(R.drawable.ic_keyboard_black_50dp);
//        }
//
//        for (int i = 0; i < 10; i++) {
//            this.itemName.add("Test: " + i);
//        }
//
//        for (int i = 0; i < 10; i++) {
//            this.price.add((int) (Math.random() * 100));
//        }
//    }

    private void fetchContent() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.trim());

                            Iterator<String> stringIterator = jsonObject.keys();

                            while (stringIterator.hasNext()){
                                JSONObject item = jsonObject.getJSONObject(stringIterator.next());

                                //get all the individual items needed to dispaly in recycler view
                                price.add(item.getInt("price"));
                                itemName.add(item.getString("name"));

                                String url = imgurl+item.getString("image_url");
                                ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        thumbnail.add(response);
                                        viewAdapter.notifyDataSetChanged();
                                    }
                                }, 1024, 1024, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                requestQueue.add(imageRequest);
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: " + e.getMessage());

                        }
                        initRecyclerView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ...called");
        recyclerView = findViewById(R.id.recyclerview_market_place);
        viewAdapter = new RecyclerViewAdapter(this, thumbnail, itemName, price);
        recyclerView.setAdapter(viewAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
}


