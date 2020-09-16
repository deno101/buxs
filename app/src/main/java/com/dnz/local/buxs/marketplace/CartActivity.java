package com.dnz.local.buxs.marketplace;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import com.dnz.local.buxs.concurrent.WriteToCart;
import com.dnz.local.buxs.net.MyCookieStore;
import com.dnz.local.buxs.net.URLBuilder;
import com.dnz.local.buxs.utils.MyAnimations;
import com.dnz.local.buxs.utils.MyCache;
import com.dnz.local.buxs.utils.MyIFace;
import com.dnz.local.buxs.utils.ProductDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;


public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CartActivity";

    private RequestQueue requestQueue;
    public CartProductDataStore productDataStore = new CartProductDataStore();
    private RecyclerViewAdapterCartActivity adapterCartActivity = new RecyclerViewAdapterCartActivity(this);
    private final String activityTitle = "Cart";

    private boolean canEnlargeTotal = false;
    private boolean canShrinkTotal = true;
    private boolean isAnimationShown = false;
    private ArrayList<Integer> productIDs;

    private String URL = URLBuilder.buildURL("mplace/cart");

    public RecyclerView recyclerView;
    private RelativeLayout animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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

        requestQueue = new RequestQueue(cache, net, 1);
        requestQueue.start();

        ((TextView) findViewById(R.id.title_toolbar_no_drawer)).setText(activityTitle);

        setListeners();
        initRecyclerView();

        Object temp = MyCache.getFromCache("cart-data-arraylist");
        if (temp instanceof ArrayList) {
            productIDs = (ArrayList<Integer>) temp;
        }
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
        recyclerView = findViewById(R.id.recyclerview_cart_activity);
        animationView = findViewById(R.id.cart_total_container);

        recyclerView.setAdapter(adapterCartActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bindRecyclerViewWithScrollListener_API_GT_23(recyclerView, animationView);
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
                    new CustomJSONResponseListener(itemCount, x) {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (!isAnimationShown) {
                                MyAnimations.dismissLoading(CartActivity.this);
                                MyAnimations.showSuccess(CartActivity.this);

                                Timer timer = new Timer();
                                MyIFace.MyTimedClass myTimedClass = new MyIFace.MyTimedClass(CartActivity.this, new MyIFace.TaskInterval() {
                                    @Override
                                    public void runOnUiThread(Object... objects) {
                                        MyAnimations.dismissSuccess(CartActivity.this);
                                    }
                                });

                                timer.schedule(myTimedClass, 2000);
                                isAnimationShown = true;
                            }

                            try {
                                int id = response.getInt("id");
                                String name = response.getString("name");
                                int price = Integer.parseInt(response.getString("price"));
                                String imagePath = response.getString("image_url1");

                                productDataStore.insertData(id, name, price, null, this.productPosition);
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

                                MyCache.destroyData("cart-data-arraylist");
                                finish();
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onErrorResponse: JsonObjectRequest", error);
                            MyAnimations.dismissLoading(CartActivity.this);
                            try {

                                MyAnimations.showError(CartActivity.this, error.getCause().getClass().getCanonicalName());
                            } catch (NullPointerException e) {
                                MyAnimations.showError(CartActivity.this, "Failed to connect");

                            }
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
                // Todo: change animation dy direction

                if (!recyclerView.canScrollVertically(1)) {
                    animationView.animate().translationY(0).setDuration(200).alpha(1.0f).setListener(
                            new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    recyclerView.setPadding(0, 0, 0, animationView.getHeight());
                                }

                            });
                    canShrinkTotal = false;
                    canEnlargeTotal = true;
                }
                int dy = scrollY - oldScrollY;
                if (dy > 0) {
                    // move upwards
                    if (canShrinkTotal) {
                        animationView.animate().translationY(animationView.getHeight()).setDuration(400).alpha(0.0f).setListener(
                                new AnimatorListenerAdapter() {

                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationStart(animation);
                                        recyclerView.setPadding(0, 0, 0, 0);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                    }

                                }
                        );
                        canShrinkTotal = false;
                        canEnlargeTotal = true;
                    }
                } else {
                    // move Downwards
                    if (canEnlargeTotal) {
                        animationView.animate().translationY(0).setDuration(400).alpha(1.0f).setListener(
                                new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationStart(animation);
                                        recyclerView.setPadding(0, 0, 0, animationView.getHeight());
                                    }

                                }

                        );
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
        public Integer productId;

        private CustomJSONResponseListener(int position, Integer productId) {
            this.productPosition = position;
            this.productId = productId;
        }
    }

    private abstract class CustomImageResponseListener implements Response.Listener<Bitmap> {
        public int productPosition;

        private CustomImageResponseListener(int productPosition) {
            this.productPosition = productPosition;
        }
    }

    protected class CartProductDataStore extends ProductDataStore {
        private Map<Integer, Integer> productCount = new HashMap<>();
        private ArrayList<Integer> keys = new ArrayList<>();

        @Override
        public synchronized void insertData(int id, String productName, int productPrice, Bitmap productImage, int position) {
            super.insertData(id, productName, productPrice, productImage, position);
            productCount.put(position, 1);
            keys.add(position);
        }

        @Override
        public void removeItem(int position) {
            int id = getProductID(position);
            removeDataFromCart(id);

            Integer mPosition = getKey(position);
            super.removeItem(mPosition);
            productCount.remove(mPosition);
            keys.remove(position);

        }

        public void setProductCount(int position, int count) {
            int mPosition = getKey(position);
            productCount.put(mPosition, count);
        }

        public float getPrice() {
            Set<Integer> keys = productCount.keySet();
            float totalPrice = 0F;
            for (Integer integer : keys) {
                totalPrice += (super.getProductPriceInt(integer) * productCount.get(integer));
            }

            return totalPrice;
        }

        private void removeDataFromCart(int value) {
            Object rawData = MyCache.getFromCache("cart-data-arraylist");

            ArrayList<Integer> temp = new ArrayList<>();
            ArrayList<Integer> cartData = (ArrayList<Integer>) rawData;

            for (int x : cartData) {
                if (x == value) continue;
                temp.add(x);
            }

            MyCache.writeToCache("cart-data-arraylist", temp);
            MyCache.saveData("cart-data-arraylist", new WriteToCart(CartActivity.this));
        }

        private Integer getKey(int position) {
            return keys.get(position);
        }

        @Override
        public Integer getProductID(int position) {
            Integer mPosition = getKey(position);
            return super.getProductID(mPosition);
        }

        @Override
        public String getProductName(int position) {
            Integer mPosition = getKey(position);
            return super.getProductName(mPosition);
        }

        @Override
        public String getProductPrice(int position) {
            Integer mPosition = getKey(position);
            return super.getProductPrice(mPosition);
        }

        @Override
        public int getProductPriceInt(int position) {
            Integer mPosition = getKey(position);
            return super.getProductPriceInt(mPosition);
        }

        @Override
        public Bitmap getProductImage(int position) {
            Integer mPosition = getKey(position);
            return super.getProductImage(mPosition);
        }
    }
}
