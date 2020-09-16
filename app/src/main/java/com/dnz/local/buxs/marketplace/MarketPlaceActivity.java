package com.dnz.local.buxs.marketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
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
import com.dnz.local.buxs.concurrent.GetCart;
import com.dnz.local.buxs.net.MyCookieStore;
import com.dnz.local.buxs.net.URLBuilder;
import com.dnz.local.buxs.utils.MyAnimations;
import com.dnz.local.buxs.utils.MyCache;
import com.dnz.local.buxs.utils.MyDrawerLayout;
import com.dnz.local.buxs.utils.MyIFace;
import com.dnz.local.buxs.utils.ProductDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

public class MarketPlaceActivity extends AppCompatActivity implements MyIFace.IFGetCartCount {

    public static final String TAG = "MarketPlaceActivity";

    public ProductDataStore dataStore = new ProductDataStore();

    public RecyclerView recyclerView;
    public RequestQueue requestQueue;
    public RecyclerViewAdapterMarketPlaceActivity viewAdapter;

    private boolean isAnimationShown = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);

        // Init app Cache
        MyCache.startCache();

        new GetCart(this, this).execute();

        new MyDrawerLayout(this).initDrawerLayout();

        CookieStore cookieStore = MyCookieStore.getInstance(this);
        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        Window window = this.getWindow();

        // set status bar for sdk > lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        MyAnimations.showLoading(this);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network net = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, net);
        requestQueue.start();

        // Initialize recycler view with adapter/Layout
        initRecyclerView();
        initCartIcon();

        // Make http request
        String getDataURL = URLBuilder.buildURL("mplace/gdata");
        CustomJSONResponseListener jsonResponseListener = new CustomJSONResponseListener();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(getDataURL, null, jsonResponseListener, jsonResponseListener);
        requestQueue.add(jsonObjectRequest);

    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview_market_place);
        viewAdapter = new RecyclerViewAdapterMarketPlaceActivity(this);
        recyclerView.setAdapter(viewAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Add scroll listener to detect end
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // check if reach end of recycler view
                if (!recyclerView.canScrollVertically(1)) {
                    // Todo: load new content
                }
            }
        });
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPostExecuteThread(ArrayList<Integer> data) {
        MyCache.writeToCache("cart-data-arraylist", data);
        initCartIcon();
    }


    public void initCartIcon() {
        Object data = MyCache.getFromCache("cart-data-arraylist");
        ArrayList<Integer> cartData;
        if (data != null) {
            cartData = (ArrayList<Integer>) data;
        } else {
            return;
//            throw new IllegalStateException("Invalid Key for cache");
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

    @Override
    protected void onStart() {
        super.onStart();
        initCartIcon();
    }

    public void startCartActivity() {
        Intent i = new Intent(MarketPlaceActivity.this, CartActivity.class);
        startActivity(i);
    }


    // Private classes to handle Response.listener and Response.errorListener
    private class CustomJSONResponseListener implements Response.Listener<JSONObject>, Response.ErrorListener {

        @Override
        public void onResponse(JSONObject response) {
            Iterator<String> stringIterator = response.keys();


            if (!isAnimationShown) {
                MyAnimations.dismissLoading(MarketPlaceActivity.this);
                MyAnimations.showSuccess(MarketPlaceActivity.this);

                Timer timer = new Timer();
                MyIFace.MyTimedClass myTimedClass = new MyIFace.MyTimedClass(MarketPlaceActivity.this, new MyIFace.TaskInterval() {
                    @Override
                    public void runOnUiThread(Object... objects) {
                        MyAnimations.dismissSuccess(MarketPlaceActivity.this);
                    }
                });

                timer.schedule(myTimedClass, 2000);
                isAnimationShown = true;
            }

            // Target position in recycler view
            int position = dataStore.length();
            while (stringIterator.hasNext()) {

                try {
                    JSONObject product = response.getJSONObject(stringIterator.next());

                    int id = product.getInt("id");
                    int price = Integer.parseInt(product.getString("price"));
                    String name = product.getString("name");
                    String imageUrl = product.getString("image_url1");

                    dataStore.insertData(id, name, price, null, position);
                    viewAdapter.notifyItemInserted(position);

                    String finalImageUrl = URLBuilder.buildURL("mplace/img", "path=" + imageUrl);
                    // Make the http request for the image
                    CustomImageResponseListener responseListener = new CustomImageResponseListener(position);
                    ImageRequest imageRequest = new ImageRequest(finalImageUrl, responseListener,
                            1024, 1024, null, responseListener);

                    requestQueue.add(imageRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                position += 1;
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            MyAnimations.dismissLoading(MarketPlaceActivity.this);
            try {
                MyAnimations.showError(MarketPlaceActivity.this, error.getCause().getClass()
                        .getCanonicalName());

            } catch (NullPointerException e) {
                MyAnimations.showError(MarketPlaceActivity.this, "Failed to connect" );
            }

            Log.e(TAG, "onErrorResponse: error", error);
        }
    }

    private class CustomImageResponseListener implements Response.ErrorListener, Response.Listener<Bitmap> {

        private int imagePosition;

        public CustomImageResponseListener(int imagePosition) {
            this.imagePosition = imagePosition;
        }

        @Override
        public void onResponse(Bitmap response) {
            dataStore.setBitmap(imagePosition, response);
            viewAdapter.notifyItemChanged(imagePosition);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_load_error);
            dataStore.setBitmap(this.imagePosition, bitmap);
            viewAdapter.notifyItemChanged(imagePosition);
        }
    }
}


