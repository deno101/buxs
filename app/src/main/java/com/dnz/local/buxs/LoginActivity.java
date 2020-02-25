package com.dnz.local.buxs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

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

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private final String LOG_TAG = LoginActivity.class.getSimpleName();
    private final String URL = "http://192.168.43.2:443/login/";
    private EditText usernameField;
    private EditText passwordField;

    //    request Queue for http connections
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Change color of status bar
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // Initialize view components
        passwordField = findViewById(R.id.password);
        usernameField = findViewById(R.id.username);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network net = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, net);
        requestQueue.start();
    }

    public void goBack(View view) {
        finish();
    }

    public void testNet(View view) {

    }

    public void login(View view) {
        // Get Values of EditText fields
        final String password = passwordField.getText().toString();
        final String username = usernameField.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(LOG_TAG, response);

                        //TODO :SET THE USER TO BE LOGGED IN, DESTROY ACTIVITY TO PREVIOUS IN STACK


                        passwordField.setText("");
                        usernameField.setText("");
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, "onErrorResponse: ");
                        // TODO: HANDLE NO NET CONNECTION ERROR, INVALID REQUEST

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        this.requestQueue.add(request);
    }
}

