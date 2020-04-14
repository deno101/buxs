package com.dnz.local.buxs.concurrent;

import android.os.AsyncTask;

import com.dnz.local.buxs.utils.AsyncIFace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class GetCart extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "GetCartCount";

    private AsyncIFace.IFGetCartCount ifGetCartCount;
    private ArrayList<Integer> data = new ArrayList<>();
    private AppCompatActivity activity;

    public GetCart(AsyncIFace.IFGetCartCount ifGetCartCount, AppCompatActivity activity) {
        this.ifGetCartCount = ifGetCartCount;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getCartCount();
        return null;
    }

    @Override
    protected void onPostExecute(Void avoid) {
        ifGetCartCount.onPostExecuteThread(data);
    }

    private void getCartCount() {
        String line = null;
        try {
            FileInputStream fin = activity.openFileInput("cart");
            FileChannel fileChannel = fin.getChannel();
            FileLock lock;

            while ((lock = fileChannel.tryLock(0, Long.MAX_VALUE, true)) == null) {
                Thread.currentThread();
                Thread.sleep(200);
            }

            InputStreamReader reader;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                reader = new InputStreamReader(fin, StandardCharsets.UTF_8);
            } else {
                reader = new InputStreamReader(fin, "UTF-8");
            }

            StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            lock.release();
            line = builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(line);
            JSONArray jsonArray = jsonObject.getJSONArray("cart");

            for (int i = 0; i < jsonArray.length(); i++) {
                data.add(jsonArray.getInt(i));
            }
        } catch (JSONException |NullPointerException e) {
            e.printStackTrace();
        }
    }
}
