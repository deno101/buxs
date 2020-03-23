package com.dnz.local.buxs.MarketPlace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.dnz.local.buxs.MainActivity;
import com.dnz.local.buxs.R;
import com.dnz.local.buxs.net.MyCookieStore;
import com.dnz.local.buxs.utils.Currency;
import com.dnz.local.buxs.utils.MyDrawerLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MarketPlaceDescActivity extends AppCompatActivity {

    private static final String TAG = "MarketPlaceDescActivity";

    private ViewPager viewPager;
    public ArrayList<Bitmap> bitmaps = new ArrayList<>();
    public View[] selectorViews = new View[3];
    Map<String, String> stringMap = new HashMap<>();
    private String url = "http://165.22.222.126:443/mplace/gdesc?pid=";
    private ViewPagerAdapter pagerAdapter;
    public TextView productDesc, productPrice, productName, productBrand;
    public String imgurl = "http://165.22.222.126:443/mplace/img/?path=";

    private RequestQueue requestQueue;

    private String img1, img2, img3;
    private MyCookieStore cookieStore;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_desc);

        cookieStore = MainActivity.getCookieStore();
        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        new MyDrawerLayout(this).initDrawerLayout();

        selectorViews[0] = findViewById(R.id.item_1);
        selectorViews[1] = findViewById(R.id.item_2);
        selectorViews[2] = findViewById(R.id.item_3);

        // Change color of status bar
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        viewPager = findViewById(R.id.view_pager);

        pagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Reset all dots to unselected
                for (View x : selectorViews) {
                    x.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.image_slider_bg));
                }

                //Set page selected  to onselect bg
                selectorViews[position].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.image_slider_bg_onselect));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        selectorViews[0].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.image_slider_bg_onselect));

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimer(), 5000, 8000);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network net = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, net, 1);

        //init vars
        productPrice = findViewById(R.id.product_price);
        productBrand = findViewById(R.id.product_brand);
        productDesc = findViewById(R.id.product_description);
        productName = findViewById(R.id.product_name);

        requestQueue.start();
        fetchData();
    }

    private void fetchData() {
        String img = getIntent().getStringExtra("PRODUCT_ID");
        url += img;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            productPrice.setText(Currency.getShilling(response.getString("price")));
                            productDesc.setText(response.getString("description"));
                            productBrand.setText("Brand: "+response.getString("brand"));
                            productName.setText(response.getString("name"));

                            img1 = response.getString("image_url1");
                            img2 = response.getString("image_url2");
                            img3 = response.getString("image_url3");
                        } catch (JSONException e) {
                            Log.d(TAG, "JSONexception " + e.getMessage());
                        }
                        ImageRequest imageRequest = new ImageRequest(imgurl + img1,
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        Log.d(TAG, "onResponse: got image 0");
                                        bitmaps.add(response);
                                        pagerAdapter.notifyDataSetChanged();

                                    }
                                }, 1024, 1024, null,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "onErrorResponse: "+error.toString());
                                    }
                                });

                        requestQueue.add(imageRequest);

                        ImageRequest imageRequest1 = new ImageRequest(imgurl + img2,
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        Log.d(TAG, "onResponse: got image 1");
                                        bitmaps.add(response);
                                        pagerAdapter.notifyDataSetChanged();
                                    }
                                }, 1024, 1024, null,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "onErrorResponse: "+error.getMessage());
                                    }
                                });

                        requestQueue.add(imageRequest1);
                        ImageRequest imageRequest2 = new ImageRequest(imgurl + img3,
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        bitmaps.add(response);
                                        Log.d(TAG, "onResponse: got image 2");
                                        pagerAdapter.notifyDataSetChanged();
                                    }
                                }, 1024, 1024, null,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "onErrorResponse: "+error.getMessage());
                                    }
                                });

                        requestQueue.add(imageRequest2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        this.requestQueue.add(objectRequest);

    }

    // Timer to handle auto scroll if images
    public class MyTimer extends TimerTask {

        @Override
        public void run() {
            MarketPlaceDescActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

}
