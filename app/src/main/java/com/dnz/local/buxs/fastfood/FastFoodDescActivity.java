package com.dnz.local.buxs.fastfood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

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
import com.dnz.local.buxs.R;
import com.dnz.local.buxs.concurrent.WriteToCart;
import com.dnz.local.buxs.marketplace.CartActivity;
import com.dnz.local.buxs.net.MyCookieStore;
import com.dnz.local.buxs.net.URLBuilder;
import com.dnz.local.buxs.utils.Currency;
import com.dnz.local.buxs.utils.MyAnimations;
import com.dnz.local.buxs.utils.MyCache;
import com.dnz.local.buxs.utils.MyDrawerLayout;
import com.dnz.local.buxs.utils.MyIFace;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class FastFoodDescActivity extends AppCompatActivity {

    private static final String TAG = "MarketPlaceDescActivity";

    private ViewPager viewPager;
    public ArrayList<Bitmap> bitmaps = new ArrayList<>();
    public View[] selectorViews = new View[3];
    private ViewPagerAdapterFastFoodDesc pagerAdapter;
    public TextView productDesc, productPrice, productName, productBrand;
    private RequestQueue requestQueue;
    private String[] viewPagerImages = new String[3];
    public TextView cartCount;
    public int productID;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_desc);

        CookieStore cookieStore = MyCookieStore.getInstance(this);
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

        MyAnimations.showLoading(this);
        viewPager = findViewById(R.id.view_pager);

        pagerAdapter = new ViewPagerAdapterFastFoodDesc(this);
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
        String productId = getIntent().getStringExtra("PRODUCT_ID");
        fetchData(productId);
        initCartIcon();
    }

    private void fetchData(String productId) {
        String productUrl = URLBuilder.buildURL("mplace/gdesc", "pid=" + productId);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, productUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyAnimations.dismissLoading(FastFoodDescActivity.this);
                        MyAnimations.showSuccess(FastFoodDescActivity.this);

                        Timer timer = new Timer();
                        MyIFace.MyTimedClass myTimedClass = new MyIFace.MyTimedClass(FastFoodDescActivity.this, new MyIFace.TaskInterval() {
                            @Override
                            public void runOnUiThread(Object... objects) {
                                MyAnimations.dismissSuccess(FastFoodDescActivity.this);
                            }
                        });

                        timer.schedule(myTimedClass, 2000);
                        findViewById(R.id.data_container_market_place_desc_activity).setAlpha(1f);
                        try {
                            productID = response.getInt("id");
                            productPrice.setText(Currency.getShilling(response.getString("price")));
                            productDesc.setText(response.getString("description"));
                            productBrand.setText(String.format("Brand : %s", response.getString("brand")));
                            productName.setText(response.getString("name"));

                            viewPagerImages[0] = response.getString("image_url1");
                            viewPagerImages[1] = response.getString("image_url2");
                            viewPagerImages[2] = response.getString("image_url3");
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException " + e.getMessage(), e);
                        }
                        for (String x : viewPagerImages) {
                            String imgUrl = URLBuilder.buildURL("mplace/img", "path=" + x);

                            ImageRequest imageRequest1 = new ImageRequest(imgUrl,
                                    new Response.Listener<Bitmap>() {

                                        @Override
                                        public void onResponse(Bitmap response) {
                                            bitmaps.add(response);
                                            pagerAdapter.notifyDataSetChanged();
                                        }
                                    }, 1024, 1024, null,
                                    new Response.ErrorListener() {
                                        @Override

                                        public void onErrorResponse(VolleyError error) {
                                            // Todo: Show image error thumbnail
                                            Log.e(TAG, "onErrorResponse: " + error.getMessage(), error);
                                        }
                                    });

                            requestQueue.add(imageRequest1);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyAnimations.dismissLoading(FastFoodDescActivity.this);
                        MyAnimations.showError(FastFoodDescActivity.this, "Failed to connect.");
                    }
                });
        this.requestQueue.add(objectRequest);

    }

    // To change UI Displaying the items already in cart on toolbar
    public void initCartIcon() {
        Object data = MyCache.getFromCache("cart-data-arraylist");
        ArrayList<Integer> cartData;
        if (data instanceof ArrayList) {
            cartData = (ArrayList<Integer>) data;
        } else {
            throw new IllegalStateException("Invalid Key for cache");
        }
        int count = cartData.size();

        TextView cartCount = findViewById(R.id.cart_amount);
        cartCount.setText(String.valueOf(count));

        if (count == 0) {
            cartCount.setVisibility(View.GONE);
        } else {
            cartCount.setVisibility(View.VISIBLE);
        }

    }

    //To change UI after item is added to cart
    public void addToCart(View view) {
        Object data = MyCache.getFromCache("cart-data-arraylist");
        ArrayList<Integer> cartData;
        TextView cartCount = findViewById(R.id.cart_amount);

        if (data instanceof ArrayList) {
            cartData = (ArrayList<Integer>) data;
        } else {
            throw new IllegalStateException("Invalid Key for cache");
        }


        if (!cartData.contains(productID)) {
            cartData.add(productID);
        } else {
            // Todo: Show error message "Item already exists in cart"
            Toast.makeText(this, "Item Already exists in cart", Toast.LENGTH_SHORT).show();
        }


        int count = cartData.size();
        cartCount.setText(String.valueOf(count));
        if (count <= 0) {
            cartCount.setVisibility(View.INVISIBLE);
        } else {
            cartCount.setVisibility(View.VISIBLE);
        }


        MyCache.writeToCache("cart-data-arraylist", cartData);
        MyCache.saveData("cart-data-arraylist", new WriteToCart(this));

    }

    // Timer to handle auto scroll if images
    public class MyTimer extends TimerTask {

        @Override
        public void run() {
            FastFoodDescActivity.this.runOnUiThread(new Runnable() {
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


    @Override
    protected void onStart() {
        super.onStart();
        initCartIcon();
    }

    public void startCartActivity() {
        Intent i = new Intent(FastFoodDescActivity.this, CartActivity.class);
        startActivity(i);
    }

    public void openCart(View v){
        startCartActivity();
    }

}
