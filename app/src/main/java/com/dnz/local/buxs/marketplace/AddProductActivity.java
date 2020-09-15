package com.dnz.local.buxs.marketplace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.dnz.local.buxs.R;
import com.dnz.local.buxs.marketplace.fragments.CameraCaptureFragment;
import com.dnz.local.buxs.marketplace.fragments.DescriptionFragment;
import com.dnz.local.buxs.marketplace.fragments.PreviewFragment;
import com.dnz.local.buxs.net.URLBuilder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = "AddProductActivity";
    public int responseCode;

    public CameraCaptureFragment cameraFragment;
    public PreviewFragment previewFragment;
    public DescriptionFragment descriptionFragment;
    public String productName = "NAME:",
            productPrice = "0",
            productBrand = "Brand: ",
            productDescription = "";

    public Bitmap[] productImages = new Bitmap[3];
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Window window = this.getWindow();

        // set status bar for sdk > lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        cameraFragment = new CameraCaptureFragment(AddProductActivity.this);
        previewFragment = new PreviewFragment(AddProductActivity.this);
        descriptionFragment = new DescriptionFragment(AddProductActivity.this);

        if (savedInstanceState == null) {
            ft.add(R.id.fragment_container, cameraFragment).commit();
        }

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network net = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, net);
        requestQueue.start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.e(TAG, "onActivityResult: error invalid result code", null);
            return;
        }

        Bitmap image = (Bitmap) data.getExtras().get("data");

        switch (requestCode) {
            case CameraCaptureFragment.IMAGE_ONE:
                productImages[0] = image;
                cameraFragment.setImage(image);
                cameraFragment.setImageToCheck(1);
                break;
            case CameraCaptureFragment.IMAGE_TWO:
                productImages[1] = image;
                cameraFragment.setImage(image);
                cameraFragment.setImageToCheck(2);
                break;
            case CameraCaptureFragment.IMAGE_THREE:
                productImages[2] = image;
                cameraFragment.setImage(image);
                cameraFragment.setImageToCheck(3);
                break;
        }
    }

    public void sendPost() {

        String url = URLBuilder.buildURL("mplace/addproduct");
        ByteArrayOutputStream outputStream[] = new ByteArrayOutputStream[3];
        int i = 0;
        for (Bitmap x : productImages) {
            x.compress(Bitmap.CompressFormat.JPEG, 100, outputStream[i]);
            i++;
        }

        final String image_1 = Base64.encodeToString(outputStream[0].toByteArray(), Base64.DEFAULT);
        final String image_2 = Base64.encodeToString(outputStream[1].toByteArray(), Base64.DEFAULT);
        final String image_3 = Base64.encodeToString(outputStream[2].toByteArray(), Base64.DEFAULT);

        CustomListener customListener = new CustomListener();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, customListener, customListener) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = super.getParams();
                params.put("name", productName);
                params.put("price", productPrice);
                params.put("desc", productDescription);
                params.put("brand", productBrand);
                params.put("category", "Generic");
                params.put("stock", "100");
                params.put("img1", image_1);
                params.put("img2", image_2);
                params.put("img3", image_3);

                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                responseCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        requestQueue.add(stringRequest);
    }


    public class CustomListener implements Response.Listener<String>, Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(AddProductActivity.this, "Error", Toast.LENGTH_LONG).show();
        }


        @Override
        public void onResponse(String response) {
            if (responseCode != 200) {
                Toast.makeText(AddProductActivity.this, "Error", Toast.LENGTH_LONG).show();
                return;
            }
            finish();
        }
    }

}
