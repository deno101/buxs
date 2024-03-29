package com.dnz.local.buxs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import com.dnz.local.buxs.net.MyCookieStore;
import com.dnz.local.buxs.net.URLBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private final String LOG_TAG = LoginActivity.class.getSimpleName();
    private static final String TAG = "LoginActivity";
    private final String URL = URLBuilder.buildURL("login");
    private EditText usernameField;
    private EditText passwordField;
    public LoginActivity context;

    //    request Queue for http connections
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        // Change color of status bar
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        CookieStore cookieStore = MyCookieStore.getInstance(this);
        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

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

    public void login(View view) {
        // Get Values of EditText fields
        final String password = passwordField.getText().toString();
        final String username = usernameField.getText().toString();

        //Todo: add Progress View showing progress

        StringRequest request = new StringRequest(Request.Method.POST, URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int statusCode = jsonObject.getInt("code");
                            if(statusCode == 200){
                                context.finish();
                            }else if (statusCode == 404){
                                // TODO : TELL USER INVALID CREDENTIALS
                                context.makeToast("Invalid credentials");
                            }
                        } catch (JSONException e) {
                            context.makeToast("Invalid Response try again");
                            Log.e(TAG, "onResponse: "+response);
                            Log.e(TAG, "onResponse: ",e );
                        }

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

    public void toSignup(View view){
        this.requestQueue.stop();
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        finish();
    }

    public void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

