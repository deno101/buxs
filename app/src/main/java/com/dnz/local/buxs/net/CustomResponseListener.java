package com.dnz.local.buxs.net;

import android.graphics.Bitmap;
import com.android.volley.Response;
import com.dnz.local.buxs.utils.ProductDataStore;

public abstract class CustomResponseListener implements Response.Listener<Bitmap> {
    protected int position;
    protected ProductDataStore dataStore;

    protected CustomResponseListener(int position, ProductDataStore dataStore) {
        this.position = position;
        this.dataStore = dataStore;
    }
}
