package com.dnz.local.buxs.concurrent;

import android.os.AsyncTask;
import android.view.View;

public class AddToCart extends AsyncTask<View,Integer,Void> {

    @Override
    protected Void doInBackground(View... views) {
        publishProgress(2);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
