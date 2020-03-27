package com.dnz.local.buxs.concurrent;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dnz.local.buxs.marketplace.MarketPlaceDescActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import androidx.appcompat.app.AppCompatActivity;

public class AddToCart extends AsyncTask<Void, Integer, Void> {
    private MarketPlaceDescActivity activity;
    private String filename = "cart";
    private boolean addCartValue;

    public AddToCart(MarketPlaceDescActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... context) {
        addCartValue = writeToFile(activity.productID, activity);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (addCartValue) {
            final int count = Integer.parseInt((String) activity.cartCount.getText());
            activity.cartCount.setText(String.valueOf(count + 1));

        }else{
            Toast.makeText(activity, "Item already in cart", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean writeToFile(int data, MarketPlaceDescActivity context) {
        String filedata = readFromFile(context);
        boolean bool = false;
        try {
            JSONObject jsonObject = new JSONObject(filedata);
            JSONArray jsonArray = jsonObject.getJSONArray("cart");

            // Check if value exists in JSONArray
            if (! ifExists(jsonArray, data)){
                bool = true;
                jsonArray = jsonArray.put(data);
                jsonObject = jsonObject.put("cart", jsonArray);
                FileOutputStream fout = context.openFileOutput(filename, Context.MODE_PRIVATE);
                fout.write(jsonObject.toString().getBytes());
            }

        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }

    private String readFromFile(MarketPlaceDescActivity context) {
        String line = null;
        try {
            FileInputStream fin = context.openFileInput(filename);
            InputStreamReader reader;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                reader = new InputStreamReader(fin, StandardCharsets.UTF_8);
            } else {
                reader = new InputStreamReader(fin, "UTF-8");
            }

            StringBuilder builder = new StringBuilder();
            BufferedReader reader1 = new BufferedReader(reader);

            while ((line = reader1.readLine()) != null){
                builder.append(line);
            }

            line = builder.toString();
        } catch (FileNotFoundException e) {
            File file = new File(context.getFilesDir(), filename);
            return "{\"cart\":[]}";
        } catch (IOException e) {

        }
        return line;
    }

    private boolean ifExists(JSONArray jsonArray, int value ){
        return jsonArray.toString().contains(String.valueOf(value));
    }
}
