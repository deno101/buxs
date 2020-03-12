package com.dnz.local.buxs.net;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.dnz.local.buxs.MarketPlace.MarketPlaceActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class StrRequestGetMP {
    private static final String TAG = "StrRequestGetMP";

    private MarketPlaceActivity placeActivity;

    public StrRequestGetMP(MarketPlaceActivity placeActivity) {
        this.placeActivity = placeActivity;
    }

    public void makeRequest(int responseSize){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, placeActivity.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        placeActivity.progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.trim());

                            Iterator<String> stringIterator = jsonObject.keys();

                            while (stringIterator.hasNext()){
                                JSONObject item = jsonObject.getJSONObject(stringIterator.next());

                                //get all the individual items needed to dispaly in recycler view
                                placeActivity.price.add(item.getInt("price"));
                                placeActivity.itemName.add(item.getString("name"));
                                placeActivity.id.add(item.getInt("id"));

                                String url = placeActivity.imgurl+item.getString("image_url1");
                                ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        placeActivity.thumbnail.add(response);
                                        placeActivity.viewAdapter.refresh(placeActivity.thumbnail, placeActivity.itemName, placeActivity.price);
                                    }
                                }, 1024, 1024, null,
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                //placeActivity.makeToast("Error check your internet Connection");
                                                placeActivity.viewAdapter.refresh(placeActivity.thumbnail, placeActivity.itemName, placeActivity.price);

                                            }
                                        });
                                placeActivity.requestQueue.add(imageRequest);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException: "+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        placeActivity.progressDialog.dismiss();
                        placeActivity.makeToast("Error check your internet Connection");
                    }
                });
        placeActivity.requestQueue.add(stringRequest);
    }
}
