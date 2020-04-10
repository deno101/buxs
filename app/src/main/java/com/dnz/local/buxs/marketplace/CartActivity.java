package com.dnz.local.buxs.marketplace;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
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
import com.dnz.local.buxs.net.URLBuilder;
import com.dnz.local.buxs.utils.ProductDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.ArrayList;


public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CartActivity";

    private RequestQueue requestQueue;
    public ProductDataStore productDataStore = new ProductDataStore();
    private RecyclerViewAdapterCartActivity adapterCartActivity = new RecyclerViewAdapterCartActivity(this);
    private final String activityTitle = "Cart";

    private boolean canEnlargeTotal = false;
    private boolean canShrinkTotal = true;

    private String URL = URLBuilder.buildURL("mplace/cart");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        CookieStore cookieStore = MainActivity.getCookieStore();
        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        Window window = this.getWindow();

        // set status bar for sdk > lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network net = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, net, 1);
        requestQueue.start();
        CardView cardView = new CardView(this);

        ((TextView) findViewById(R.id.title_toolbar_no_drawer)).setText(activityTitle);

        setListeners();
        initRecyclerView();

        ArrayList<Integer> productIDs = getIntent().getIntegerArrayListExtra("ids");
        makeNetworkRequests(productIDs);
    }

    // Execute when item is clicked
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_toolbar_no_drawer:
                finish();
                break;
            case R.id.more_toolbar_no_drawer:
                Toast.makeText(this, "Clicked on more", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // Link view components with this instance of View.OnClickListeners
    private void setListeners() {
        findViewById(R.id.back_toolbar_no_drawer).setOnClickListener(this);
        findViewById(R.id.more_toolbar_no_drawer).setOnClickListener(this);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview_cart_activity);
        RelativeLayout animationView = findViewById(R.id.cart_total_container);

        recyclerView.setAdapter(adapterCartActivity);
        Log.d(TAG, "initRecyclerView: " + animationView.getMeasuredHeightAndState());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bindRecyclerViewWithScrollListener_API_GT_23(recyclerView, animationView);
        } else {
            bindRecyclerViewWithScrollListener_API_LT_23(recyclerView, animationView);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void makeNetworkRequests(ArrayList<Integer> itemsInCart) {
        int itemCount = 0;
        for (int x : itemsInCart) {
            String productID = String.valueOf(x);
            String finalUrl = URLBuilder.appendQuery(URL, "pid=" + productID);

            // create request to get the data associated with a certain id
            JsonObjectRequest dataRequest = new JsonObjectRequest(finalUrl, null,
                    new CustomJSONResponseListener(itemCount) {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String name = response.getString("name");
                                int price = Integer.parseInt(response.getString("price"));
                                String imagePath = response.getString("image_url1");

                                productDataStore.insertData(name, price, null, this.productPosition);
                                adapterCartActivity.notifyItemInserted(this.productPosition);
                                String fullImageUrl = URLBuilder.buildURL("mplace/img", "path=" + imagePath);

                                // Make request associated with the json data
                                ImageRequest imageRequest = new ImageRequest(fullImageUrl,
                                        new CustomImageResponseListener(this.productPosition) {

                                            @Override
                                            public void onResponse(Bitmap response) {
                                                productDataStore.setBitmap(this.productPosition, response);
                                                adapterCartActivity.notifyItemChanged(this.productPosition);
                                            }
                                        }, 1024, 1024, null,
                                        new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e(TAG, "onErrorResponse: ImageRequest", error);
                                            }
                                        });
                                requestQueue.add(imageRequest);

                            } catch (JSONException e) {
                                Log.e(TAG, "onResponse: JSONException", e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onErrorResponse: JsonObjectRequest", error);
                        }
                    });
            requestQueue.add(dataRequest);
            itemCount += 1;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void bindRecyclerViewWithScrollListener_API_GT_23(final RecyclerView recyclerView, final RelativeLayout animationView) {
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {

            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int dy = scrollY - oldScrollY;
                if (dy > 0) {
                    // move upwards
                    if (canShrinkTotal) {
                        animationView.animate().translationY(-animationView.getHeight()).setDuration(300).alpha(0.0f).setListener(
                                new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        animationView.setVisibility(View.GONE);
                                        recyclerView.setElevation(10);
                                        animationView.setElevation(0);
                                    }

                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationStart(animation);
                                        recyclerView.setPadding(0, 0, 0, 0);
                                    }
                                }
                        );
                        canShrinkTotal = false;
                        canEnlargeTotal = true;
                        Log.d(TAG, "onScrollChange: shrinking height" + animationView.getMeasuredHeight());
                    }
                } else {
                    // move Downwards
                    if (canEnlargeTotal) {
                        animationView.animate().translationY(0).setDuration(300).alpha(1.0f).setListener(
                                new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationStart(animation);
                                        animationView.setVisibility(View.VISIBLE);
                                        recyclerView.setElevation(0);
                                        animationView.setElevation(10);
                                        recyclerView.setPadding(0, animationView.getHeight(), 0, 0);
                                    }

                                }

                        );
                        canShrinkTotal = true;
                        canEnlargeTotal = false;
                        Log.d(TAG, "onScrollChange: enlarging");
                    }
                }
            }
        });
    }

    private void bindRecyclerViewWithScrollListener_API_LT_23(RecyclerView recyclerView, final RelativeLayout animationView) {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // move upwards
                    if (canShrinkTotal) {
                        animationView.setAnimation(AnimationUtils.loadAnimation(CartActivity.this, R.anim.shrink_animation));
                        canShrinkTotal = false;
                        canEnlargeTotal = true;

                    }
                } else {
                    // move downwards
                    if (canEnlargeTotal) {
                        animationView.setAnimation(AnimationUtils.loadAnimation(CartActivity.this, R.anim.enlarge_animation));
                        canShrinkTotal = true;
                        canEnlargeTotal = false;
                    }
                }
            }

        });

    }

    // Classes  to help in sorting response data
    private abstract class CustomJSONResponseListener implements Response.Listener<JSONObject> {
        public int productPosition;

        private CustomJSONResponseListener(int position) {
            this.productPosition = position;
        }
    }

    private abstract class CustomImageResponseListener implements Response.Listener<Bitmap> {
        public int productPosition;

        private CustomImageResponseListener(int productPosition) {
            this.productPosition = productPosition;
        }
    }
}
