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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class GetCartCount extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "GetCartCount";

    private AsyncIFace.IFGetCartCount ifGetCartCount;
    private ArrayList<Integer> data = new ArrayList<>();
    private AppCompatActivity activity;

    public GetCartCount(AsyncIFace.IFGetCartCount ifGetCartCount, AppCompatActivity activity) {
        this.ifGetCartCount = ifGetCartCount;
        this.activity = activity;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return getCartCount();
    }

    @Override
    protected void onPostExecute(Integer result) {
        ifGetCartCount.onPostExecuteThread(result, data);
    }

    private int getCartCount() {
        String line = null;
        try {
            FileInputStream fin = activity.openFileInput("cart");
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

            line = builder.toString();
        } catch (FileNotFoundException e) {
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(line);
            JSONArray jsonArray = jsonObject.getJSONArray("cart");

            for (int i =0; i < jsonArray.length(); i++){
                data.add(jsonArray.getInt(i));
            }
            return jsonArray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
