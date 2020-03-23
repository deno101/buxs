package com.dnz.local.buxs.concurrent;

import android.content.Context;
import android.os.AsyncTask;

import com.dnz.local.buxs.R;
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
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class AddToCart extends AsyncTask<MarketPlaceDescActivity, Integer, Void> {
    private MarketPlaceDescActivity activity;
    private String filename = "cart";

    @Override
    protected Void doInBackground(final MarketPlaceDescActivity... context) {
        final int count = Integer.parseInt((String) context[0].cartCount.getText());

        activity = context[0];
        writeToFile(context[0].productID, context[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        final int count = Integer.parseInt((String) activity.cartCount.getText());
        super.onPostExecute(aVoid);
        activity.cartCount.setText(String.valueOf(count + 1));
    }

    private void writeToFile(int data, MarketPlaceDescActivity context) {
        String filedata = readFromFile(context);

        try {
            JSONObject jsonObject = new JSONObject(filedata);
            JSONArray jsonArray = jsonObject.getJSONArray("cart");

            jsonArray = jsonArray.put(data);

            jsonObject = jsonObject.put("cart", jsonArray);
            FileOutputStream fout = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fout.write(jsonObject.toString().getBytes());

        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
