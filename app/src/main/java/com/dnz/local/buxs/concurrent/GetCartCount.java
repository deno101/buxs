package com.dnz.local.buxs.concurrent;

import android.os.AsyncTask;
import android.widget.TextView;

import com.dnz.local.buxs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import androidx.appcompat.app.AppCompatActivity;

public class GetCartCount extends AsyncTask<Void, Void, Void> {

    private AppCompatActivity activity;
    private int count = 0;

    public GetCartCount(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        count = getCartCount();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        TextView textView = activity.findViewById(R.id.cart_amount);
        textView.setText(String.valueOf(count));
    }

    private int getCartCount(){
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

            while((line = bufferedReader.readLine()) != null){
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

            return jsonArray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
