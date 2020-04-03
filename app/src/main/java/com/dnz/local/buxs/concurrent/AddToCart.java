package com.dnz.local.buxs.concurrent;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dnz.local.buxs.marketplace.MarketPlaceDescActivity;
import com.dnz.local.buxs.utils.AsyncIFace;

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
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class AddToCart extends AsyncTask<Integer, Void, Boolean> {
    private AppCompatActivity activity;
    private String filename = "cart";
    private ArrayList<Integer> data = new ArrayList<>();
    private AsyncIFace.IFAddToCart ifAddToCart;


    public AddToCart(AsyncIFace.IFAddToCart ifAddToCart, AppCompatActivity activity) {
        this.activity = activity;
        this.ifAddToCart = ifAddToCart;
    }

    @Override
    protected Boolean doInBackground(Integer... ids) {
        boolean isAddedToCart = false;
        for(Integer id : ids) {
            isAddedToCart = writeToFile(id);
        }
        return isAddedToCart;
    }

    @Override
    protected void onPostExecute(Boolean isAddedToCart) {
        ifAddToCart.onPostExecuteThread(isAddedToCart, data);
    }

    private boolean writeToFile(int data) {
        String filedata = readFromFile();
        boolean bool = false;
        try {
            JSONObject jsonObject = new JSONObject(filedata);
            JSONArray jsonArray = jsonObject.getJSONArray("cart");
            // Check if value exists in JSONArray
            if(! ifExists(jsonArray, data)){
                bool = true;
                jsonArray = jsonArray.put(data);
                jsonObject = jsonObject.put("cart", jsonArray);
                FileOutputStream fout = activity.openFileOutput(filename, Context.MODE_PRIVATE);
                fout.write(jsonObject.toString().getBytes());
            }

            for (int i = 0; i < jsonArray.length(); i++){
                this.data.add(jsonArray.getInt(i));
            }

        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }

    private String readFromFile() {
        String line = null;
        try {
            FileInputStream fin = activity.openFileInput(filename);
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
            File file = new File(activity.getFilesDir(), filename);
            return "{\"cart\":[]}";
        } catch (IOException e) {

        }
        return line;
    }

    private boolean ifExists(JSONArray jsonArray, int value ){
        return jsonArray.toString().contains(String.valueOf(value));
    }
}
