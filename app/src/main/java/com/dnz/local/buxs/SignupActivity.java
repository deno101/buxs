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
import com.dnz.local.buxs.net.MyCookieStore;
import com.dnz.local.buxs.net.URLBuilder;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private final String LOG_TAG = SignupActivity.class.getSimpleName();
    public final String URL = URLBuilder.buildURL("signup");

    private EditText usernameField, firstNameField, lastNameField, emailField,
                        passwordField;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Change color of status bar
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        CookieStore cookieStore = MainActivity.getCookieStore();
        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        usernameField = findViewById(R.id.username);
        firstNameField = findViewById(R.id.first_name);
        lastNameField = findViewById(R.id.last_name);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network net = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, net);
        requestQueue.start();
    }

    public void goBack(View view){
        finish();
    }

    public void signUp(View view){

        final String username = usernameField.getText().toString(),
                        password = passwordField.getText().toString(),
                        fName = firstNameField.getText().toString(),
                        lName = lastNameField.getText().toString(),
                        email = emailField.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(LOG_TAG, response);

                        //TODO :SET THE USER TO BE LOGGED IN, DESTROY ACTIVITY TO PREVIOUS IN STACK

                        passwordField.setText("");
                        usernameField.setText("");
                        firstNameField.setText("");
                        lastNameField.setText("");
                        emailField.setText("");
                        finish();

                        //TODO: LOGIN USER
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
                params.put("first_name", fName);
                params.put("last_name", lName);
                params.put("email", email);

                return params;
            }
        };

        this.requestQueue.add(request);
    }

}
