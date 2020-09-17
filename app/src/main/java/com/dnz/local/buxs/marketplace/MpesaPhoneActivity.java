package com.dnz.local.buxs.marketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.dnz.local.buxs.R;
import com.dnz.local.buxs.net.URLBuilder;
import com.dnz.local.buxs.utils.Currency;
import com.dnz.local.buxs.utils.MyAnimations;

import java.util.HashMap;
import java.util.Map;

public class MpesaPhoneActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String> {

    private RequestQueue requestQueue;
    private String price;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa_phone);

        Window window = this.getWindow();

        // set status bar for sdk > lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network net = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, net, 1);
        requestQueue.start();

        price = getIntent().getExtras().getString("Price");

        ((TextView) findViewById(R.id.price)).setText(Currency.getShilling(price));
        editText = findViewById(R.id.phone_number);
    }

    private void sendPost(final String phone, final String amount) {
        String url = URLBuilder.buildURL("mpesa_pay/lipa_na_mpesa");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone_number", phone);
                params.put("amount", amount);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void pay(View v){
        String phoneNumber = editText.getText().toString().trim();
        sendPost(phoneNumber, price);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MpesaPhoneActivity.this, "Error", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onResponse(String response) {
        Toast.makeText(MpesaPhoneActivity.this, "Success", Toast.LENGTH_LONG).show();
        finish();
    }
}